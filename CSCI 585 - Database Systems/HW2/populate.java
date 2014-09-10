import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class populate {
	public static void main(String s[]) throws IOException, FileNotFoundException, SQLException
	{
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            String url = new String();
            url = "jdbc:oracle:thin:@localhost:1521:hw2";
            String userId = "system";
            String password = "hw2";
            Connection con = DriverManager.getConnection(url, userId, password);
            con.setAutoCommit(true);
		
            BufferedReader reader = new BufferedReader(new FileReader(s[1]));
            String line = null;
            String deleteS = "Delete from students";
            PreparedStatement stmt = con.prepareStatement(deleteS);
            stmt.executeUpdate();
            System.out.println("Deleted old table entries from students");
            while ((line = reader.readLine()) != null) {
                String id = line.substring(0, line.indexOf(","));
                String xCord = line.substring(line.indexOf(",") + 2, line.lastIndexOf(","));
                String yCord = line.substring(line.lastIndexOf(",") + 2);
                String insertS = "INSERT INTO students VALUES(? ,SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE(?, ?, NULL), NULL, NULL))";
                stmt = con.prepareStatement(insertS);
                stmt.setString(1, id);
                stmt.setLong(2, Long.valueOf(xCord));
                stmt.setLong(3, Long.valueOf(yCord));
                stmt.executeUpdate();
                stmt.close();
            }
			System.out.println("Success!! Students values added");
            
            reader = new BufferedReader(new FileReader(s[2]));
            line = null;
            String deleteAS = "Delete from announcementsystems";
            stmt = con.prepareStatement(deleteAS);
            stmt.executeUpdate();
            System.out.println("Deleted old table entries from announcement Systems");
            while ((line = reader.readLine()) != null) {
                String values[] = line.split(",");
                for(int i=0; i<values.length;i++)
                    values[i] = values[i].trim();
                int pointQueryX = Integer.parseInt(values[1]);
                int pointQueryY = Integer.parseInt(values[2]);
                int radius = Integer.parseInt(values[3]);
                String insertAS = "INSERT INTO announcementsystems VALUES(? ,SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE(?, ?, NULL), NULL, NULL), ?, sdo_geometry(2003,NULL,NULL, sdo_elem_info_array(1,1003,4), sdo_ordinate_array("+pointQueryX+","+(pointQueryY-radius)+", "+(pointQueryX+radius)+","+pointQueryY+", "+pointQueryX+","+(pointQueryY+radius)+")))";
         
                stmt = con.prepareStatement(insertAS);
                stmt.setString(1, values[0]);
                stmt.setLong(2, Long.valueOf(values[1]));
                stmt.setLong(3, Long.valueOf(values[2]));
                stmt.setLong(4, Long.valueOf(values[3]));
                stmt.executeUpdate();
                stmt.close();
            }
			System.out.println("Success!! Announcement Systems values added");
            
            reader = new BufferedReader(new FileReader(s[0]));
            line = null;
            String deleteB = "Delete from buildings";
            stmt = con.prepareStatement(deleteB);
            stmt.executeUpdate();
            System.out.println("Deleted old table entries from buildings");
            Statement statement = con.createStatement();
            while ((line = reader.readLine()) != null) {
                String values[] = line.split(",");
                for(int i=0; i<values.length;i++)
                    values[i] = values[i].trim();
                String buildingsArray = "";
                for(int i = 3; i<values.length; i=i+2)
                    buildingsArray += values[i] + "," + values[i+1] + ", ";
                if(values.length>3)
                    buildingsArray += values[3] + "," + values[4];
                String insertB = "INSERT INTO buildings VALUES('" + values[0] + "','" + values[1] + "'," + values[2] + ", SDO_GEOMETRY(2003,  NULL, NULL, SDO_ELEM_INFO_ARRAY(1,1003,1), SDO_ORDINATE_ARRAY(" + buildingsArray + ")))";
                statement.executeUpdate(insertB);
                stmt.close();
            }
			System.out.println("Success!! Buildings values added");
            con.close();
	}
}