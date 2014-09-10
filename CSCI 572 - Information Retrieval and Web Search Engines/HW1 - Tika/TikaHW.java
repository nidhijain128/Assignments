import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.apache.commons.lang3.StringUtils;

public class TikaHW {

	List<String> keywords;
	PrintWriter logfile;
	int num_keywords, num_files, num_fileswithkeywords;
	Map<String, Integer> keyword_counts;
	Date timestamp;
	static int count = 0;

	/**
	 * constructor DO NOT MODIFY
	 */
	public TikaHW() {
		keywords = new ArrayList<String>();
		num_keywords = 0;
		num_files = 0;
		count = 0;
		num_fileswithkeywords = 0;
		keyword_counts = new HashMap<String, Integer>();
		timestamp = new Date();
		try {
			logfile = new PrintWriter("log.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * destructor DO NOT MODIFY
	 */
	protected void finalize() throws Throwable {
		try {
			logfile.close();
		} finally {
			super.finalize();
		}
	}

	/**
	 * main() function instantiate class and execute DO NOT MODIFY
	 */
	public static void main(String[] args) {
		TikaHW instance = new TikaHW();
		instance.run();
	}

	/**
	 * execute the program DO NOT MODIFY
	 */
	private void run() {

		// Open input file and read keywords
		try {
			BufferedReader keyword_reader = new BufferedReader(new FileReader(
					"keywords.txt"));
			String str;
			while ((str = keyword_reader.readLine()) != null) {
				keywords.add(str);
				num_keywords++;
				keyword_counts.put(str, 0);
			}
			keyword_reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Open all pdf files, process each one
		File pdfdir = new File("./vault");
		File[] pdfs = pdfdir.listFiles(new PDFFilenameFilter());
		for (File pdf : pdfs) {
			num_files++;
			processfile(pdf);
		}

		// Print output file
		try {
			PrintWriter outfile = new PrintWriter("output.txt");
			outfile.print("Keyword(s) used: ");
			if (num_keywords > 0)
				outfile.print(keywords.get(0));
			for (int i = 1; i < num_keywords; i++)
				outfile.print(", " + keywords.get(i));
			outfile.println();
			outfile.println("No of files processed: " + num_files);
			outfile.println("No of files containing keyword(s): "
					+ num_fileswithkeywords);
			outfile.println();
			outfile.println("No of occurrences of each keyword:");
			outfile.println("----------------------------------");
			for (int i = 0; i < num_keywords; i++) {
				String keyword = keywords.get(i);
				outfile.println("\t" + keyword + ": "
						+ keyword_counts.get(keyword));
			}
			outfile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Process a single file
	 * 
	 * Here, you need to: - use Tika to extract text contents from the file -
	 * (optional) check OCR quality before proceeding - search the extracted
	 * text for the given keywords - update num_fileswithkeywords and
	 * keyword_counts as needed - update log file as needed
	 * 
	 * @param f
	 *            File to be processed
	 */
	private void processfile(File f) {

		/***** YOUR CODE GOES HERE *****/
		PDFParser pdf = new PDFParser();
		InputStream input;
		boolean isKeywordPresent = false;
		try {
			//parse the PDF file passed to this method
			input = new FileInputStream(f);
			ContentHandler ch = new BodyContentHandler(2147483647);
			Metadata meta = new Metadata();
			ParseContext pc = new ParseContext();
			pdf.parse(input, ch, meta, pc);
			//convert the entire PDF to a single string
			String fileText=ch.toString();
			//split the string on all characters except alphabets
			String indiKeywords[] = fileText.split("[^a-zA-Z]+");
			boolean flag= false;
			for(int j=0;j<indiKeywords.length;j++)
				if(indiKeywords[j].matches("[a-zA-Z]+")) {
					for(int i=0;i<keywords.size();i++) {
						flag = false;
						String keyword[] = keywords.get(i).split(" ");
						String text = "";
						//case if keyword has more than 1 word
						if(keyword.length>1) {
							//check for all the words in the keyword
							for(int k=0;k<keyword.length;k++) 
								if(j<indiKeywords.length-1)
									text = text + indiKeywords[j+k] + " ";
							if(!text.equals(""))
								text.substring(0, text.length()-1);
							//check if Levenshtein distance is less than or equal to 1
							if(StringUtils.getLevenshteinDistance(text.toLowerCase(), keywords.get(i).toLowerCase())<=1)
								flag = true;
							if(flag) {
								//record the count of the keyword, increase the count for the keyword, update log
								keyword_counts.put(keywords.get(i), keyword_counts.get(keywords.get(i))+1);
								isKeywordPresent = true;
								updatelog(keywords.get(i), f.getName());
							}
						}
						//check for keyword with a single word
						else {
							int g = 1000;
							//check if Levenshtein distance is less than or equal to 1
							g = StringUtils.getLevenshteinDistance(indiKeywords[j].toLowerCase(), keyword[0].toLowerCase());
							if(indiKeywords[j].equalsIgnoreCase(keyword[0]) || g<=1) {
								//record the count of the keyword, increase the count for the keyword, update log
								keyword_counts.put(keywords.get(i), keyword_counts.get(keywords.get(i))+1);
								isKeywordPresent = true;
								updatelog(keywords.get(i), f.getName());
							}
						}
					}
				}
			//increase the count for the files containing keywords
			if(isKeywordPresent)
				num_fileswithkeywords++;
			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TikaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// to update the log file with a search hit, use:
		// updatelog(keyword,f.getName());

	}

	/**
	 * Update the log file with search hit Appends a log entry with the system
	 * timestamp, keyword found, and filename of PDF file containing the keyword
	 * DO NOT MODIFY
	 */
	private void updatelog(String keyword, String filename) {
		timestamp.setTime(System.currentTimeMillis());
		logfile.println(timestamp + " -- \"" + keyword + "\" found in file \""
				+ filename + "\"");
		logfile.flush();
	}

	/**
	 * Filename filter that accepts only *.pdf DO NOT MODIFY
	 */
	static class PDFFilenameFilter implements FilenameFilter {
		private Pattern p = Pattern.compile(".*\\.pdf",
				Pattern.CASE_INSENSITIVE);

		public boolean accept(File dir, String name) {
			Matcher m = p.matcher(name);
			return m.matches();
		}
	}
}