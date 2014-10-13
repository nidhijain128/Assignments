import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ExtractPaintingData {

	static FileWriter file;
	static int count = 0;
	static List<String> inventoryNumbers= new ArrayList<String>();

	public static void main(String s[]) throws IOException {
		//creating a file to write JSON data
		file = new FileWriter("datasetNationalGallery.json");
		file.write("[");
		Document htmlPage = null;
		
		//traversing through various decades to get the paintings from that decade
		for (int decade = 1750; decade <= 1930; decade = decade + 10) {
			htmlPage = Jsoup.connect("http://www.nationalgallery.org.uk/paintings/explore-the-paintings/browse-by-century/*/decade/" + decade + "/").timeout(0).get();
			createDataSet(htmlPage);
		}
		file.write("]");
		file.close();
		System.out.println("Total Number of datasets obtained: " + count);
	}

	@SuppressWarnings("unchecked")
	public static void createDataSet(Document doc) throws IOException {

		//link to the page within each image to get the details of the painting
		Elements paintingURL = doc.select("div.content div.tableContainer table tr td.preview div.content a");
		Elements imageURL = doc.select("div.content div.tableContainer table tr td.preview div.content a img");
		
		//iterate over all paintings of particular decade
		for (int i = 0; i < paintingURL.size(); i++) {
			JSONObject obj = new JSONObject();
			
			//get the image link for the painting
			String imageDetails = imageURL.get(i).attr("src");
			
			//navigate to the key facts page
			String keyFactsURL = paintingURL.get(i).attr("href") + "/*/key-facts";
			Document keyFacts = Jsoup.connect(keyFactsURL).timeout(0).get();
			
			//extract the number of key facts
			Elements numberOfKeyFacts = keyFacts.select("div.catalogueInfo div.infoLines div.info label.field");
			
			String inventoryNumber = "";
			
			//add the image URL into JSON object
			obj.put("Image URL on Website", imageDetails);
			
			//add all the key facts and add them to the JSON object
			for(int j=0;j<numberOfKeyFacts.size();j++) {
				String nameKeyFact = keyFacts.select("div.catalogueInfo div.infoLines div.info:eq(" + j + ") label.field").text();
				String valueKeyFact = keyFacts.select("div.catalogueInfo div.infoLines div.info:eq(" + j + ") div.value").text();
				obj.put(nameKeyFact,valueKeyFact);
				
				//keep track of inventory number to avoid duplicates
				if(nameKeyFact.equals("Inventory number")){
					inventoryNumber = valueKeyFact;
				}
			}

			//write the json object to the file
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(obj);

			//check if the JSON object to be written to the file already exists in the file
			if(!inventoryNumbers.contains(inventoryNumber)) {
				if(count!=0)
					file.write(",");
				file.write(json);
				inventoryNumbers.add(inventoryNumber);
				count++;
			}
		}
		file.flush();
	}
}
