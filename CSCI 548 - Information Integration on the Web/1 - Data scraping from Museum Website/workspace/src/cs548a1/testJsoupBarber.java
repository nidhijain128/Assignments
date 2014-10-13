package cs548a1;

import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class testJsoupBarber {
	static FileWriter file;
	static int count=0;
	public static void main(String s[]) throws IOException{
		Document doc = null;
		file = new FileWriter("test1.json");
		for(int addRecord = 1;addRecord<121;addRecord=addRecord+15) {
			doc = Jsoup.connect("http://collection.blantonmuseum.org/PRT131?rec=" + addRecord + "&sid=295636&x=4643129&port=131").timeout(0).get();
		Elements paintingURL = doc.select("table tr td table tr td a");
		
		for(int i=paintingURL.size()-1;i>=5;i=i-3) {
			String imageURL = paintingURL.get(i).attr("href");
			Document doc1 = Jsoup.connect("http://collection.blantonmuseum.org/" + imageURL).timeout(0).get();
			JSONObject obj = new JSONObject();
			Elements artist = doc1.select("table tr td a");
			obj.put("Artist", artist.get(21).text());
			
			
			Elements paintingTitle = doc1.select("table tr td b");
			obj.put("Painting Title", paintingTitle.get(1).text());
			
			Elements medSupp = doc1.select("table tr td");
			obj.put("Object Type", medSupp.get(15).text());
			obj.put("Medium and Support", medSupp.get(19).text());
			obj.put("Credit Line", medSupp.get(23).text());
			obj.put("Accession Number", medSupp.get(27).text());
			obj.put("Number", ++count);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(obj);
			
			file.write(json);
		}
		}
		file.close();
	}
}
