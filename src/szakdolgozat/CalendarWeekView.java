package szakdolgozat;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/*
 * cLass variables 
 */
public class CalendarWeekView extends CalendarFrame{
    static JTable tblCalendar;
    static JFrame frmMain;
    static Container pane;
    static DefaultTableModel mtblCalendar; //Table model
    static JScrollPane stblCalendar; //The scrollpane
    static JPanel pnlCalendar;
    static int weekOfYear, year;
    static long weekCount;
    static LocalDateTime firstDayOfWeek, lastDayOfWeek, dateToOpen;
 
    /**
     * @wbp.parser.entryPoint
     */
    public void start(LocalDateTime dateImp){
    	System.setProperty("file.encoding","UTF-8");
    	WeekFields weekNumbering = WeekFields.of(new Locale("hu","HU"));
    	dateToOpen = dateImp;
        weekOfYear = dateToOpen.get(weekNumbering.weekOfWeekBasedYear());
        year = dateToOpen.get(weekNumbering.weekBasedYear());
        weekCount = dateToOpen.range(WeekFields.ISO.weekOfWeekBasedYear()).getMaximum();
        firstDayOfWeek = dateToOpen.with(LocalTime.MIN).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        lastDayOfWeek = dateToOpen.with(LocalTime.MAX).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
 
        //Prepare frame
        frmMain = new JFrame (Func.printFormatDate(firstDayOfWeek, false)+" - "+Func.printFormatDate(lastDayOfWeek, false)); //Create frame
        frmMain.addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
        		mini.removeTrayIcon(); 
        		display();
        	}
        });
        frmMain.setSize(1040, 651);
        pane = frmMain.getContentPane();
        pane.setLayout(null);
        frmMain.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        mtblCalendar = new DefaultTableModel(){public boolean isCellEditable(int rowIndex, int mColIndex){return false;}};
        tblCalendar = new JTable(mtblCalendar);
        tblCalendar.setCellSelectionEnabled(true);
        tblCalendar.setShowHorizontalLines(false);
        tblCalendar.setIntercellSpacing(new Dimension(1,0));
        tblCalendar.setFont(new Font("Tahoma", Font.PLAIN, 10));
        stblCalendar = new JScrollPane(tblCalendar);
        pnlCalendar = new JPanel(null);
        tblCalendar.addMouseListener(new MouseAdapter() {	//This is the listener for clicking on an elemnt in the table. When clicked shows the full information about lesson.
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (arg0.getClickCount() == 1) { 

                    int column = tblCalendar.getSelectedColumn();

                    int row = tblCalendar.getSelectedRow();
                    String str = (String) tblCalendar.getValueAt(row, column);
                    if(str!=null) {
                    	JOptionPane.showMessageDialog(null, str);
                    }
                    
                }
            }
        });
        //Set border
        pnlCalendar.setBorder(BorderFactory.createTitledBorder("Heti nézet - Kattintással nagyítható"));
         
        //Add controls to pane
        pane.add(pnlCalendar);
        pnlCalendar.add(stblCalendar);
         
        //Set bounds
        pnlCalendar.setBounds(0, 0, 1026, 614);
        stblCalendar.setBounds(10, 20, 1006, 584);
        frmMain.setVisible(true);
        
        //Add headers
        String[] headers = {"Idő", "Hétfő", "Kedd", "Szerda", "Csütörtök", "Péntek", "Szombat", "Vasárnap"}; //All headers
        for (int i=0; i<8; i++){
            mtblCalendar.addColumn(headers[i]);
        }
        
        tblCalendar.setRowHeight(40);
        tblCalendar.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        mtblCalendar.setColumnCount(8);
        mtblCalendar.setRowCount(48);
        //Add time column
        ArrayList<String> times = new ArrayList<String>();
        for(int f=0; f<25; f++) {
        	times.add(Integer.toString(f)+":00");
        	times.add(Integer.toString(f)+":30");
        }
        for(int f=0; f<48; f++) {
        	mtblCalendar.setValueAt(times.get(f), f, 0);
        }
        
        //Set background
        tblCalendar.getParent().setBackground(tblCalendar.getBackground()); 
 
        //No resize/reorder
        tblCalendar.getTableHeader().setResizingAllowed(true);
        tblCalendar.getTableHeader().setReorderingAllowed(false);
 
        //Single cell selection
        tblCalendar.setCellSelectionEnabled(true);
        tblCalendar.setColumnSelectionAllowed(true);
        tblCalendar.setRowSelectionAllowed(true);
        tblCalendar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         
        //Refresh calendar
        refreshCalendar ();
        
        mini.trayIcon(frmMain);
        mini.notificationCalendar();
    }
    /*
     * This function called when the table created. This is the function which writes the lessons in the table. 
     * 
     */
    public static void refreshCalendar(){  
        
        Stream<LocalDate> dates = firstDayOfWeek.toLocalDate().datesUntil(lastDayOfWeek.toLocalDate().plusDays(1));	//Get the days of the week
        List<LocalDate> dateslist = dates.collect(Collectors.toList());	//Get the dates to a list
        ArrayList<CalendarItem> isEqualWeek = new ArrayList<CalendarItem>();	//Make a list for the items is on the selected week
		for (LocalDate dat : dateslist) {
		for (CalendarItem d : calendarItemList) {
			if(d.getdtStart().toLocalDate().equals(dat))isEqualWeek.add(d);	//Collects the items that on the selected week by the start date
		}
		}
		
		int rows,cols,rowe,cole;
		for(CalendarItem item : isEqualWeek) {
			cols=item.getdtStart().getDayOfWeek().getValue();	//Set the colum by the day of the week
			rows=item.getdtStart().getHour()*2;	//Set the row by the starting hour
			if(item.getdtStart().getMinute()>30)rows++;	//If the start time is more the half hour move a row lower.
			String print = printItem(item);	//Print the item to the cell
			
			int l = print.length()/20;	//Check the text's length.
			if(l<1) {
			mtblCalendar.setValueAt(printItem(item), rows, cols);
			cole=item.getdtEnd().getDayOfWeek().getValue();
			rowe=item.getdtEnd().getHour()*2;
			if(item.getdtEnd().getMinute()>30)rowe++;
			for(int c = rows+1; c<=rowe; c++)
			{
				mtblCalendar.setValueAt("", c, cole);
			}
			}
			else {
				int mag = tblCalendar.getRowHeight();
				tblCalendar.setRowHeight(rows,mag+21*l);
				mtblCalendar.setValueAt(printItem(item), rows, cols);
				cole=item.getdtEnd().getDayOfWeek().getValue();
				rowe=item.getdtEnd().getHour()*2;
				if(item.getdtEnd().getMinute()>30)rowe++;
				for(int c = rows+1; c<=rowe; c++)
				{
					tblCalendar.setRowHeight(c,mag + 21*l);
					mtblCalendar.setValueAt("", c, cole);
				}
			}
		}

        //Apply renderers
        tblCalendar.setDefaultRenderer(tblCalendar.getColumnClass(1), new tblCalendarRenderer());
    }
    /*
     * With this method print the items in the item with the correct format in the cell.
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
    /*
     * This is the table renderer which set the colors and formats for the table.
     */
    static class tblCalendarRenderer extends JTextArea implements TableCellRenderer{
    	public tblCalendarRenderer() {
    		setLineWrap(true);
    		setWrapStyleWord(true);
    		}
        public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column){
            if (column == 6 || column == 7){ //Week-end
                setBackground(new Color(255, 220, 220));
            }
            else{ //Week days
                setBackground(new Color(255, 255, 255));
            }
            if(value != null && column != 0)
            {
            	setBackground(new Color(0, 213, 255));
            }
            setText((String)value);
            setBorder(null);
            if(column==0) {
            	setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
            }
            setForeground(Color.black);
            return this; 
        }
    }
}
