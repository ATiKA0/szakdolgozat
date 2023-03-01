package szakdolgozat;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.toedter.calendar.IDateEvaluator;
import com.toedter.calendar.JCalendar;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.UnfoldingReader;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.CalendarComponent;

public class CalendarFrame {
/*
 *	Public variables 
 */
	static ArrayList<CalendarItem> calendarItemList = new ArrayList<CalendarItem>();	//This is the list where the calendar items stored from the ics file
	static private LocalDateTime chosenDate = LocalDateTime.now();	//This is the chosen date from the calendar view
	static HighlightEvaluator evaluator = new HighlightEvaluator();
	public Tray mini = new Tray();
/*
 *	This is the highlight evaluator. This is an imported class for the JCalendar.
 *	This is used for highlight the dates when there is an event.
 *	When there is an event on the day, the background changes to green. 
 */
    static class HighlightEvaluator implements IDateEvaluator {

        private final List<Date> list = new ArrayList<>();	//This is the list where the highlighted dates are stored

        public void add(Date date) {
            list.add(date);
        }
        public List<Date> getList() {
        	return list;
        }
        @Override
        public boolean isSpecial(Date date) {
            return list.contains(date);
        }

        @Override
        public Color getSpecialForegroundColor() {
            return Color.red.darker();
        }

        @Override
        public Color getSpecialBackroundColor() {
            return Color.green.brighter();
        }

        @Override
        public String getSpecialTooltip() {
            return "Highlighted event.";
        }

        @Override
        public boolean isInvalid(Date date) {
            return false;
        }

        @Override
        public Color getInvalidForegroundColor() {
            return null;
        }

        @Override
        public Color getInvalidBackroundColor() {
            return null;
        }

        @Override
        public String getInvalidTooltip() {
            return null;
        }

    }

