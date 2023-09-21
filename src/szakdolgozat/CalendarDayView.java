package szakdolgozat;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CalendarDayView extends CalendarFrame {
//This is a Day View window for the calendar using a list view.
	/**
	 * @wbp.parser.entryPoint
	 */
	public void listView() {
		System.setProperty("file.encoding","UTF-8");
		JFrame frm = new JFrame();	//Initialize window frame
		frm.addWindowListener(new WindowAdapter() {	//When frame is closed the program goes back to the main calendar window
			@Override
			public void windowClosing(WindowEvent e) {		
				mini.removeTrayIcon();	//Removing the tray icon for this window
				display();	//Return to the main window
			}
		});
		frm.setTitle(Func.printFormatDate(getchosenDate(),false));//In the head title print the chosen date
		frm.setBounds(100, 100, 700, 680);
		frm.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);	//When 'X' pressed just hide and not stop the application
		String[] columnNames = {"SSID", "Esemény leírása", "Helyszín", "Kezdés ideje", "Befejezés ideje"};	//Head of the table view
		Object[][] array = createTableViev();	//Generating the table view with another method and load the table object
		frm.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));	// Set frame layout
		DefaultTableModel model = new DefaultTableModel(array, columnNames);	//Creating the table model with the head and the objects
		JTable table = new JTable(model);	//Generate the table
		table.setRowHeight(30);
		table.setEnabled(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);	//Set table selection to only one item
		JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteMenuItem = new JMenuItem("Esemény törlése");
        deleteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                	System.out.println(array[selectedRow][0]);
                }
            }
        });
	     
	    popupMenu.add(deleteMenuItem);
	    table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        table.setRowSelectionInterval(row, row);
                        popupMenu.show(table, e.getX(), e.getY());
                    }
                }
            }
        });
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
		    array[i][2] = Func.printFormatDate(c.getdtStart(),true);
		    array[i][3] = Func.printFormatDate(c.getdtEnd(),true);
		    i++;
		}
		return array;
	}
}
