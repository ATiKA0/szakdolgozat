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

	static ArrayList<CalendarItem> sus = new ArrayList<CalendarItem>();
	static private LocalDateTime choosenDate;
	static HighlightEvaluator evaluator = new HighlightEvaluator();
	public Tray mini = new Tray();

    static class HighlightEvaluator implements IDateEvaluator {

        private final List<Date> list = new ArrayList<>();

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
    void firstRun() {
    	importCalendar();
    	createEvaluator();
    	display();
    }
     void display() {
    	Locale.setDefault(new Locale("hu", "HU"));
    	System.setProperty("file.encoding","UTF-8");
        JFrame f = new JFrame("Naptár");
        f.addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowIconified(WindowEvent e) {
        		f.setVisible(false);
        	}
        });
        f.setResizable(true);
        f.setMinimumSize(new Dimension(500, 400));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JCalendar jc = new JCalendar();
        jc.getDayChooser().setDayBordersVisible(true);
        jc.getDayChooser().addDateEvaluator(evaluator);
        jc.getDayChooser().addPropertyChangeListener("day", new PropertyChangeListener() {
        	@Override
            public void propertyChange(PropertyChangeEvent e) {
                choosenDate = convertToLocalDateTime(jc.getDate());
                WeekOrDay weekorday = new WeekOrDay();
                weekorday.WeekOrDayinit();
                f.dispose();
                mini.removeTrayIcon();
                }
        });
        f.getContentPane().setLayout(new BorderLayout(0, 0));
        jc.setCalendar(jc.getCalendar());
        f.getContentPane().add(jc);
        
        JButton btnNewItem = new JButton("Új esemény");
        btnNewItem.addMouseListener(new MouseAdapter() {
        	public void mousePressed(MouseEvent e) {
        		f.dispose();
        		mini.removeTrayIcon();
        		AddItem additem = new AddItem();
        		additem.initialize();
        	}
        });
        f.getContentPane().add(btnNewItem, BorderLayout.SOUTH);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
		mini.trayIcon(f);
		mini.notificationCalendar();
    }    
    
    private static LocalDateTime convertToNewFormat(String dateStr) throws ParseException {
    	TimeZone utc = TimeZone.getTimeZone("UTC");
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        sourceFormat.setTimeZone(utc);
        Date parsedDate = sourceFormat.parse(dateStr);
        LocalDateTime returnedDate= convertToLocalDateTime(parsedDate);
        return returnedDate;
    }
    
    public static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
          .atZone(ZoneId.systemDefault())
          .toLocalDateTime();
    }
    
    public static Date convertToDate(LocalDateTime dateToConvert) {
        return java.util.Date
          .from(dateToConvert.atZone(ZoneId.systemDefault())
          .toInstant());
    }
    
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
    
    public static String removeText(String cnv) {
    	StringBuffer cnvf = new StringBuffer(cnv);
    	cnvf.delete(0,cnvf.indexOf(":")+1);
    	cnvf.deleteCharAt(cnvf.length()-1);
		return cnvf.toString();
    }
    
    public void createEvaluator(){
    	ArrayList<CalendarItem> callist = new ArrayList<CalendarItem>(sus);
    	Iterator<CalendarItem> lol = callist.iterator();
    	while (lol.hasNext()) {
    		CalendarItem event = lol.next();
    		LocalDateTime sus = event.getDtstart();
    		int y = sus.getYear();
    		int m = sus.getMonthValue()-1;
    		int d = sus.getDayOfMonth();
    		evaluator.add(createDate(y, m, d));
    	}
    }
    
    public void importCalendar(){
    	final String ics = Frame_main.getNewestFile().toString();
    	System.setProperty("ical4j.unfolding.relaxed", "true");
    	System.setProperty("ical4j.parsing.relaxed", "true");
    	try {
    	      CalendarBuilder builder = new CalendarBuilder();
    	      final UnfoldingReader ufrdr =new UnfoldingReader(new FileReader(ics),true);
    	      net.fortuna.ical4j.model.Calendar calendar = builder.build(ufrdr);
    	      List<CalendarComponent> events = calendar.getComponents(Component.VEVENT);
    	      Iterator<CalendarComponent> iter = events.iterator(); 
    	      while (iter.hasNext()) {
    	    	CalendarComponent ize = iter.next();
    	    	String location = ize.getProperties(Property.LOCATION).toString();
    	    	String summary = ize.getProperties(Property.SUMMARY).toString();
    	    	String uid = ize.getProperties(Property.UID).toString();
    	    	String dtstartf = removeText(ize.getProperties(Property.DTSTART).toString());
    	    	String dtendf = removeText(ize.getProperties(Property.DTEND).toString());
    	    	
    	  		sus.add(new CalendarItem(removeText(uid), convertToNewFormat(dtstartf), convertToNewFormat(dtendf), removeText(location), removeText(summary)));
    	      }
    	} 
    	catch (Throwable t) {
    	      t.printStackTrace();
    	}
    }

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
    
    public LocalDateTime getChoosenDate() {
    	return choosenDate;
    }
    
    class Tray{
    	static TrayIcon pubI;
    	static SystemTray pubT;
    	 public void trayIcon(JFrame f) {
    		 	if (!SystemTray.isSupported()) {
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
	    	Iterator<CalendarItem> iter = sus.iterator();
	    	LocalDateTime date = LocalDateTime.now();
			Timer time = new Timer();
			while (iter.hasNext()) {
				CalendarItem ize = iter.next();
				LocalDateTime dtstart = ize.getDtstart();
				TimerTask task = new TimerTask(){
					@Override
					public void run() {
					pubI.displayMessage("Esemény kezdete", "Leírás: "+ize.getSummary()+System.lineSeparator()+"Helyszín: "+ize.getLocation()+System.lineSeparator()+"Kezdés ideje: "+printFormatDate(dtstart, true)+System.lineSeparator()+"Vége: "+printFormatDate(ize.getDtend(),true), MessageType.INFO);
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
    
    