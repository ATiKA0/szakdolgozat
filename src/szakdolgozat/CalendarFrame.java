package szakdolgozat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.toedter.calendar.IDateEvaluator;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDayChooser;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.UnfoldingReader;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.CalendarComponent;
import java.awt.Toolkit;
import javax.swing.JLabel;
import java.awt.Font;

public class CalendarFrame {
/*
 *	Public variables 
 */
	static ArrayList<CalendarItem> calendarItemList = new ArrayList<CalendarItem>();	//This is the list where the calendar items stored from the ics file
	static private LocalDateTime chosenDate = LocalDateTime.now();	//This is the chosen date from the calendar view
	static HighlightEvaluator evaluator = new HighlightEvaluator();
	public Tray mini = new Tray();
	private final JButton btnNeptun = new JButton("Neptun szinkronizálás");
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
        public void remove() {
        	list.clear();
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
     * This is the method which runs first and set everything up when this frame is opened
     * @wbp.parser.entryPoint
     */
    void firstRun() {
    	Locale.setDefault(new Locale("hu", "HU"));	//Set the default locale for the dates.
    	System.setProperty("file.encoding","UTF-8");	//Set the file encoding to UTF-8
    	getFromSql();
    	createEvaluator();
    	display();
    }
    
    /**
     * This is the "main" method.
     * Creating the window view.
     */
     void display() {
        JFrame f = new JFrame("Naptár");
        f.setIconImage(Toolkit.getDefaultToolkit().getImage(CalendarFrame.class.getResource("/szakdolgozat/calendar.png")));
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
        JDayChooser dayChooser = jc.getDayChooser();
        dayChooser.setDayBordersVisible(true);
        btnNeptun.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mousePressed(MouseEvent e) {
        		calendarItemList.clear();
        		ImportFileFromLink imp = new ImportFileFromLink();
        		imp.run();
        		mini.removeTrayIcon();
        		f.dispose();
        	}
        });
        dayChooser.add(btnNeptun, BorderLayout.SOUTH);
        dayChooser.addDateEvaluator(evaluator);	//Add the evaluator for the coloring
        jc.setDate(Func.convertToDate(chosenDate));
        dayChooser.setAlwaysFireDayProperty(true);
        dayChooser.addPropertyChangeListener("day", new PropertyChangeListener() {	//Listener for the date choosing with mouse click
        	int c = 0;
        	@Override
            public void propertyChange(PropertyChangeEvent e) {
        		if(c == 0) {	// This is needed to select the current date
        			c++;
        		}else {
        			chosenDate = Func.convertToLocalDateTime(jc.getDate());	//This gets the clicked date in localdate format
        			WeekOrDay weekorday = new WeekOrDay();
                    weekorday.WeekOrDayinit();	//Start the asking window. It's ask what the user want, a week view or a day view
                    f.dispose();	//Destroy the month window
                    mini.removeTrayIcon();	//Remove the tray icon for this window
        		}
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
        		EventQueue.invokeLater(new AddItem()::initialize);	//If this new event button pressed, it opens a new window where the usern can add a new event
        	}
        });
        f.getContentPane().add(btnNewItem, BorderLayout.SOUTH);
        
        JLabel lblNewLabel = new JLabel("Napi/heti nézet a napokra való kattintással érhető el");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
        f.getContentPane().add(lblNewLabel, BorderLayout.NORTH);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
		mini.trayIcon(f);	//In the end of the window building the method add a tray icon to the system tray
    }    

    /**
     *	Here we creates the Highlight Evaluator. 
     */
    public void createEvaluator(){
    	ArrayList<CalendarItem> callist = new ArrayList<CalendarItem>(calendarItemList);	//Creating a local copy of the list contains the calendar items
    	Iterator<CalendarItem> iter = callist.iterator();	//For the local list we create an iterator
    	while (iter.hasNext()) {	//With a while going trough the whole list
    		CalendarItem event = iter.next();	//The next iterator element is claimed
    		LocalDateTime calendarItemDate = event.getdtStart();	//And from the element we create a date for the evaluator
    		int y = calendarItemDate.getYear();
    		int m = calendarItemDate.getMonthValue()-1;
    		int d = calendarItemDate.getDayOfMonth();
    		evaluator.add(createDate(y, m, d));
    		
    	}
    }
    /**
     * Here is the calendar importing method.
     * This method imports the ics file and parse it to CalendarItem
     */
    public void importCalendar(){
    	final String ics = Func.getNewestFile().toString();
    	System.setProperty("ical4j.unfolding.relaxed", "true");
    	System.setProperty("ical4j.parsing.relaxed", "true");
    	Connection connection = Func.connectToSql();
    	String loggedUser= Login_main.getUsrn().toLowerCase();
    	try {
		      CalendarBuilder builder = new CalendarBuilder();
		      final UnfoldingReader ufrdr =new UnfoldingReader(new FileReader(ics),true);
		      net.fortuna.ical4j.model.Calendar calendar = builder.build(ufrdr);	//Here is the calendar builder what build a calendar from the parsed ics
		      List<CalendarComponent> events = calendar.getComponents(Component.VEVENT);	//It's parsed to multiple VEVENT
		      Iterator<CalendarComponent> iter = events.iterator();	//This list of VEVENT gets an iterator
		      while (iter.hasNext()) {	//And with a while we go trough this list and parse it to a calendar component
		    	CalendarComponent comp = iter.next();
		    	String location = comp.getProperties(Property.LOCATION).toString();
		    	String summary = comp.getProperties(Property.SUMMARY).toString();
		    	String uid = comp.getProperties(Property.UID).toString();
		    	String dtstartf = Func.removeText(comp.getProperties(Property.DTSTART).toString());
		    	String dtendf = Func.removeText(comp.getProperties(Property.DTEND).toString());
		    	Func.insertIntoSql(connection, loggedUser, Func.removeText(uid), Func.removeText(summary), Func.removeText(location), Func.convertToNewFormat(dtstartf), Func.convertToNewFormat(dtendf));
    	      }
		      connection.close();
		      Func.getNewestFile().delete();
    	} 
    	catch (Throwable t) {
    	      t.printStackTrace();
    	}
    }
    
    /**
     * Get items from SQL database and store it locally
     */
    public void getFromSql() {
    	String loggedUser = Login_main.getUsrn().toLowerCase();
    	Connection connection = Func.connectToSql();
    	try {
    		String procedureCall = "{call getFromSql(?)}";
    		CallableStatement callableStatement = connection.prepareCall(procedureCall);
    		callableStatement.setString(1, loggedUser);
    		callableStatement.execute();
    		ResultSet result = callableStatement.getResultSet();
			while(result.next()) {
				String uid = result.getString(1);
				LocalDateTime dtstartf = result.getTimestamp(4).toLocalDateTime();
				LocalDateTime dtendf = result.getTimestamp(5).toLocalDateTime();
				String location = result.getString(3);
				String summary = result.getString(2);
				calendarItemList.add(new CalendarItem(uid, dtstartf, dtendf, location, summary));
			}
			connection.close();
		} catch (SQLException e) {
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

    public static void main(String[] args) {
        EventQueue.invokeLater(new CalendarFrame()::firstRun);
    }
    
}
    
    