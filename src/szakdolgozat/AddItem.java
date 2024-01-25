package szakdolgozat;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.commons.lang3.time.DateUtils;

import com.toedter.calendar.JDateChooser;
import java.awt.Toolkit;

/**
 * Add a new calendar entry
 * @author gluck
 */
public class AddItem extends CalendarFrame {
	private JFrame frame;
	private JTextField textSummary, textLocation;
	private JDateChooser dateDtstart, dateDtend;

	/**
	 * @wbp.parser.entryPoint
	 */
	
	/*
	 * In this window we can add a new entry to the calendar.
	 */
	public void initialize() {
		System.setProperty("file.encoding","UTF-8");
		frame  = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(AddItem.class.getResource("/szakdolgozat/calendar.png")));
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				mini.removeTrayIcon();
				display();
			}
		});
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setTitle("Új esemény");
		frame.setBounds(100, 100, 360, 450);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
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
		
		textSummary = new JTextField();
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
		
		textLocation = new JTextField();
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
		
		dateDtstart = new JDateChooser();
		dateDtstart.setDate(new Date());
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
		
		dateDtend = new JDateChooser();
		Date date = DateUtils.addMinutes(new Date(), 1);
		dateDtend.setDate(date);
		dateDtend.setDateFormatString("yyyy.MM.dd. HH:mm");
		GridBagConstraints gbc_dateDtend = new GridBagConstraints();
		gbc_dateDtend.fill = GridBagConstraints.BOTH;
		gbc_dateDtend.insets = new Insets(0, 0, 5, 5);
		gbc_dateDtend.gridx = 1;
		gbc_dateDtend.gridy = 7;
		frame.getContentPane().add(dateDtend, gbc_dateDtend);
		
		JButton btnSend = new JButton("Hozzáadás");
		btnSend.addActionListener(new ActionListener() {	//This is the listener for the add new lesson button.
			public void actionPerformed(ActionEvent e) {
				// Gets the summary and the location of the lesson.
				String summary = textSummary.getText();
				String location = textLocation.getText();
				LocalDateTime dtstart = null; 
				LocalDateTime dtend = null;
				try {	//This try-catch block is checks if the date format is correct by try to convert to LocalDate format.
				dtstart = Func.convertToLocalDateTime(dateDtstart.getDate());
				dtend = Func.convertToLocalDateTime(dateDtend.getDate());
				}
				catch(Exception f){
					JOptionPane.showMessageDialog(null, "Hibás dátum!");
					textSummary.setText(null); textLocation.setText(null); dateDtstart.setDate(new Date()); dateDtend.setDate(new Date());
				}
				if(summary.isEmpty()||location.isEmpty()||dateDtstart.equals(null)||dtend.equals(null)) { //Check for empty boxes.
					JOptionPane.showMessageDialog(null, "Kitöltetlen mező!");
					textSummary.setText(null); textLocation.setText(null); dateDtstart.setDate(new Date()); dateDtend.setDate(new Date());
				}
				else if(dtstart.isAfter(dtend)) {	//Checks the date of start is not after the date of end of lesson.
					JOptionPane.showMessageDialog(null, "Hibás dátum!");
					textSummary.setText(null); textLocation.setText(null); dateDtstart.setDate(new Date()); dateDtend.setDate(new Date());
				}
				else {	//If everything is correct adds to the evaluator list and closes the window.
					addNewItem(summary, location, dtstart, dtend);
				}
			}
		});
		
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 5, 5);
		gbc_btnSend.fill = GridBagConstraints.VERTICAL;
		gbc_btnSend.gridx = 1;
		gbc_btnSend.gridy = 9;
		frame.getContentPane().add(btnSend, gbc_btnSend);
		mini.trayIcon(frame);
	}
	/**
	 *	This function generates a random UID for an event and returns as a string.
	 */
	private String getRandomSsid() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder randstart = new StringBuilder();
        StringBuilder randend = new StringBuilder();
        Random rnd = new Random();
        while (randstart.length() < 11) {
            int index = (int) (rnd.nextFloat() * chars.length());
            randstart.append(chars.charAt(index));
        }
        while (randend.length() < 11) {
            int index = (int) (rnd.nextFloat() * chars.length());
            randend.append(chars.charAt(index));
        }
        String Str = randstart.toString();
        String Stre = randend.toString();
        Str = Str +"-0000-0000-0000-"+ Stre;
        return Str;

    }
	
	private void addNewItem(String summary, String location, LocalDateTime dtstart, LocalDateTime dtend) {
		CalendarItem returned = new CalendarItem(getRandomSsid(), dtstart, dtend, location, summary);
		frame.setVisible(false);
		calendarItemList.add(returned);
		Date createdDate = createDate(returned.getdtStart().getYear(),returned.getdtStart().getMonthValue()-1,returned.getdtStart().getDayOfMonth());
		evaluator.add(createdDate);
		try {
			Connection connect = Func.connectToSql();
			Func.insertIntoSql(connect, Login_main.getUsrn().toLowerCase(), returned.getuid(), summary, location, dtstart, dtend);
			connect.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		mini.removeTrayIcon();
		display();
	}

}
