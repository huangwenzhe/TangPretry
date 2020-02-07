package Projict;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Request {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, SQLException {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        String url = "https://so.gushiwen.org/gushi/tangshi.aspx";
        HtmlPage page = webClient.getPage(url);
        HtmlElement body =page.getBody();
        List<HtmlElement> list = body.getElementsByAttribute(
                "div",
                "class",
                "typecont"
        );

        int count =0 ;

        for(HtmlElement element : list){
            list = new ArrayList<>();

            List <HtmlElement>aelement = element.getElementsByTagName("a");
            for(HtmlElement e : aelement){
                //每个古诗的链接
         //       System.out.println(e.getAttribute("href"));              //即获得每首古诗的题目
           //     System.out.println(e.getTextContent());

                String s = e.getAttribute("href");
                System.out.println(s);

                String urls = "https://so.gushiwen.org"+s;
                HtmlPage p = webClient.getPage(urls);
                HtmlElement bodys = p.getBody();

                StringBuffer words = new StringBuffer();
                //标题
                String title;
                {
                    String path = "//div[@class='cont']/h1/text()";
                    Object o =  bodys.getByXPath(path).get(0);
                    DomText domText = (DomText)o;

                    System.out.println(domText.asText());
                    title = domText.asText().trim();
                    words.append(divids(domText.asText().trim())).append(",");
                }
                //朝代
                String dy;
                {
                    String xpath = "//div[@class='cont']/p[@class='source']/a[1]/text()";
                    Object o = bodys.getByXPath(xpath).get(0);DomText domText = (DomText)o;
                    dy=domText.asText().trim();
                    System.out.println(domText.asText());
                }
                //作者
                String author;
                {
                    String xpath = "//div[@class='cont']/p[@class='source']/a[2]/text()";
                    Object o = bodys.getByXPath(xpath).get(0);DomText domText = (DomText)o;
                    author = domText.asText().trim();
                    System.out.println(domText.asText());
                }
                //内容
                String sha256 ;
                String content;
                {
                    String xpath = "//div[@class='cont']/div[@class='contson']";
                    Object o = bodys.getByXPath(xpath).get(0);
                    HtmlElement e1 = (HtmlElement)o;
                    String ss = e1.getTextContent();
                     sha256 =  caculateSha256(ss.trim());

                     content = e1.getTextContent().trim();
                    System.out.println(sha256);
                    System.out.println(e1.getTextContent().trim());

                    words.append(divids(ss.trim()));
                }
                插入诗词(sha256,dy,title,author,content,words.toString());
                count ++;
            }
        }
        System.out.println(count);
    }

    private static String divids(String s) {
        StringBuffer str = new StringBuffer();
        List<Term> termList = NlpAnalysis.parse(s).getTerms();
        for (Term term : termList) {
            str.append(term.getRealName()).append(",");
            System.out.println(term.getNatureStr() + ":  " + term.getRealName());
        }
        return str.toString();
    }

    private static String caculateSha256(String ss) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[]bytes = ss.getBytes("UTF-8");
        messageDigest.update(bytes);
        byte[]result = messageDigest.digest();
        StringBuffer s = new StringBuffer();
        for (byte b : result){
            System.out.printf("%02x ",b);
            s.append(String.format("%02x",b));
        }
        System.out.println(s);
        return s.toString();
    }
    private static void 插入诗词(String sha256, String dy, String title, String author, String s,String words) throws SQLException {
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setServerName("127.0.0.1");
        dataSource.setPort(3306);
        dataSource.setUser("root");
        dataSource.setPassword("123456789");
        dataSource.setDatabaseName("db_tangshi");
        dataSource.setUseSSL(false);
        dataSource.setCharacterEncoding("UTF8");

        try (Connection connection = dataSource.getConnection()){
            String sql =  "INSERT INTO tangpoetry " +
                    "(sha256, dynasty, title, author, " +
                    "content,words) " +
                    "VALUES (?,?,?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setString(1,sha256);
                statement.setString(2,dy);
                statement.setString(3,title);
                statement.setString(4,author);
                statement.setString(5,s);
                statement.setString(6,words);
                statement.executeUpdate();
            }

        }

    }
}
