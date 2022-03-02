package szakdolgozat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.toedter.calendar.IDateEvaluator;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.UnfoldingReader;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.CalendarComponent;
import javax.swing.JButton;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CalendarFrame {
	
	CalendarItem returned = new CalendarItem(null ,null, null, null, null);
	ArrayList<CalendarItem> sus = new ArrayList<CalendarItem>();
		
		public void initialize() {
			System.setProperty("file.encoding","UTF-8");
			JFrame frame  = new JFrame();
			frame.setVisible(true);
			frame.setResizable(false);
			frame.setTitle("Új bejegyzés");
			frame.setBounds(100, 100, 360, 450);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWidths = new int[]{65, 212, 65, 0};
			gridBagLayout.rowHeights = new int[]{55, 36, 50, 36, 50, 36, 50, 36, 10, 37, 17, 0};
			gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
			gridBagLayout.rowWeights = new double[]{1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
			frame.getContentPane().setLayout(gridBagLayout);
			
			JLabel lblSummary = new JLabel("Leírás");
			GridBagConstraints gbc_lblSummary = new GridBagConstraints();
			gbc_lblSummary.anchor = GridBagConstraints.SOUTH;
			gbc_lblSummary.insets = new Insets(0, 0, 5, 5);
			gbc_lblSummary.gridx = 1;
			gbc_lblSummary.gridy = 0;
			frame.getContentPane().add(lblSummary, gbc_lblSummary);
			
			JTextField textSummary = new JTextField();
			GridBagConstraints gbc_textSummary = new GridBagConstraints();
			gbc_textSummary.fill = GridBagConstraints.BOTH;
			gbc_textSummary.insets = new Insets(0, 0, 5, 5);
			gbc_textSummary.gridx = 1;
			gbc_textSummary.gridy = 1;
			frame.getContentPane().add(textSummary, gbc_textSummary);
			textSummary.setColumns(10);
			
			JLabel lblLocation = new JLabel("Helyszín");
			GridBagConstraints gbc_lblLocation = new GridBagConstraints();
			gbc_lblLocation.anchor = GridBagConstraints.SOUTH;
			gbc_lblLocation.insets = new Insets(0, 0, 5, 5);
			gbc_lblLocation.gridx = 1;
			gbc_lblLocation.gridy = 2;
			frame.getContentPane().add(lblLocation, gbc_lblLocation);
			
			JTextField textLocation = new JTextField();
			textLocation.setColumns(10);
			GridBagConstraints gbc_textField = new GridBagConstraints();
			gbc_textField.fill = GridBagConstraints.BOTH;
			gbc_textField.insets = new Insets(0, 0, 5, 5);
			gbc_textField.gridx = 1;
			gbc_textField.gridy = 3;
			frame.getContentPane().add(textLocation, gbc_textField);
			
			JLabel lblDtstart = new JLabel("Kezdés ideje");
			GridBagConstraints gbc_lblDtstart = new GridBagConstraints();
			gbc_lblDtstart.anchor = GridBagConstraints.SOUTH;
			gbc_lblDtstart.insets = new Insets(0, 0, 5, 5);
			gbc_lblDtstart.gridx = 1;
			gbc_lblDtstart.gridy = 4;
			frame.getContentPane().add(lblDtstart, gbc_lblDtstart);
			
			JDateChooser dateDtstart = new JDateChooser();
			dateDtstart.setDateFormatString("yyyy.MM.dd. HH:mm");
			GridBagConstraints gbc_dateDtstart = new GridBagConstraints();
			gbc_dateDtstart.fill = GridBagConstraints.BOTH;
			gbc_dateDtstart.insets = new Insets(0, 0, 5, 5);
			gbc_dateDtstart.gridx = 1;
			gbc_dateDtstart.gridy = 5;
			frame.getContentPane().add(dateDtstart, gbc_dateDtstart);
			
			JLabel lblDtend = new JLabel("Befejezés");
			GridBagConstraints gbc_lblDtend = new GridBagConstraints();
			gbc_lblDtend.anchor = GridBagConstraints.SOUTH;
			gbc_lblDtend.insets = new Insets(0, 0, 5, 5);
			gbc_lblDtend.gridx = 1;
			gbc_lblDtend.gridy = 6;
			frame.getContentPane().add(lblDtend, gbc_lblDtend);
			
			JDateChooser dateDtend = new JDateChooser();
			dateDtend.setDateFormatString("yyyy.MM.dd. HH:mm");
			GridBagConstraints gbc_dateDtend = new GridBagConstraints();
			gbc_dateDtend.fill = GridBagConstraints.BOTH;
			gbc_dateDtend.insets = new Insets(0, 0, 5, 5);
			gbc_dateDtend.gridx = 1;
			gbc_dateDtend.gridy = 7;
			frame.getContentPane().add(dateDtend, gbc_dateDtend);
			
			JButton btnSend = new JButton("Hozzáadás");
			btnSend.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					btnSend.addMouseListener(new MouseAdapter() {
						public void mousePressed(MouseEvent e) {
							String summary = textSummary.getText();
							String location = textLocation.getText();
							Date dtstart = dateDtstart.getDate();
							Date dtend = dateDtend.getDate();
							
							if(summary == null || location == null || dtstart == null || dtend == null) {
								JOptionPane.showMessageDialog(null, "Kitöltetlen mező!");
								textSummary.setText(null); textLocation.setText(null); dateDtstart.setDate(null); dateDtend.setDate(null);
							}
							else {
								returned.setDtend(dtend);
								returned.setLocation(location);
								returned.setSummary(summary);
								returned.setDtstart(dtstart);
								returned.setUid(getRandomUid());
								frame.setVisible(false);
								display();
								}
						}
					});
				}
			});
			
			GridBagConstraints gbc_btnSend = new GridBagConstraints();
			gbc_btnSend.insets = new Insets(0, 0, 5, 5);
			gbc_btnSend.fill = GridBagConstraints.VERTICAL;
			gbc_btnSend.gridx = 1;
			gbc_btnSend.gridy = 9;
			frame.getContentPane().add(btnSend, gbc_btnSend);
		}
		
		private String getRandomUid() {
	        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
	        StringBuilder rand = new StringBuilder();
	        Random rnd = new Random();
	        while (rand.length() < 12) {
	            int index = (int) (rnd.nextFloat() * chars.length());
	            rand.append(chars.charAt(index));
	        }
	        String Str = rand.toString();
	        Str = Str +"-0000-0000-0000-000000000000";
	        return Str;

	    }
	


    private static class HighlightEvaluator implements IDateEvaluator {

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
     void display() {
    	System.setProperty("file.encoding","UTF-8");
    	Tray mini = new Tray();
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
        HighlightEvaluator evaluator = createEvaluator();
        
        JCalendar jc = new JCalendar();
        jc.getDayChooser().setDayBordersVisible(true);
        jc.getDayChooser().addDateEvaluator(evaluator);
        f.getContentPane().setLayout(new BorderLayout(0, 0));
        jc.setCalendar(jc.getCalendar());
        f.getContentPane().add(jc);
        
        JButton btnNewItem = new JButton("Új esemény");
        btnNewItem.addMouseListener(new MouseAdapter() {
        	public void mousePressed(MouseEvent e) {
        		f.dispose();
        		mini.removeTrayIcon();
        		initialize();
        	}
        });
        f.getContentPane().add(btnNewItem, BorderLayout.SOUTH);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
		mini.trayIcon(f);
		mini.notificationCalendar();
    }    
    
    private static Date convertToNewFormat(String dateStr) throws ParseException {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        sourceFormat.setTimeZone(utc);
        Date convertedDate = sourceFormat.parse(dateStr);
        return convertedDate;
    }
    
    public static String removeText(String cnv) {
    	StringBuffer cnvf = new StringBuffer(cnv);
    	cnvf.delete(0,cnvf.indexOf(":")+1);
    	cnvf.deleteCharAt(cnvf.length()-1);
		return cnvf.toString();
    }
    
    public HighlightEvaluator createEvaluator(){
    	HighlightEvaluator evaluator = new HighlightEvaluator();
    	ArrayList<CalendarItem> callist = new ArrayList<CalendarItem>(importCalendar());
    	Iterator<CalendarItem> lol = callist.iterator();
    	while (lol.hasNext()) {
    		CalendarItem event = lol.next();
    		Date sus = event.getDtstart();
    		int y = sus.getYear()+1900;
    		int m = sus.getMonth();
    		int d = sus.getDate();
    		evaluator.add(createDate(y, m, d));
    	}
		return evaluator;
    }
    
    public ArrayList<CalendarItem> importCalendar(){
    	final String ics = Frame_main.getNewestFile().toString();
    	System.setProperty("ical4j.unfolding.relaxed", "true");
    	System.setProperty("ical4j.parsing.relaxed", "true");
    	try {
    	      CalendarBuilder builder = new CalendarBuilder();
    	      final UnfoldingReader ufrdr =new UnfoldingReader(new FileReader(ics),true);
    	      if(returned.getDtend() != null)sus.add(returned);
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
    	      return sus;
    	} 
    	catch (Throwable t) {
    	      t.printStackTrace();
    	      return null;
    	}
    }

    private Date createDate(int y, int m, int d) {
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
         		
         		MenuItem open = new MenuItem("Megnyitás");
         		open.addActionListener(new ActionListener() {
         	        public void actionPerformed(ActionEvent e) {
         	            f.setVisible(true);
         	            f.setState(f.NORMAL);
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
	    	Date date = new Date(System.currentTimeMillis());
			Timer time = new Timer();
			while (iter.hasNext()) {
				CalendarItem ize = iter.next();
				Date dtstart = ize.getDtstart();
				TimerTask task = new TimerTask(){
					@Override
					public void run() {
					pubI.displayMessage("Hello, World", ize.getSummary()+System.lineSeparator()+ize.getLocation()+System.lineSeparator()+dtstart+System.lineSeparator()+ize.getDtend(), MessageType.INFO);
					}
				};
				if(dtstart.compareTo(date) > 0)time.schedule(task, dtstart);
			}
    	}
    	public void removeTrayIcon() {
    		pubT.remove(pubI);
    	}
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new CalendarFrame()::display);
    }
    
}
    
    