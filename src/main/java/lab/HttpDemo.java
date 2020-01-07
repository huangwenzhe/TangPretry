package lab;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class HttpDemo {
    public static void main(String[] args) throws IOException {
        //无界面的浏览器(HTTP 客户端)
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        //关闭了浏览器的js执行引擎，不再执行网页中的js脚本
        webClient.getOptions().setJavaScriptEnabled(false);
        //关闭了浏览器的CSS执行引擎，不再执行网页中的css布局
        webClient.getOptions().setCssEnabled(false);
        HtmlPage htmlPage = webClient.getPage("https://so.gushiwen.org/shiwenv_45c396367f59.aspx");

        /*
        System.out.println(htmlPage);
     File file =new File("唐诗三百首\\列表页.html");
     file.delete();
     htmlPage.save(file);

     //  从html中提取我们需要的信息
       HtmlElement body =  htmlPage.getBody();
       List<HtmlElement>elements =  body.getElementsByAttribute("div","class","typecont");
       /*
       for(HtmlElement e :elements){
           System.out.println(e);
       }


        HtmlElement divElement = elements.get(0);
       List<HtmlElement> aElement = divElement.getElementsByAttribute("a","target","_blank");
       for(HtmlElement e : aElement){
           System.out.println(e);
       }
        System.out.println(aElement.size());
       */


        HtmlElement body = htmlPage.getBody();
         /*
            List<HtmlElement> elements = body.getElementsByAttribute(
                    "div",
                    "class",
                    "contson"
            );

            for (HtmlElement element : elements) {
                System.out.println(element);
            }

            System.out.println(elements.get(0).getTextContent().trim());
             */

        // 标题
        {
            String xpath = "//div[@class='cont']/h1/text()";
            Object o = body.getByXPath(xpath).get(0);DomText domText = (DomText)o;
            System.out.println(domText.asText());
        }
        {
            String xpath = "//div[@class='cont']/p[@class='source']/a[1]/text()";
            Object o = body.getByXPath(xpath).get(0);DomText domText = (DomText)o;
            System.out.println(domText.asText());
        }
        {
            String xpath = "//div[@class='cont']/p[@class='source']/a[2]/text()";
            Object o = body.getByXPath(xpath).get(0);DomText domText = (DomText)o;
            System.out.println(domText.asText());
        }
        {
            String xpath = "//div[@class='cont']/div[@class='contson']";
            Object o = body.getByXPath(xpath).get(0);
            HtmlElement element = (HtmlElement)o;
            System.out.println(element.getTextContent().trim());
        }
    }
}
