import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class STARTVERSION2 {

	public static Logger logger = Logger.getLogger(STARTVERSION2.class);
	
	public static ArrayList<String> tableArray = new ArrayList<String>();
	
	public static String Databaseipport;
	
	public static String Databasename;
	
	public static String Databaseacc;
	
	public static String Databasepwd;
	
	public static void main(String[] args) {
		 
//		 log4j設置
		 Date DateUtil = new Date();
		
		 SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		 String date = bartDateFormat.format(DateUtil);
		
		 PropertyConfigurator.configure(STARTVERSION2.class.getResource("log4j.properties"));
		
		 Appender appender =
		 LogManager.getLoggerRepository().getRootLogger().getAppender("FILE");
		
		 if(appender instanceof FileAppender)
		 {
			 FileAppender fileAppender = (FileAppender)appender;
			 fileAppender.setFile( "E:/SQLTYPELOG/Commandlog2."+date+ ".log");
			 fileAppender.activateOptions();
		 }
//		 log4j設置結束
		 
		 //character_maximum_length  二進位或字元資料型別的長度上限 (字元數)。 否則，傳回 NULL。
		 //character_octet_length    二進位或字元資料型別的最大長度 (以位元組為單位)。 否則，傳回 NULL。
		 //numeric_precision		  如果 data_type 標識一個數值類型，這個字串包含（聲明了或者蘊涵著）這個字串的資料類型的精度。 精度資料表示有效小數位的長度。它可以用十進制或者二進制來資料表示，這一點在 numeric_precision_radix 字串裡聲明。 對於其它資料類型，這個字串是空。
		 //numeric_precision_radix   這個字串標識字串 numeric_precision 和 numeric_scale 裡的資料是多少進制的。值要麼是 2 要麼是 10。對於所有其它資料類型， 這個字串是空。
		 //numeric_scale			  如果 data_type 標識一個精確的數值類型， 那麼這個字串包含（聲明的或者隱含的）這個字串上這個類型的數量級。 數量級資料表明小數點右邊的有效小數位的數目。它可以用十進制（10為基）或者二進制（二為基）來資料表示，正如 numeric_precision_radix 裡聲明的那樣。對於所有其它資料類型，這個字串是空。

		 System.out.println(args.length);

		 //已下為由外部設定所要監聽的SQL參數資料(IP DATABSENAME ACC PWD)
		 //如果輸入的參數小於四個
		 if(args.length<4)
		 {
			 System.exit(0);
		 }else
		 {
			 Databaseipport = args[0];
			 Databasename = args[1];
			 
			 try {
				Databaseacc = (new String(Base64.getDecoder().decode(args[2]), "UTF-8"));
				Databasepwd = (new String(Base64.getDecoder().decode(args[3]), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
		 }
		 
		 try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
				 
				 String URL = "jdbc:sqlserver://"+Databaseipport+";databaseName="+Databasename;
				 String USER = Databaseacc;
				 String PASS = Databasepwd;
				 String Table = "";
				 Connection con = DriverManager.getConnection(URL, USER, PASS);
				
				
//				String connectionUrl = "jdbc:sqlserver://192.168.222.181;" +  
//		        		   "databaseName=IFRSRPDB;user=SA;password=JHadmin123;";  
//				
//		        Connection con = DriverManager.getConnection(connectionUrl); 
		        
		        Statement st = con.createStatement();		 
				
		        ResultSet rs = st.executeQuery
		        		("SELECT TABLE_NAME AS TABLE_NAME,"
		        				+"COLUMN_NAME AS COLUMN_NAME,"
		        				+"IS_NULLABLE AS IS_NULLABLE,"
		        				+"DATA_TYPE AS DATA_TYPE,"
		        				+"CHARACTER_MAXIMUM_LENGTH,"
		        				+"CHARACTER_OCTET_LENGTH,"
		        				+"NUMERIC_PRECISION,"
		        				+"NUMERIC_SCALE"
		        				+" FROM INFORMATION_SCHEMA.COLUMNS");
		        while (rs.next()) {
		    
		        	//System.out.println("rs.getString(1)="+rs.getString(1));
		        	if(!Table.equals(rs.getString(1)))
		        	{
		        		System.out.println("---------------------------------------------------"+rs.getString(1)+"---------------------------------------------------");
		        		logger.info("---------------------------------------------------"+rs.getString(1)+"---------------------------------------------------");
			        	Table=rs.getString(1);
		        	}
		        		String result = (//"資料表名稱:"+rs.getString(1)+","
        					    "欄位名稱:"+rs.getString(2)+","
        			            +"可空:"+rs.getString(3)+","
        					    +"欄位形態:"+rs.getString(4)+","
        			            +"字元數長度上限:"+rs.getString(5)+","
        			            +"位元組最大長度:"+rs.getString(6)+","
        			            +"資料的精準度(大):"+rs.getString(7)+","
        			            +"資料的精準度(小):"+rs.getString(8));
		        		System.out.println(result);
		
		           	logger.info(result);
		        }
		        
		 }catch(Exception ex)
		 {
			 ex.printStackTrace();
		 }
	
				 
	}

}
