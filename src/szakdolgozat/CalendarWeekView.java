package szakdolgozat;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.BadLocationException;

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
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
 
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
        tblCalendar.setShowHorizontalLines(false);
        tblCalendar.setIntercellSpacing(new Dimension(1,0));
        tblCalendar.setFont(new Font("Tahoma", Font.PLAIN, 10));
        stblCalendar = new JScrollPane(tblCalendar);
        pnlCalendar = new JPanel(null);
        tblCalendar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (arg0.getClickCount() == 1) { 

                    int column = tblCalendar.getSelectedColumn();

                    int row = tblCalendar.getSelectedRow();
                    String str = (String) tblCalendar.getValueAt(row, column);
                    if(str!="") {
                    	JOptionPane.showMessageDialog(null, str);
                    }
                    
                }
            }
        });
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
        
        tblCalendar.getParent().setBackground(tblCalendar.getBackground()); //Set background
 
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

    public static void refreshCalendar(){  
        
        Stream<LocalDate> dates = firstDayOfWeek.toLocalDate().datesUntil(lastDayOfWeek.toLocalDate().plusDays(1));
        List<LocalDate> dateslist = dates.collect(Collectors.toList());
        ArrayList<CalendarItem> isEqualWeek = new ArrayList<CalendarItem>();
		int i = 0;
		for (LocalDate dat : dateslist) {
		for (CalendarItem d : calendarItemList) {
			if(d.getdtStart().toLocalDate().equals(dat))isEqualWeek.add(d);
		}
		}
		
		int rows,cols,rowe,cole;
		for(CalendarItem item : isEqualWeek) {
			cols=item.getdtStart().getDayOfWeek().getValue();
			rows=item.getdtStart().getHour()*2;
			if(item.getdtStart().getMinute()>30)rows++;
			String print = printItem(item);
			int l = print.length()/20;
			tblCalendar.setRowHeight(rows,21*l);
			mtblCalendar.setValueAt(printItem(item), rows, cols);
			cole=item.getdtEnd().getDayOfWeek().getValue();
			rowe=item.getdtEnd().getHour()*2;
			if(item.getdtEnd().getMinute()>30)rowe++;
			for(int c = rows+1; c<=rowe; c++)
			{
				tblCalendar.setRowHeight(c,21*l);
				mtblCalendar.setValueAt("", c, cole);
			}
			
		}

        //Apply renderers
        tblCalendar.setDefaultRenderer(tblCalendar.getColumnClass(1), new tblCalendarRenderer());
    }
    
    private static String printItem(CalendarItem input) {
    	StringBuffer cnvt = new StringBuffer(input.getsummary());
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
    
    static class tblCalendarRenderer extends JTextArea implements TableCellRenderer{
    	public tblCalendarRenderer() {
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
