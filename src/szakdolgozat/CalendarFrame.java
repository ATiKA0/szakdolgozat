package szakdolgozat;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import com.toedter.calendar.IDateEvaluator;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Calendar;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.beans.PropertyChangeEvent;

public class CalendarFrame {

    private static class HighlightEvaluator implements IDateEvaluator {

        private final List<Date> list = new ArrayList<>();

        public void add(Date date) {
            list.add(date);
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
            return Color.blue;
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
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        HighlightEvaluator evaluator = new HighlightEvaluator();
        evaluator.add(createDate(2022,01,14));
        evaluator.add(createDate(2022,00,15));
        JCalendar jc = new JCalendar();
        jc.addPropertyChangeListener("calendar", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
            	try {
					parse();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParserException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        jc.getDayChooser().addDateEvaluator(evaluator);
        jc.setCalendar(jc.getCalendar());
        f.getContentPane().add(jc);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
    public String readFile(String string, Charset charset) throws IOException {
        File file = new File(string);
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }
    
    public void parse() throws IOException, ParserException {
    	String content = readFile("C:\\Users\\gluck\\Downloads\\igen.ics", Charset.defaultCharset());
    	System.setProperty("ical4j.unfolding.relaxed", "true");
    	System.setProperty("ical4j.parsing.relaxed", "true");
    	StringReader sin = new StringReader(content);
    	CalendarBuilder builder = new CalendarBuilder();
    	net.fortuna.ical4j.model.Calendar calendar = builder.build(sin);
    	JOptionPane.showMessageDialog(null, calendar.getProperties());
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