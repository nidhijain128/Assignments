package cs548a1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class assignment1 {

	static FileWriter file;
	static int count = 0;
	
	public static void main(String s[]) throws IOException{
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);

        HtmlPage nextPage;
        String url = "http://www.portlandartmuseum.us/mwebcgi/mweb.exe?request=jump;dtype=d;startat=17";

        final WebClient webclient = new WebClient(BrowserVersion.CHROME);
        final HtmlPage page = webclient.getPage(url);
        System.out.println(page.getWebResponse().getContentAsString());
        
        List<HtmlAnchor> articles = (List<HtmlAnchor>) page.getByXPath("//DIV[@class='searchResultsRowInner']");
        //List<HtmlAnchor> articles = (List<HtmlAnchor>) page.getByXPath("//div[@class='hform1']/a[@class='lblentrylink']");
        System.out.println(page.toString());
        System.out.println(articles.size());
	}
}
