package Projict;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.List;


public class Request {
    public static void main(String[] args) throws IOException {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
  //      String url = "https://so.gushiwen.org/shiwenv_44ba4afb80db.aspx";

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
         //       System.out.println(e.getAttribute("href"));//                //即获得每首古诗的题目
           //     System.out.println(e.getTextContent());

                String s = e.getAttribute("href");
                String urls = "https://so.gushiwen.org"+s;
                HtmlPage p = webClient.getPage(urls);
                HtmlElement bodys = p.getBody();
                {
                    String path = "//div[@class='cont']/h1/text()";
                    Object o =  bodys.getByXPath(path).get(0);
                    DomText domText = (DomText)o;
                    System.out.println(domText.asText());
                   // System.out.println(o);
                }
                {
                    String xpath = "//div[@class='cont']/p[@class='source']/a[1]/text()";
                    Object o = bodys.getByXPath(xpath).get(0);DomText domText = (DomText)o;
                    System.out.println(domText.asText());
                }
                {
                    String xpath = "//div[@class='cont']/p[@class='source']/a[2]/text()";
                    Object o = bodys.getByXPath(xpath).get(0);DomText domText = (DomText)o;
                    System.out.println(domText.asText());
                }
                {
                    String xpath = "//div[@class='cont']/div[@class='contson']";
                    Object o = bodys.getByXPath(xpath).get(0);
                    HtmlElement e1 = (HtmlElement)o;
                    System.out.println(e1.getTextContent().trim());
                }
                count ++;
            }
        }
        System.out.println(count);
//
//        {
//            String path = "//div[@class='cont']/h1/text()";
//            Object o =  body.getByXPath(path).get(0);
//            DomText domText = (DomText)o;
//            System.out.println(domText.asText());
//        }

    }
}
