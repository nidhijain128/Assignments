import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;

import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.Sail;
import org.openrdf.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.openrdf.sail.memory.MemoryStore;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class sesame {
	@SuppressWarnings("deprecation")
	public static void main(String args[]) throws RepositoryException, MalformedQueryException, QueryEvaluationException, IOException {
		// creating a .txt to store all the university details
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("data.txt"));
		bufferedWriter.write("University Name\tCity\tLatitude\tLongitude\n");

		// creating a new local repository
		MemoryStore memoryStore = new MemoryStore();
		Sail sail = new ForwardChainingRDFSInferencer(memoryStore);
		Repository repository = new SailRepository(sail);
		repository.initialize();
		RepositoryConnection repositoryConnection = repository.getConnection();

		// query to fetch all private universities in California
		String sQuery = "PREFIX  rdf:    <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX dbpprop:  <http://dbpedia.org/property/> PREFIX dbpedia-owl:  <http://dbpedia.org/ontology/> PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#> SELECT distinct ?univName ?city ?latitude ?longitude WHERE { ?univName dbpprop:type <http://dbpedia.org/resource/Private_university> . ?univName dbpedia-owl:state <http://dbpedia.org/resource/California> . OPTIONAL { ?univName dbpprop:name ?name . } OPTIONAL { ?univName dbpprop:city ?city. } OPTIONAL { ?univName geo:lat ?latitude . } OPTIONAL { ?univName geo:long ?longitude . }}";
		Query query = QueryFactory.create(sQuery);
		QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
		ResultSet resultSet = queryExecution.execSelect();
		String schema = "http://schema.org/";
		String sample = "http://www.nidhipjain.com/";
		ValueFactory vFactory = repository.getValueFactory();
		int number = 1;

		// for each university, writing the data into file and adding the
		// triples into triple store
		while (resultSet.hasNext()) {
			QuerySolution querySolution = resultSet.nextSolution();
			String university, city, latitude, longitude;
			URI s, p, o;
			Literal l;

			// creating triples for universitiy name
			if (querySolution.get("?univName") != null) {
				university = querySolution.get("?univName").toString();
				String[] getUniversityName = university.split("/");
				String universityName = getUniversityName[getUniversityName.length - 1].replace("_", " ");
				bufferedWriter.write(universityName + "\t");

				s = vFactory.createURI(university.toString());
				p = vFactory.createURI("http://www.w3.org/2000/01/rdf-schema#type");
				o = vFactory.createURI(schema + "CollegeOrUniversity");
				repositoryConnection.add(s, p, o);

				s = vFactory.createURI(university.toString());
				p = vFactory.createURI(schema + "name");
				l = vFactory.createLiteral(universityName);
				repositoryConnection.add(s, p, l);
			} else {
				university = "";
				bufferedWriter.write("\t");
			}

			// creating triples for location and city details
			if (querySolution.get("?city") != null) {
				city = querySolution.get("?city").toString();
				String[] getCityName = city.split("/");
				String cityName = getCityName[getCityName.length - 1].replace("_", " ");
				bufferedWriter.write(cityName + "\t");

				s = vFactory.createURI(university.toString());
				p = vFactory.createURI(schema + "location");
				o = vFactory.createURI(sample + "loc" + number);
				repositoryConnection.add(s, p, o);

				s = vFactory.createURI(sample + "loc" + number);
				p = vFactory.createURI("http://www.w3.org/2000/01/rdf-schema#type");
				o = vFactory.createURI(schema + "Place");
				repositoryConnection.add(s, p, o);

				s = vFactory.createURI(sample + "loc" + number);
				p = vFactory.createURI(schema + "address");
				o = vFactory.createURI(sample + "address" + number);
				repositoryConnection.add(s, p, o);

				s = vFactory.createURI(sample + "address" + number);
				p = vFactory.createURI(schema + "addressRegion");
				l = vFactory.createLiteral(city.toString());
				repositoryConnection.add(s, p, l);

				s = vFactory.createURI(sample + "loc" + number);
				p = vFactory.createURI(schema + "geo");
				o = vFactory.createURI(sample + "geo" + number);
				repositoryConnection.add(s, p, o);
			} else {
				city = null;
				bufferedWriter.write("\t");
			}
			
			if(querySolution.get("?city") == null && (querySolution.get("?latitude") != null || querySolution.get("?longitude") != null)) {
				s = vFactory.createURI(university.toString());
				p = vFactory.createURI(schema + "location");
				o = vFactory.createURI(sample + "loc" + number);
				repositoryConnection.add(s, p, o);

				s = vFactory.createURI(sample + "loc" + number);
				p = vFactory.createURI("http://www.w3.org/2000/01/rdf-schema#type");
				o = vFactory.createURI(schema + "Place");
				repositoryConnection.add(s, p, o);

				s = vFactory.createURI(sample + "loc" + number);
				p = vFactory.createURI(schema + "address");
				o = vFactory.createURI(sample + "address" + number);
				repositoryConnection.add(s, p, o);

				s = vFactory.createURI(sample + "address" + number);
				p = vFactory.createURI(schema + "addressRegion");
				l = vFactory.createLiteral(sample + "city" + number);
				repositoryConnection.add(s, p, l);

				s = vFactory.createURI(sample + "loc" + number);
				p = vFactory.createURI(schema + "geo");
				o = vFactory.createURI(sample + "geo" + number);
				repositoryConnection.add(s, p, o);
			}

			// creating triples for latitude and longitude
			if (querySolution.get("?latitude") != null) {
				latitude = querySolution.get("?latitude").toString();
				String[] getLatitude = latitude.split("\\^");
				String latitudeValue = getLatitude[0];
				bufferedWriter.write(latitudeValue + "\t");

				s = vFactory.createURI(sample + "geo" + number);
				p = vFactory.createURI(schema + "latitude");
				l = vFactory.createLiteral(latitude.toString());
				repositoryConnection.add(s, p, l);
			} else {
				latitude = "";
				bufferedWriter.write("\t");
			}

			if (querySolution.get("?longitude") != null) {
				longitude = querySolution.get("?longitude").toString();
				String[] getLongitude = longitude.split("\\^");
				String longitudeValue = getLongitude[0];
				bufferedWriter.write(longitudeValue + "\n");

				s = vFactory.createURI(sample + "geo" + number);
				p = vFactory.createURI(schema + "longitude");
				l = vFactory.createLiteral(longitude.toString());
				repositoryConnection.add(s, p, l);
			} else {
				longitude = "";
				bufferedWriter.write("\n");
			}
			number++;
		}

		// query to fetch all the data from the local triple store
		String finalQuery = "SELECT distinct ?univName ?latitude ?longitude WHERE { ?univName <http://www.w3.org/2000/01/rdf-schema#type> <http://schema.org/CollegeOrUniversity> . ?univName <http://schema.org/location> ?location . ?location <http://schema.org/geo> ?geo . ?geo <http://schema.org/latitude> ?latitude . ?geo <http://schema.org/longitude> ?longitude . }";
		TupleQuery tQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL, finalQuery);
		TupleQueryResult tqResult = tQuery.evaluate();
		double UCLALatitude = 34.06, UCLALongitude = -118.44;
		int totalUnivs = 0;
		int withinUCLA = 0;

		System.out.println();
		System.out.println("Name of universities within 200 miles of UCLA:");

		// compare the latitude and longitude of each university with that of
		// UCLA and print if it's less than 200 miles
		while (tqResult.hasNext()) {
			BindingSet bindingSet = tqResult.next();

			// extract latitude and longitude values for each university
			String latitude = bindingSet.getValue("latitude").toString();
			String[] getLatitude = latitude.split("\\^");
			String latitudeValue = getLatitude[0];
			latitudeValue = latitudeValue.substring(1);
			double latitudeDouble = Double.parseDouble(latitudeValue);

			String longitude = bindingSet.getValue("longitude").toString();
			String[] getlongitude = longitude.split("\\^");
			String longitudeValue = getlongitude[0];
			longitudeValue = longitudeValue.substring(1);
			double longitudeDouble = Double.parseDouble(longitudeValue);

			String universityName = bindingSet.getValue("univName").toString();

			// consider the university if it's within 200 miles of UCLA
			double distance = distance(UCLALatitude, UCLALongitude, latitudeDouble, longitudeDouble);
			if (distance < 200) {
				String[] getUniversityName = universityName.split("/");
				universityName = getUniversityName[getUniversityName.length - 1].replace("_", " ");
				universityName = URLDecoder.decode(universityName);
				System.out.println(universityName);
				withinUCLA++;
			}
			totalUnivs++;
		}

		// print data
		System.out.println();
		System.out.println("Total number of private universities in California: " + number);
		System.out.println("Out of the above " + number + " universities, total number of universities with latitude and longitude details: "
				+ totalUnivs);
		System.out.println("Out of the above " + totalUnivs + " universities, total number of universities within 200 miles of UCLA: " + withinUCLA);
		bufferedWriter.close();
		repositoryConnection.close();
		repository.shutDown();
	}

	// to calculate the distance between two pairs of latitude and longitude.
	// Source - www.distancesfrom.com
	private static double distance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		return (dist);
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}
}