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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.commons.lang3.time.DateUtils;

import com.toedter.calendar.JDateChooser;
import java.awt.Toolkit;

public class ModifyEvent extends CalendarDayView {
	/*
	 * In this window we can edit a calendar event.
	 */
	private CalendarItem item; 
	/**
	 * @wbp.parser.entryPoint
	 */
	public void initialize(String ssidToEdit) {
		System.setProperty("file.encoding","UTF-8");
		int index = searchForItem(ssidToEdit);
		JFrame frmMdosts  = new JFrame();
		frmMdosts.setIconImage(Toolkit.getDefaultToolkit().getImage(ModifyEvent.class.getResource("/szakdolgozat/calendar.png")));
		frmMdosts.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {	//When the window closing returns to the list view
				mini.removeTrayIcon();
				listView();
			}
		});
		frmMdosts.setVisible(true);
		frmMdosts.setResizable(false);
		frmMdosts.setTitle("Módosítás");
		frmMdosts.setBounds(100, 100, 360, 450);
		frmMdosts.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{65, 212, 65, 0};
		gridBagLayout.rowHeights = new int[]{55, 36, 50, 36, 50, 36, 50, 36, 10, 37, 17, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		frmMdosts.getContentPane().setLayout(gridBagLayout);
		
		JLabel lblSummary = new JLabel("Leírás");
		GridBagConstraints gbc_lblSummary = new GridBagConstraints();
		gbc_lblSummary.anchor = GridBagConstraints.SOUTH;
		gbc_lblSummary.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummary.gridx = 1;
		gbc_lblSummary.gridy = 0;
		frmMdosts.getContentPane().add(lblSummary, gbc_lblSummary);
		
		JTextField textSummary = new JTextField();
		textSummary.setText(item.getsummary());
		GridBagConstraints gbc_textSummary = new GridBagConstraints();
		gbc_textSummary.fill = GridBagConstraints.BOTH;
		gbc_textSummary.insets = new Insets(0, 0, 5, 5);
		gbc_textSummary.gridx = 1;
		gbc_textSummary.gridy = 1;
		frmMdosts.getContentPane().add(textSummary, gbc_textSummary);
		textSummary.setColumns(10);
		
		JLabel lblLocation = new JLabel("Helyszín");
		GridBagConstraints gbc_lblLocation = new GridBagConstraints();
		gbc_lblLocation.anchor = GridBagConstraints.SOUTH;
		gbc_lblLocation.insets = new Insets(0, 0, 5, 5);
		gbc_lblLocation.gridx = 1;
		gbc_lblLocation.gridy = 2;
		frmMdosts.getContentPane().add(lblLocation, gbc_lblLocation);
		
		JTextField textLocation = new JTextField();
		textLocation.setText(item.getlocation());
		textLocation.setColumns(10);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 3;
		frmMdosts.getContentPane().add(textLocation, gbc_textField);
		
		JLabel lblDtstart = new JLabel("Kezdés ideje");
		GridBagConstraints gbc_lblDtstart = new GridBagConstraints();
		gbc_lblDtstart.anchor = GridBagConstraints.SOUTH;
		gbc_lblDtstart.insets = new Insets(0, 0, 5, 5);
		gbc_lblDtstart.gridx = 1;
		gbc_lblDtstart.gridy = 4;
		frmMdosts.getContentPane().add(lblDtstart, gbc_lblDtstart);
		
		JDateChooser dateDtstart = new JDateChooser();
		dateDtstart.setDate(Func.convertToDate(item.getdtStart()));
		dateDtstart.setDateFormatString("yyyy.MM.dd. HH:mm");
		GridBagConstraints gbc_dateDtstart = new GridBagConstraints();
		gbc_dateDtstart.fill = GridBagConstraints.BOTH;
		gbc_dateDtstart.insets = new Insets(0, 0, 5, 5);
		gbc_dateDtstart.gridx = 1;
		gbc_dateDtstart.gridy = 5;
		frmMdosts.getContentPane().add(dateDtstart, gbc_dateDtstart);
		
		JLabel lblDtend = new JLabel("Befejezés");
		GridBagConstraints gbc_lblDtend = new GridBagConstraints();
		gbc_lblDtend.anchor = GridBagConstraints.SOUTH;
		gbc_lblDtend.insets = new Insets(0, 0, 5, 5);
		gbc_lblDtend.gridx = 1;
		gbc_lblDtend.gridy = 6;
		frmMdosts.getContentPane().add(lblDtend, gbc_lblDtend);
		
		JDateChooser dateDtend = new JDateChooser();
		dateDtend.setDate(Func.convertToDate(item.getdtEnd()));
		dateDtend.setDateFormatString("yyyy.MM.dd. HH:mm");
		GridBagConstraints gbc_dateDtend = new GridBagConstraints();
		gbc_dateDtend.fill = GridBagConstraints.BOTH;
		gbc_dateDtend.insets = new Insets(0, 0, 5, 5);
		gbc_dateDtend.gridx = 1;
		gbc_dateDtend.gridy = 7;
		frmMdosts.getContentPane().add(dateDtend, gbc_dateDtend);
		
		JButton btnSend = new JButton("Módosítás");
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
				}
				if(summary.isEmpty()||location.isEmpty()||dateDtstart.equals(null)||dtend.equals(null)) { //Check for empty boxes.
					JOptionPane.showMessageDialog(null, "Kitöltetlen mező!");
				}
				else if(dtstart.isAfter(dtend)) {	//Checks the date of start is not after the date of end of lesson.
					JOptionPane.showMessageDialog(null, "Hibás dátum!");
				}
				else {	//If everything is correct adds to the evaluator list and closes the window.
					frmMdosts.setVisible(false);
					calendarItemList.remove(index);
					item.setsummary(summary);
					item.setlocation(location);
					item.setdtStart(dtstart);
					item.setdtEnd(dtend);
					calendarItemList.add(item);
					try {	//Edit the event in SQL based on UID
						Connection con = Func.connectToSql();
						Func.editEventSql(con, item.getuid(), Login_main.getUsrn().toLowerCase(),
								summary, location, dtstart, dtend);
						con.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					mini.removeTrayIcon();
					frmMdosts.dispose();
					listView();
					
				}
			}
		});
		
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 5, 5);
		gbc_btnSend.fill = GridBagConstraints.VERTICAL;
		gbc_btnSend.gridx = 1;
		gbc_btnSend.gridy = 9;
		frmMdosts.getContentPane().add(btnSend, gbc_btnSend);
		mini.trayIcon(frmMdosts);
	}
	
	/**
	 * Search for the item in the local list and returns the index of it
	 * @param uid	: Ssid to search
	 * @return	The index of the item in the local list
	 */
	private int searchForItem(String ssid){
		for (int i = 0; i < calendarItemList.size(); i++) {
            CalendarItem obj = calendarItemList.get(i);
            if (obj.getuid() == ssid) {
            	item = obj;
            	return i;
            }
        }
		return 0;
	}

}
