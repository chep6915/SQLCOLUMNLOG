import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class START {

	public static Logger logger = Logger.getLogger(START.class);
	
	public static ArrayList<String> tableArray = new ArrayList<String>();
	
	public static void main(String[] args) {
		
		
//		 log4j設置
				 Date DateUtil = new Date();
				
				 SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				
				 String date = bartDateFormat.format(DateUtil);
				
				 PropertyConfigurator.configure(START.class.getResource("log4j.properties"));
				
				 Appender appender =
				 LogManager.getLoggerRepository().getRootLogger().getAppender("FILE");
				
				 if(appender instanceof FileAppender)
				 {
				 FileAppender fileAppender = (FileAppender)appender;
				 fileAppender.setFile( "E:/SQLTYPELOG/testCommandlog."+date+ ".log");
				 fileAppender.activateOptions();
				 }
//		log4j設置結束
		
				 
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
			String connectionUrl = "jdbc:sqlserver://192.168.222.181;" +  
	        		   "databaseName=IFRSRPDB;user=SA;password=JHadmin123;";  
			
	        Connection con = DriverManager.getConnection(connectionUrl); 
	        
	        Statement st = con.createStatement();
//            st.executeUpdate("select * from test");
//          st.exectueUpdate("Update Customer set Customer_ID = 'HIHIHI' where Customer_ID =1");
	        DatabaseMetaData md = con.getMetaData();
	        ResultSet rss = md.getTables(null, null, "%", null);
	        

	        while (rss.next()) {
	          System.out.println(rss.getString(2)+","+rss.getString(3)+","+rss.getString(4));
	          if(rss.getString(2).equals("dbo"))
	          {
	        	  tableArray.add(rss.getString(3));
	          }
	        }
	        
	        
	        String sql = "select TOP 1 * from ";
	        for(String table:tableArray)
	        {
	        	System.out.println(sql+"["+table+"]");
	        	String start ="----------------------Start--"+table+"--------------------";
	        	logger.info(start);
	        	System.out.println(start);
	        	try {
	        		ResultSet rs = st.executeQuery(sql+"["+table+"]");
	        		//ResultSet rs = st.executeQuery("sp_help"+"["+table+"]");

		        	ResultSetMetaData rsmd = rs.getMetaData();
		        	
		        	while(rs.next())
		        	{
		        		for (int idx = 1; idx <= rsmd.getColumnCount(); idx++) {
		        			String result = "columnLabel="+rsmd.getColumnLabel(idx)+",,"+"columntype="+rsmd.getColumnTypeName(idx);
		        			System.out.println(result);
		        			logger.info(result);
		                    //DataMap.put(rsmd.getColumnName(idx), rs.getString(idx));
		                  }
		        	}
	        		
				} catch (Exception e) {
					
				}
	        	String end ="----------------------Over--"+table+"--------------------";
	        	System.out.println(end);
	        	logger.info(end);
	        	
	        }
	        
	        
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

}
