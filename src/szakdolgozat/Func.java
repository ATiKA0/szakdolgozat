package szakdolgozat;

import java.io.File;
import java.io.FileFilter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public class Func {

    /**
     * This method parse the date/time format from the ics file to a LocalDateTime format
     * @param dateStr : Date string need to be formated
     * @return Formated LocalDateTIme
     * @throws ParseException
     */
    public static LocalDateTime convertToNewFormat(String dateStr) throws ParseException {
    	TimeZone utc = TimeZone.getTimeZone("UTC");
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        sourceFormat.setTimeZone(utc);
        Date parsedDate = sourceFormat.parse(dateStr);
        LocalDateTime returnedDate= convertToLocalDateTime(parsedDate);
        return returnedDate;
    }
    
    /**
     * 	This method converts a java.util.Date element to java.time.LocalDateTime
     * @param dateToConvert : java.util.Date to convert
     */
    public static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
          .atZone(ZoneId.systemDefault())
          .toLocalDateTime();
    }
    
    /**
     * This method converts a java.sql.Date element to java.time.LocalDateTime
     * @param dateToConvert : java.sql.Date to convert
     */
    public static LocalDateTime convertFromSqlDate(java.sql.Date dateToConvert) {
    	Instant instant = Instant.ofEpochMilli(dateToConvert.getTime());
    	return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
    
    /**
     * 	This method convert java.time.LocalDateTime element to java.util.Date
     */
    public static Date convertToDate(LocalDateTime dateToConvert) {
        return java.util.Date
          .from(dateToConvert.atZone(ZoneId.systemDefault())
          .toInstant());
    }
    
    /**
     *	This method set the printing format of the LocalDateTime.
     *	@param input : Element to format
     *	@param withTime : With the true/false  we can set the format is printed with the time or without.
     */
    public static String printFormatDate(LocalDateTime input, boolean withTime) {
    	DateTimeFormatter formatterD = DateTimeFormatter.ofPattern("EEEE, yyyy.MMMdd");
    	DateTimeFormatter formatterT = DateTimeFormatter.ofPattern("EEEE, yyyy.MMMdd HH:mm");
    	String output = null;
    	if(withTime) {
    		output = input.format(formatterT);
    	}
    	else {
    		output = input.format(formatterD);
    	}
    	return output;
    }
    
    /**
     * The removeText method removes the unnecessary text from the date in the ics file
     * @param input : Input string to truncate
     * @return String with text removed
     */
    public static String removeText(String input) {
    	StringBuffer cnvf = new StringBuffer(input);
    	cnvf.delete(0,cnvf.indexOf(":")+1);
    	cnvf.deleteCharAt(cnvf.length()-1);
		return cnvf.toString();
    }
    
    /**
     * This create date method is needed for the evaluator
     * @param y : Year
     * @param m : Month
     * @param d : Day
     */
    public static Date createDate(int y, int m, int d) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, y);
        c.set(Calendar.MONTH, m);
        c.set(Calendar.DAY_OF_MONTH, d);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return (c.getTime());
    }
    
    /**
     * Insert calendar element into the SQL database.
     * @param connection : Sql connection
     * @param user : Logged user
     */
    public static void insertIntoSql(Connection connection,String user,  String uid, String summary, String location, LocalDateTime dtstart, LocalDateTime dtend) {
    	try {
    		String procedureCall = "{call insertIntoSql(?, ?, ?, ?, ?, ?)}";
    		CallableStatement callableStatement = connection.prepareCall(procedureCall);
    		System.out.println(dtstart);
    		callableStatement.setString(1, uid);
    	    callableStatement.setString(2, summary);
    	    callableStatement.setString(3, location);
    	    callableStatement.setString(4, dtstart.toString());
    	    callableStatement.setString(5, dtend.toString());
    	    callableStatement.setString(6, user);
    	    callableStatement.execute();
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Create an SQL connection to the database
     * @return SQL connection
     */
    public static Connection connectToSql() {
    	Connection connection;
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/orarend",
					"root", "");
			return connection;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * The getNewestFile method returns the newest ics file from the working directory
     * Ics is the calendar file what we can export from the Neptun
     */
    	public static File getNewestFile() {
    	    File theNewestFile = null;
    	    File dir = new File(System.getProperty("user.dir"));
    	    FileFilter fileFilter = new WildcardFileFilter("*." + "ashx");
    	    File[] files = dir.listFiles(fileFilter);

    	    if (files.length > 0) {
    	        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
    	        theNewestFile = files[0];
    	    }
    	    return theNewestFile;
    	}
    

}
