package szakdolgozat;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

/**
 * This is the Tray icon class
 */
public class Tray {
	static TrayIcon pubI;	//The tray icon
	static SystemTray pubT;	//The system tray
	/**
	 * This is the method for activating the tray icon
	 * @param f :The JFram window what the tray icon attached to
	 */
	public void trayIcon(JFrame f) {
		 	if (!SystemTray.isSupported()) {	//Check the system tray for trayicon support
		 		System.out.println("SystemTray is not supported");
	            return;
	        }
		 	final PopupMenu popup = new PopupMenu();
		 	final SystemTray tray = SystemTray.getSystemTray();
		 	Image icon = Toolkit.getDefaultToolkit().createImage(".//src//szakdolgozat//calendar.png");
		 	TrayIcon trayIcon = new TrayIcon(icon, "Órarend");
		 	pubI = trayIcon;
		 	pubT = tray;
		 	trayIcon.setImageAutoSize(true);
     		trayIcon.setToolTip("Órarend");
     		
     		trayIcon.addActionListener(new ActionListener() {
     			public void actionPerformed(ActionEvent evt) {
     				f.setVisible(true);
     	            f.setState(Frame.NORMAL);
     			};
     		});
     		
     		MenuItem open = new MenuItem("Megnyitás");
     		open.addActionListener(new ActionListener() {
     	        public void actionPerformed(ActionEvent e) {
     	            f.setVisible(true);
     	            f.setState(Frame.NORMAL);
     	        }
     	    });
     		MenuItem close = new MenuItem("Bezárás");
     		close.addActionListener(new ActionListener() {
     	        public void actionPerformed(ActionEvent e) {
     	            System.exit(0);
     	        }
     	    });
     		popup.add(open);
     		popup.add(close);
     		trayIcon.setPopupMenu(popup);
     		
     		try {
                tray.add(trayIcon);
                notificationCalendar();
            } catch (AWTException e) {
                System.out.println("TrayIcon could not be added.");
            }

	 }

	private void notificationCalendar() {
    	Iterator<CalendarItem> iter = CalendarFrame.calendarItemList.iterator();
    	LocalDateTime date = LocalDateTime.now();
		Timer time = new Timer();
		while (iter.hasNext()) {
			CalendarItem event = iter.next();
			LocalDateTime dtstart = event.getdtStart();
			LocalDateTime fiveMinutesBeforeStart = dtstart.minusMinutes(5);
			if(fiveMinutesBeforeStart.compareTo(date) > 0) {
			TimerTask task = new TimerTask(){
				@Override
				public void run() {
				pubI.displayMessage("Esemény 5 perc múlva!", "Leírás: "+event.getsummary()+System.lineSeparator()+"Helyszín: "+event.getlocation()+System.lineSeparator()+"Kezdés ideje: "+Func.printFormatDate(dtstart, true)+System.lineSeparator()+"Vége: "+Func.printFormatDate(event.getdtEnd(),true), MessageType.INFO);
				}
			};
			time.schedule(task, Func.convertToDate(fiveMinutesBeforeStart));
			}
		}
	}

	public void removeTrayIcon() {
		pubT.remove(pubI);
	}
}
