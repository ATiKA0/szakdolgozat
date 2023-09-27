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
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;

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
				evaluator.remove();	//Reload the evaluator
				createEvaluator();
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
		TableColumnModel columnModel = table.getColumnModel();	//Staff to hide the first column with SSID
		TableColumn firstColumn = columnModel.getColumn(0);	//Get the first column contains SSID
        firstColumn.setMinWidth(0);
        firstColumn.setMaxWidth(0);
        firstColumn.setPreferredWidth(0);
        firstColumn.setResizable(false);
		JPopupMenu popupMenu = new JPopupMenu();	
        JMenuItem deleteMenuItem = new JMenuItem("Esemény törlése");
        JMenuItem modifyMenuItem = new JMenuItem("Esemény módosítása");
        deleteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {	//Get the selected row and remove it from SQL and local
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                	String selectedUid = array[selectedRow][0].toString();
                	try {
                		Connection con = Func.connectToSql();
                		Func.deleteFromSql(con, selectedUid, Login_main.getUsrn().toLowerCase());
						con.close();
						removeItemFromList(selectedUid);
						model.removeRow(selectedRow);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
                }
            }
        });
	     
	    popupMenu.add(deleteMenuItem);
	    table.addMouseListener(new MouseAdapter() {	//Show the popup menu on right click
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
	
	
	/**
	 * Get the item from the main event list and parse it to an object for the table
	 * @return Object for the JTable
	 */
	private Object[][] createTableViev() {
		ArrayList<CalendarItem> isEqual = new ArrayList<CalendarItem>();	//Creating a new array list for the chosen calendar items
		int i = 0;
		for (CalendarItem d : calendarItemList) {	//This for each loop adding the items to the array list which starting date is equal to the chosen one
			if(d.getdtStart().toLocalDate().equals(getchosenDate().toLocalDate()))isEqual.add(d);
		}
		Object[][] array = new Object[isEqual.size()][];	//Creating the array which is returned to the main table
		for (CalendarItem c : isEqual)		//This for loop creates the objects from the array list
		{
			if(c.getdtStart().toLocalDate().equals(getchosenDate().toLocalDate())) {
			    array[i] = new Object[5];
				array[i][0] = c.getuid();
			    array[i][1] = c.getsummary();
			    array[i][2] = c.getlocation();
			    array[i][3] = Func.printFormatDate(c.getdtStart(),true);
			    array[i][4] = Func.printFormatDate(c.getdtEnd(),true);
			    i++;
			}
		}
		return array;
	}

	private void removeItemFromList(String uid) {
		for (int i = 0; i < calendarItemList.size(); i++) {
            CalendarItem obj = calendarItemList.get(i);
            if (obj.getuid() == uid) {
            	calendarItemList.remove(i); // Remove the object with the matching ID
                break; // Exit the loop once the object is removed
            }
        }
	}
}
