package APK;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.prefs.Preferences;

public class DatabaseConnection{
	
	private Properties properties;
	private Connection DBconnection;
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost:5555/serviciuldeambulante?autoReconnect=true&useSSL=false";
    Preferences preferences = Preferences.userNodeForPackage(DatabaseConnection.class);
    
    /*
	private void setCredentials(String user, String pass) {
	    preferences.put("database_user", user);
	    preferences.put("database_pass", pass);
	  }
	  */

    
    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", getUser());
            properties.setProperty("password", getPass());

        }
        return properties;
    }
    
    private String getUser() {
    	return preferences.get("database_user", null);
    }
    
    private String getPass() {
    	return preferences.get("database_pass", null);
    }
   
    public Connection connect() {
        if (DBconnection == null) {
	        try {
	        	Class.forName(driver);
				DBconnection = DriverManager.getConnection(url, getProperties());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
                System.out.println("Database Connection error!");
				e1.printStackTrace();
			}
        }
        return DBconnection;
    }
    
    public void disconnect() {
        if (DBconnection != null) {
            try {
            	DBconnection.close();
                DBconnection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
   /*
    public static void main(String[] args) {
    	DatabaseConnection db = new DatabaseConnection();
    	String user = "root";
    	String pass = "123459876";
    	db.setCredentials(user,pass);
    	System.out.println(db.getUser());
    	System.out.println(db.getPass());
     }
  */
    
}
