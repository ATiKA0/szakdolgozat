package szakdolgozat;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CalendarDayView extends CalendarFrame {
//This is a Day View window for the calendar using a list view.
	/**
	 * @wbp.parser.entryPoint
	 */
	public void listView() {
		
		JFrame frm = new JFrame();	//Initialize window frame
		frm.addWindowListener(new WindowAdapter() {	//When frame is closed the program goes back to the main calendar window
			@Override
			public void windowClosing(WindowEvent e) {		
				mini.removeTrayIcon();	//Removing the tray icon for this window
				display();	//Return to the main window
			}
		});
		frm.setTitle(printFormatDate(getchosenDate(),false));//In the head title print the chosen date
		frm.setBounds(100, 100, 700, 680);
		frm.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);	//When 'X' pressed just hide and not stop the application
		String[] columnNames = {"Esemény leírása", "Helyszín", "Kezdés ideje", "Befejezés ideje"};	//Head of the table view
		Object[][] array = createTableViev();	//Generating the table view with another method and load the table object
		frm.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));	// Set frame layout
		JTable table = new JTable(array, columnNames);	//Creating the table with the head and the objects
		table.setRowHeight(30);
		table.setEnabled(false);
		JScrollPane sp = new JScrollPane(table);	//Insert table into a scroll pane
		frm.getContentPane().add(sp);
		frm.toFront();	//When open the window opens at foreground
		frm.requestFocus();	//When open the window requests the focus
		mini.trayIcon(frm);	//Open the tray icon in the notification center
		mini.notificationCalendar();	//Trigger the notifications for the events
		frm.setVisible(true);	//Set the window visible
	}
	
	
	private Object[][] createTableViev() {
		ArrayList<CalendarItem> isEqual = new ArrayList<CalendarItem>();	//Creating a new array list for the chosen calendar items
		int i = 0;
		for (CalendarItem d : calendarItemList) {	//This for each loop adding the items to the array list which starting date is equal to the chosen one
			if(d.getdtStart().toLocalDate().equals(getchosenDate().toLocalDate()))isEqual.add(d);
		}
		Object[][] array = new Object[isEqual.size()][];	//Creating the array which is returned to the main table
		for (CalendarItem c : isEqual)		//This for loop creates the objects from the array list
		{
			if(c.getdtStart().toLocalDate().equals(getchosenDate().toLocalDate()))
		    array[i] = new Object[4];
		    array[i][0] = c.getsummary();
		    array[i][1] = c.getlocation();
		    array[i][2] = printFormatDate(c.getdtStart(),true);
		    array[i][3] = printFormatDate(c.getdtEnd(),true);
		    i++;
		}
		return array;
	}
}
