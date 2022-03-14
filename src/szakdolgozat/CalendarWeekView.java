package szakdolgozat;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
 
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
    	
    	WeekFields weekNumbering = WeekFields.of(new Locale("hu","HU"));
    	dateToOpen = dateImp;
        weekOfYear = dateToOpen.get(weekNumbering.weekOfWeekBasedYear());
        year = dateToOpen.get(weekNumbering.weekBasedYear());
        weekCount = dateToOpen.range(WeekFields.ISO.weekOfWeekBasedYear()).getMaximum();
        firstDayOfWeek = dateToOpen.with(LocalTime.MIN).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        lastDayOfWeek = dateToOpen.with(LocalTime.MAX).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
 
        //Prepare frame
        frmMain = new JFrame (printFormatDate(firstDayOfWeek, false)+" - "+printFormatDate(lastDayOfWeek, false)); //Create frame
        frmMain.addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
        		mini.removeTrayIcon();
        		display();
        	}
        });
        frmMain.setSize(1040, 651); //Set size to 400x400 pixels
        pane = frmMain.getContentPane(); //Get content pane
        pane.setLayout(null); //Apply null layout
        frmMain.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        mtblCalendar = new DefaultTableModel(){public boolean isCellEditable(int rowIndex, int mColIndex){return false;}};
        tblCalendar = new JTable(mtblCalendar);
        tblCalendar.setCellSelectionEnabled(true);
        tblCalendar.setFont(new Font("Tahoma", Font.PLAIN, 10));
        stblCalendar = new JScrollPane(tblCalendar);
        pnlCalendar = new JPanel(null);
        //Set border
        pnlCalendar.setBorder(BorderFactory.createTitledBorder("Heti nézet"));
         
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
        
        tblCalendar.setRowHeight(45);
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
        
        tblCalendar.getParent().setBackground(tblCalendar.getBackground()); //Set background
 
        //No resize/reorder
        tblCalendar.getTableHeader().setResizingAllowed(true);
        tblCalendar.getTableHeader().setReorderingAllowed(false);
 
        //Single cell selection
        tblCalendar.setColumnSelectionAllowed(true);
        tblCalendar.setRowSelectionAllowed(true);
        tblCalendar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         
        //Refresh calendar
        refreshCalendar ();
        
        mini.trayIcon(frmMain);
        mini.notificationCalendar();
    }
     
    public static void refreshCalendar(){  
        
        Stream<LocalDate> dates = firstDayOfWeek.toLocalDate().datesUntil(lastDayOfWeek.toLocalDate().plusDays(1));
        List<LocalDate> dateslist = dates.collect(Collectors.toList());
        ArrayList<CalendarItem> isEqualWeek = new ArrayList<CalendarItem>();
		int i = 0;
		for (LocalDate dat : dateslist) {
		for (CalendarItem d : sus) {
			if(d.getDtstart().toLocalDate().equals(dat))isEqualWeek.add(d);
		}
		}
		
		int rows,cols,rowe,cole;
		for(CalendarItem item : isEqualWeek) {
			cols=item.getDtstart().getDayOfWeek().getValue();
			rows=item.getDtstart().getHour()*2;
			System.out.println(rows);
			if(item.getDtstart().getMinute()>30)rows++;
			mtblCalendar.setValueAt(item.getSummary(), rows, cols);
			cole=item.getDtend().getDayOfWeek().getValue();
			rowe=item.getDtend().getHour()*2;
			if(item.getDtend().getMinute()>30)rowe++;
			for(int c = rows; c<=rowe; c++)
			{
				mtblCalendar.setValueAt(item.getSummary(), c, cole);
			}
			
		}

        //Apply renderers
        tblCalendar.setDefaultRenderer(tblCalendar.getColumnClass(1), new tblCalendarRenderer());
    }
    static class tblCalendarRenderer extends JTextArea implements TableCellRenderer{
    	public tblCalendarRenderer() {
    		setOpaque(true);
    		setLineWrap(true);
    		setWrapStyleWord(true);
    		}
        public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column){
            if (column == 6 || column == 7){ //Week-end
                setBackground(new Color(255, 220, 220));
            }
            else{ //Wee
                setBackground(new Color(255, 255, 255));
            }
            if(value != null && column != 0)
            {
            	setBackground(new Color(255, 220, 220));
            }
            setSize(table.getColumnModel().getColumn(column).getWidth(),
                    Short.MAX_VALUE);
            setText((value == null)
            		? ""
            		: value.toString());

            setBorder(null);
            setForeground(Color.black);
            return this; 
        }
    }
}
