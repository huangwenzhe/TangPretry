package Projict;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class Request {
    public static void main(String[] args) throws IOException {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        String url = "https://so.gushiwen.org/shiwenv_44ba4afb80db.aspx";

        HtmlPage page = webClient.getPage(url);
        HtmlElement body =page.getBody();

//        List<HtmlElement> list = body.getElementsByAttribute(
//                "div",
//                "class",
//                "typecont"
//        );

//
//        int count =0 ;
//        for(HtmlElement element : list){
//            List <HtmlElement>aelement = element.getElementsByTagName("a");
//            for(HtmlElement e : aelement){
//                //每个古诗的链接
//                System.out.println(e.getAttribute("href"));
//                // <span><a href="https://so.gushiwen.org/shiwenv_44ba4afb80db.aspx" target="_blank">山居秋暝</a>(王维)</span>
//                //即获得每首古诗的题目
//                System.out.println(e.getTextContent());
//                count ++;
//            }
//        }
//        System.out.println(count);

        {
            String path = "//div[@class='cont']/h1/text()";
            Object o =  body.getByXPath(path).get(0);
            DomText domText = (DomText)o;
            System.out.println(domText.asText());
        }

    }
}
