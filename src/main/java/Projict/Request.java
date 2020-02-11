package Projict;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import javax.sql.DataSource;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Request {
    public static class  Job implements Runnable{
        private  String url;
        private  MessageDigest messageDigest;
        private DataSource dataSource;

        public Job(String url, MessageDigest messageDigest, DataSource dataSource) {
            this.url = url;
            this.messageDigest = messageDigest;
            this.dataSource = dataSource;
        }

        @Override
        public void run() {
                WebClient webClient  = new WebClient(BrowserVersion.CHROME);
                webClient.getOptions().setJavaScriptEnabled(false);
                webClient.getOptions().setCssEnabled(false);

                String divs1;
                String divs2;
            try {
                HtmlPage page = webClient.getPage(url);
                String xpath;
                DomText domText;

                xpath = "//div[@class='cont']/h1/text()";
                domText = (DomText) page.getBody().getByXPath(xpath).get(0);
                String title = domText.asText();
                divs1 = divids(title);

                xpath = "//div[@class='cont']/p[@class='source']/a[1]/text()";
                domText = (DomText) page.getBody().getByXPath(xpath).get(0);
                String dynastry = domText.asText();


                xpath = "//div[@class='cont']/p[@class='source']/a[2]/text()";
                domText = (DomText) page.getBody().getByXPath(xpath).get(0);
                String author = domText.asText();

                xpath = "//div[@class='cont']/div[@class='contson']";
                HtmlElement element = (HtmlElement)page.getBody().getByXPath(xpath).get(0);
                String text = element.asText().trim();
                divs2 = divids(text);

                String s = title + text;

                //计算sha256
                byte[]bytes = s.getBytes("UTF-8");
                messageDigest.update(bytes);
                byte[]result = messageDigest.digest();
                StringBuffer ss= new StringBuffer();
                for (byte b : result){
                    ss.append(String.format("%02x",b));
                }


                //计算分词
                String div = divs1 + divs2;

                //插入数据库
                try (Connection connection = dataSource.getConnection()){
                    String sql =  "INSERT INTO tangpoetry " +
                            "(sha256, dynasty, title, author, " +
                            "content,words) " +
                            "VALUES (?,?,?, ?, ?, ?)";
                    try (PreparedStatement statement = connection.prepareStatement(sql)){
                        statement.setString(1,ss.toString());
                        statement.setString(2,dynastry);
                        statement.setString(3,title);
                        statement.setString(4,author);
                        statement.setString(5,text);
                        statement.setString(6,div);
                        statement.executeUpdate();
                    }
                }

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        }
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(30);

        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        String url = "https://so.gushiwen.org/gushi/tangshi.aspx";
        List<String> detilUrlList =  new ArrayList<>();
        {
            HtmlPage page = webClient.getPage(url);
            List<HtmlElement> list = page.getBody().getElementsByAttribute("div","class","typecont");
            for(HtmlElement element : list){
                List<HtmlElement> alement = element.getElementsByTagName("a");
                for(HtmlElement e : alement){
                    String detilUrl = e.getAttribute("href");
                    detilUrlList.add("https://so.gushiwen.org"+detilUrl);
                }
            }
        }
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setServerName("127.0.0.1");
        dataSource.setPort(3306);
        dataSource.setUser("root");
        dataSource.setPassword("123456789");
        dataSource.setDatabaseName("db_tangshi");
        dataSource.setUseSSL(false);
        dataSource.setCharacterEncoding("UTF8");

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        CountDownLatch countDownLatch = new CountDownLatch(detilUrlList.size());

        for(String u : detilUrlList){
            service.execute( new Job(u,messageDigest,dataSource));
        }
        countDownLatch.await();
        service.shutdown();

    }

    private static String divids(String s) {
        StringBuffer str = new StringBuffer();
        List<Term> termList = NlpAnalysis.parse(s).getTerms();
        for (Term term : termList) {
            if(term.getNatureStr().equals("w")) {
                continue;
            }
            if(term.getNatureStr().equals(null)){
                continue;
            }
            str.append(term.getRealName()).append(",");
            System.out.println(term.getNatureStr() + ":  " + term.getRealName());
        }
        return str.toString();
    }

}
