package szakdolgozat;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CalendarDayView extends CalendarFrame {

	private JFrame frmLol;
	private JTable table;
	/**
	 * @wbp.parser.entryPoint
	 */
	public void listView() {
		
		frmLol = new JFrame();
		frmLol.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				display();
			}
		});
		frmLol.setTitle(printFormatDate(getChoosenDate(),false));
		frmLol.setBounds(100, 100, 700, 680);
		frmLol.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		String[] columnNames = {"Summary", "Location", "Datestart", "Dateend"};
		Object[][] array = createTableViev();
		frmLol.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		table = new JTable(array, columnNames);
		table.setRowHeight(30);
		table.setEnabled(false);
		JScrollPane sp = new JScrollPane(table);
		frmLol.getContentPane().add(sp);
		frmLol.toFront();
		frmLol.requestFocus();
		frmLol.setVisible(true);
	}
	
	
	private Object[][] createTableViev() {
		ArrayList<CalendarItem> isEqual = new ArrayList<CalendarItem>();
		int i = 0;
		for (CalendarItem d : sus) {
			if(d.getDtstart().toLocalDate().equals(getChoosenDate().toLocalDate()))isEqual.add(d);
		}
		Object[][] array = new Object[isEqual.size()][];
		for (CalendarItem c : isEqual)
		{
			if(c.getDtstart().toLocalDate().equals(getChoosenDate().toLocalDate()))
		    array[i] = new Object[4];
		    array[i][0] = c.getSummary();
		    array[i][1] = c.getLocation();
		    array[i][2] = printFormatDate(c.getDtstart(),true);
		    array[i][3] = printFormatDate(c.getDtend(),true);
		    i++;
		}
		return array;
	}
}