    /**
     * @return 
     * @wbp.parser.entryPoint
     */
    /*
     * This is the method which runs first and set everything up when this frame is opened
     */
    void firstRun() {
    	Locale.setDefault(new Locale("hu", "HU"));	//Set the default locale for the dates.
    	System.setProperty("file.encoding","UTF-8");	//Set the file encoding to UTF-8
    	System.out.println(Charset.defaultCharset());
    	//importCalendar();
    	getFromSql();
    	createEvaluator();
    	display();
    }
    /*
     * This is the "main" method.
     * Creating the window view.
     */
     void display() {
        JFrame f = new JFrame("Naptár");
        f.addWindowListener(new WindowAdapter() {	//This window listener is for iconify to the system tray. 
        	@Override
        	public void windowIconified(WindowEvent e) {
        		f.setVisible(false);
        	}
        });
        f.setResizable(true);
        f.setMinimumSize(new Dimension(500, 400));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JCalendar jc = new JCalendar();	//Creating the JCalendar element
        jc.getDayChooser().setDayBordersVisible(true);
        jc.getDayChooser().addDateEvaluator(evaluator);	//Add the evaluator for the coloring
        jc.setDate(convertToDate(chosenDate));
        jc.getDayChooser().addPropertyChangeListener("day", new PropertyChangeListener() {	//Listener for the date choosing with mouse click
        	@Override
            public void propertyChange(PropertyChangeEvent e) {
                chosenDate = convertToLocalDateTime(jc.getDate());	//This gets the clicked date in localdate format
                WeekOrDay weekorday = new WeekOrDay();
                weekorday.WeekOrDayinit();	//Start the asking window. It's ask what the user want, a week view or a day view
                f.dispose();	//Destroy the month window
                mini.removeTrayIcon();	//Remove the tray icon for this window
                }
        });
        f.getContentPane().setLayout(new BorderLayout(0, 0));
        jc.setCalendar(jc.getCalendar());
        f.getContentPane().add(jc);
        
        JButton btnNewItem = new JButton("Új esemény");	//There is a new event button labeled "Új esemény".
        btnNewItem.addMouseListener(new MouseAdapter() {
        	public void mousePressed(MouseEvent e) {
        		f.dispose();
        		mini.removeTrayIcon();
        		AddItem additem = new AddItem();
        		additem.initialize();	//If this new event button pressed, it opens a new window where the usern can add a new event
        	}
        });
        f.getContentPane().add(btnNewItem, BorderLayout.SOUTH);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
		mini.trayIcon(f);	//In the end of the window building the method add a tray icon to the system tray
		mini.notificationCalendar();	//Start the notification backend process
    }    
    /*
     *	This method parse the date/time format from the ics file to a LocalDateTime format
     */
    private static LocalDateTime convertToNewFormat(String dateStr) throws ParseException {
    	TimeZone utc = TimeZone.getTimeZone("UTC");
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        sourceFormat.setTimeZone(utc);
        Date parsedDate = sourceFormat.parse(dateStr);
        LocalDateTime returnedDate= convertToLocalDateTime(parsedDate);
        return returnedDate;
    }
    /*
     * 	This method convert java.util.Date element to java.time.LocalDateTime
     */
    public static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
          .atZone(ZoneId.systemDefault())
          .toLocalDateTime();
    }
    /*
     * 	This method convert java.time.LocalDateTime element to java.util.Date
     */
    public static Date convertToDate(LocalDateTime dateToConvert) {
        return java.util.Date
          .from(dateToConvert.atZone(ZoneId.systemDefault())
          .toInstant());
    }
    /*
     *	This method set the printing format of the LocalDateTime.
     *	With the true/false at the end we can set the format is printed with the time or without. 
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
    /*
     *	The removeText method removes the unnecessary text from the date in the ics file 
     */
    public static String removeText(String cnv) {
    	StringBuffer cnvf = new StringBuffer(cnv);
    	cnvf.delete(0,cnvf.indexOf(":")+1);
    	cnvf.deleteCharAt(cnvf.length()-1);
		return cnvf.toString();
    }
    /*
     *	Here we creates the Highlight Evaluator. 
     */
    public void createEvaluator(){
    	ArrayList<CalendarItem> callist = new ArrayList<CalendarItem>(calendarItemList);	//Creating a local copy of the list contains the calendar items
    	Iterator<CalendarItem> iter = callist.iterator();	//For the local list we create an iterator
    	while (iter.hasNext()) {	//With a while going trough the whole list
    		CalendarItem event = iter.next();	//The next iterator element is claimed
    		LocalDateTime calendarItemList = event.getdtStart();	//And from the element we create a date for the evaluator
    		int y = calendarItemList.getYear();
    		int m = calendarItemList.getMonthValue()-1;
    		int d = calendarItemList.getDayOfMonth();
    		evaluator.add(createDate(y, m, d));
    	}
    }
    /*
     * Here is the calendar importing method. This is the first thing in this view.
     * This method imports the ics file and parse it to CalendarItem
     */
    public void importCalendar(){
    	final String ics = Frame_main.getNewestFile().toString();
    	System.setProperty("ical4j.unfolding.relaxed", "true");
    	System.setProperty("ical4j.parsing.relaxed", "true");
    	Connection connection;
    	String loggedUser= Login_main.getUsrn().toLowerCase();
    	try {
    		  Class.forName("com.mysql.cj.jdbc.Driver");
			  connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/orarend",
	                "root", "");
			  System.out.println("Database connected!");
		      CalendarBuilder builder = new CalendarBuilder();
		      final UnfoldingReader ufrdr =new UnfoldingReader(new FileReader(ics),true);
		      net.fortuna.ical4j.model.Calendar calendar = builder.build(ufrdr);	//Here is the calendar builder what build a calendar from the parsed ics
		      List<CalendarComponent> events = calendar.getComponents(Component.VEVENT);	//It's parsed to multiple VEVENT
		      Iterator<CalendarComponent> iter = events.iterator();	//This list of VEVENT gets an iterator
		      while (iter.hasNext()) {	//And with a while we go trough this list and parse it to a calendar component
		    	CalendarComponent ize = iter.next();
		    	String location = ize.getProperties(Property.LOCATION).toString();
		    	String summary = ize.getProperties(Property.SUMMARY).toString();
		    	String uid = ize.getProperties(Property.UID).toString();
		    	String dtstartf = removeText(ize.getProperties(Property.DTSTART).toString());
		    	String dtendf = removeText(ize.getProperties(Property.DTEND).toString());
		    	insert_into_sql(connection, loggedUser, removeText(uid), summary, location, dtstartf, dtendf);
		    	//At the end we create a CalendarItem and add it to the public list
		  		//calendarItemList.add(new CalendarItem(removeText(uid), convertToNewFormat(dtstartf), convertToNewFormat(dtendf), removeText(location), removeText(summary)));
    	      }
		      connection.close();
    	} 
    	catch (Throwable t) {
    	      t.printStackTrace();
    	}
    }
    
    public void insert_into_sql(Connection connection,String user,  String uid, String summary, String location, String dtstart, String dtend) {
    	try {
			String quary1 = "INSERT INTO "+user+" (`uid`, `summary`, `location`, `startdate`, `enddate`) VALUES ('"+uid+"', '"+summary+"', '"+location+"', '"+dtstart+"', '"+dtend+"');";
			Statement statement = connection.createStatement();
			statement.addBatch(quary1);
			statement.executeBatch();
			statement.close();
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public void getFromSql() {
    	String loggedUser = Login_main.getUsrn().toLowerCase();
    	Connection connection;
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/orarend",
	                "root", "");
			System.out.println("Database connected!");
			String quary= "SELECT * FROM "+loggedUser+"";
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(quary);
			while(result.next()) {
				String uid = result.getString(1);
				String dtstartf = result.getString(4);
				String dtendf = result.getString(5);
				String location = result.getString(3);
				String summary = result.getString(2);
				calendarItemList.add(new CalendarItem(removeText(uid), convertToNewFormat(dtstartf), convertToNewFormat(dtendf), removeText(location), removeText(summary)));
				result.next();
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
    }
    
    /*
     * This create date method is needed for the evaluator
     */
    public Date createDate(int y, int m, int d) {
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
    
    public LocalDateTime getchosenDate() {
    	return chosenDate;
    }
    /*
     *	Here starts the class for the tray icon 
     */
    class Tray{
    	static TrayIcon pubI;	//The tray icon
    	static SystemTray pubT;	//The system tray
    	/*
    	 * This is the method for activating the tray icon
    	 */
    	 public void trayIcon(JFrame f) {
    		 	if (!SystemTray.isSupported()) {	//Check the system tray for trayicon support
    		 		System.out.println("SystemTray is not supported");
    	            return;
    	        }
    		 	final PopupMenu popup = new PopupMenu();
    		 	final SystemTray tray = SystemTray.getSystemTray();
    		 	Image icon = Toolkit.getDefaultToolkit().createImage(".//src//szakdolgozat//calendar.png");
    		 	TrayIcon trayIcon = new TrayIcon(icon, "Órarend");
    		 	pubI = trayIcon;
    		 	pubT=tray;
    		 	trayIcon.setImageAutoSize(true);
         		trayIcon.setToolTip("Órarend");
         		
         		trayIcon.addActionListener(new ActionListener() {
         			public void actionPerformed(ActionEvent evt) {
         				f.setVisible(true);
         	            f.setState(Frame.NORMAL);
         			};
         		});
         		
         		MenuItem open = new MenuItem("Megnyitás");
         		open.addActionListener(new ActionListener() {
         	        public void actionPerformed(ActionEvent e) {
         	            f.setVisible(true);
         	            f.setState(Frame.NORMAL);
         	        }
         	    });
         		MenuItem close = new MenuItem("Bezárás");
         		close.addActionListener(new ActionListener() {
         	        public void actionPerformed(ActionEvent e) {
         	            System.exit(0);
         	        }
         	    });
         		popup.add(open);
         		popup.add(close);
         		trayIcon.setPopupMenu(popup);
         		
         		try {
                    tray.add(trayIcon);
                } catch (AWTException e) {
                    System.out.println("TrayIcon could not be added.");
                }

    	 }

    	public void notificationCalendar() {
	    	Iterator<CalendarItem> iter = calendarItemList.iterator();
	    	LocalDateTime date = LocalDateTime.now();
			Timer time = new Timer();
			while (iter.hasNext()) {
				CalendarItem ize = iter.next();
				LocalDateTime dtstart = ize.getdtStart();
				TimerTask task = new TimerTask(){
					@Override
					public void run() {
					pubI.displayMessage("Esemény kezdete", "Leírás: "+ize.getsummary()+System.lineSeparator()+"Helyszín: "+ize.getlocation()+System.lineSeparator()+"Kezdés ideje: "+printFormatDate(dtstart, true)+System.lineSeparator()+"Vége: "+printFormatDate(ize.getdtEnd(),true), MessageType.INFO);
					}
				};
				if(dtstart.compareTo(date) > 0)time.schedule(task, convertToDate(dtstart));
			}
    	}
    	public void removeTrayIcon() {
    		pubT.remove(pubI);
    	}
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new CalendarFrame()::firstRun);
    }
    
}
    
    