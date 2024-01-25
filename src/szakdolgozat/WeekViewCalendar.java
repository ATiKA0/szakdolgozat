package szakdolgozat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WeekViewCalendar extends CalendarFrame {
	private JFrame frame;
    private JTable weekViewTable;
    private DefaultTableModel tableModel;

    public WeekViewCalendar(LocalDateTime dateToOpen) {
    	LocalDate today = dateToOpen.toLocalDate();
    	LocalDateTime startOfWeek = today.atStartOfDay().with(DayOfWeek.MONDAY);
    	LocalDateTime endOfWeek = today.atStartOfDay().with(DayOfWeek.SUNDAY).plusHours(23).plusMinutes(59);
    	
    	frame = new JFrame(Func.printFormatDate(startOfWeek, false)+" - "+Func.printFormatDate(endOfWeek, false));
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
        		mini.removeTrayIcon(); 
        		display();
        	}
        });
        frame.setSize(800, 600);


        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        weekViewTable = new JTable(tableModel);
        weekViewTable.setRowHeight(60);
        weekViewTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        weekViewTable.setShowHorizontalLines(false);
        weekViewTable.setIntercellSpacing(new Dimension(1,0));

        String[] headers = {"Hétfő", "Kedd", "Szerda", "Csütörtök", "Péntek", "Szombat", "Vasárnap", "Idő"};
        // Add columns for each day of the week
        for (String day : headers) {
            tableModel.addColumn(day);
        }

        // Initialize the table with empty cells
        for (int row = 0; row < 24 * 2; row++) {
            tableModel.addRow(new Object[7]);
        }
        
        //Add time column
        ArrayList<String> times = new ArrayList<String>();
        for(int f=0; f<25; f++) {
        	times.add(Integer.toString(f)+":00");
        	times.add(Integer.toString(f)+":30");
        }
        for(int f=0; f<48; f++) {
        	tableModel.setValueAt(times.get(f), f, 7);
        }

        JScrollPane scrollPane = new JScrollPane(weekViewTable);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        weekViewTable.addMouseListener(new MouseAdapter() {	//This is the listener for clicking on an element in the table. When clicked shows the full information about lesson.
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (arg0.getClickCount() == 1) { 

                    int column = weekViewTable.getSelectedColumn();
                    int row = weekViewTable.getSelectedRow();
                    
                    Object cell = weekViewTable.getValueAt(row, column);
                    String str = (String) cell;
                    if(str!=null) {
                    	JOptionPane.showMessageDialog(null, str);
                    }
                    
                }
            }
        });
        

        // Add calendar items
        List<CalendarItem> calendarItems = getCalendarItems(startOfWeek, endOfWeek);

        for (CalendarItem item : calendarItems) {
            LocalDateTime itemStart = item.getdtStart();
            LocalDateTime itemEnd = item.getdtEnd();

            int rowStart = itemStart.getHour() * 2 + itemStart.getMinute() / 30;
            int rowEnd = itemEnd.getHour() * 2 + itemEnd.getMinute() / 30;

            // Calculate the column range for the event
            int colStart = itemStart.getDayOfWeek().getValue() - 1; // Adjust for 0-based index
            int colEnd = itemEnd.getDayOfWeek().getValue() - 1; // Adjust for 0-based index

            // Set the event text in the appropriate cells for each day the event spans
            for (int col = colStart; col <= colEnd; col++) {
                for (int row = rowStart; row <= rowEnd; row++) {
                    if (col == colStart && row == rowStart) {
                        // Display event details only in the starting cell
                        tableModel.setValueAt(printItem(item), row, col);
                    } else {
                        // For cells within the event range, mark them as empty
                        tableModel.setValueAt("", row, col);
                    }
                }
            }
        }
        weekViewTable.setDefaultRenderer(weekViewTable.getColumnClass(1), new tblCalendarRenderer());
        mini.trayIcon(frame);
        frame.setVisible(true);
    }
    
    /**
     * Format the Neptun timetable items to print. If it's not from Neptun leave it
     * @param input : Calendar item to format
     * @return
     */
    private static String printItem(CalendarItem input) {
    	StringBuffer cnvt = new StringBuffer(input.getsummary());
    	try {
	    	String type;
	    	if(input.getsummary().contains("EA")) {
	    		type = " - Előadás";
	    	}
	    	else if(input.getsummary().contains("GY")) {
	    		type = " - Gyakorlat";
	    	}
	    	else {
	    		type = "";
	    	}
	    	cnvt.delete(cnvt.indexOf("-",0), cnvt.length());
	    	return cnvt.toString()+type+" - "+input.getlocation();
	    	}
    	catch(Exception e) {
    		return input.getsummary();
    	}
    }

    /**
     * Get the calendar items which on the selected week from the main list
     * @param startOfWeek : Start day of the week
     * @param endOfWeek : End day of the week
     * @return A list of the calendar items
     */
    private List<CalendarItem> getCalendarItems(LocalDateTime startOfWeek, LocalDateTime endOfWeek) {
    	Stream<LocalDate> dates = startOfWeek.toLocalDate().datesUntil(endOfWeek.toLocalDate());	//Get the days of the week
        List<LocalDate> dateslist = dates.collect(Collectors.toList());	//Get the dates to a list        ArrayList<CalendarItem> isEqualWeek = new ArrayList<CalendarItem>();	//Make a list for the items is on the selected week
        List<CalendarItem> calendarItems = new ArrayList<>();
        
        for (LocalDate dat : dateslist) {
    		for (CalendarItem d : calendarItemList) {
    			if(d.getdtStart().toLocalDate().equals(dat))calendarItems.add(d);	//Collects the items that on the selected week by the start date
    		}
    	}
        return calendarItems;
    }

    static class tblCalendarRenderer extends JTextArea implements TableCellRenderer{
    	public tblCalendarRenderer() {
    		setLineWrap(true);
    		setWrapStyleWord(true);
    		}
        public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column){
            if (column == 5 || column == 6){ //Week-end
                setBackground(new Color(255, 220, 220));
            }
            else{ //Week days
                setBackground(new Color(255, 255, 255));
            }
            
            if(value != null && column != 7)
            {
            	setBackground(new Color(0, 213, 255));
            }
            setText((String)value);
            setBorder(null);
            if(column==7) {
            	setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
            }
            setForeground(Color.black);
            return this; 
        }
    }
}