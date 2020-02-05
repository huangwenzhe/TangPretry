package Projict;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class Request {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
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

                list = new ArrayList<>();

                //标题
                {
                    String path = "//div[@class='cont']/h1/text()";
                    Object o =  bodys.getByXPath(path).get(0);
                    DomText domText = (DomText)o;

                    System.out.println(domText.asText());

                    divids(domText.asText());
                }
                //朝代
                {
                    String xpath = "//div[@class='cont']/p[@class='source']/a[1]/text()";
                    Object o = bodys.getByXPath(xpath).get(0);DomText domText = (DomText)o;
                    System.out.println(domText.asText());
                }
                //作者
                {
                    String xpath = "//div[@class='cont']/p[@class='source']/a[2]/text()";
                    Object o = bodys.getByXPath(xpath).get(0);DomText domText = (DomText)o;
                    System.out.println(domText.asText());
                }
                //内容
                {
                    String xpath = "//div[@class='cont']/div[@class='contson']";
                    Object o = bodys.getByXPath(xpath).get(0);
                    HtmlElement e1 = (HtmlElement)o;
                    String ss = e1.getTextContent();
                    String sha =  caculateSha256(ss);



                    System.out.println(sha);
                    System.out.println(e1.getTextContent().trim());

                    divids(ss);
                }
                count ++;
            }
        }
        System.out.println(count);

    }
    static     List<String> list = new ArrayList<>();
    private static void divids(String s) {
        List<Term> termList = NlpAnalysis.parse(s).getTerms();
        for (Term term : termList) {
           list.add(term.getRealName());
            System.out.println(term.getNatureStr() + ":  " + term.getRealName());
        }
    }

    private static String caculateSha256(String ss) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[]bytes = ss.getBytes("UTF-8");
        messageDigest.update(bytes);
        byte[]result = messageDigest.digest();
        StringBuffer s = new StringBuffer();
        for (byte b : result){
            s.append(b);
        }
        return s.toString();
    }
}
