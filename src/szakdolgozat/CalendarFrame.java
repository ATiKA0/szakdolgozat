package szakdolgozat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.swing.JFrame;

import com.toedter.calendar.IDateEvaluator;
import com.toedter.calendar.JCalendar;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.UnfoldingReader;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.CalendarComponent;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CalendarFrame {

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
     * @wbp.parser.entryPoint
     */
    private void display() {
        JFrame f = new JFrame("Naptár");
        f.setResizable(true);
        f.setMinimumSize(new Dimension(500, 400));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        HighlightEvaluator evaluator = createEvaluator();

        JCalendar jc = new JCalendar();
        jc.getDayChooser().addDateEvaluator(evaluator);
        f.getContentPane().setLayout(new BorderLayout(0, 0));
        jc.setCalendar(jc.getCalendar());
        f.getContentPane().add(jc);
        
        JButton btnNewItem = new JButton("Új esemény");
        btnNewItem.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        	}
        });
        f.getContentPane().add(btnNewItem, BorderLayout.SOUTH);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }    
    
    public static Date convertToNewFormat(String dateStr) throws ParseException {
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
    	for (CalendarItem event : callist) {
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
    	      net.fortuna.ical4j.model.Calendar calendar = builder.build(ufrdr);
    	      List<CalendarComponent> events = calendar.getComponents(Component.VEVENT);
    	      ArrayList<CalendarItem> sus = new ArrayList<CalendarItem>();
    	      for (CalendarComponent event : events) {
    	    	String location = event.getProperties(Property.LOCATION).toString();
    	    	String summary = event.getProperties(Property.SUMMARY).toString();
    	    	String uid = event.getProperties(Property.UID).toString();
    	    	String dtstartf = removeText(event.getProperties(Property.DTSTART).toString());
    	    	String dtendf = removeText(event.getProperties(Property.DTEND).toString());

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

    public static void main(String[] args) {
        EventQueue.invokeLater(new CalendarFrame()::display);
    }
}