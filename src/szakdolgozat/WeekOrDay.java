package szakdolgozat;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class WeekOrDay extends CalendarFrame{
	/**
	 * @wbp.parser.entryPoint
	 */
	
	/*
	 * This is the window where we can select which view want to see. Can select between day and week view.
	 * In the top of the window we can see the chosen date.
	 */
	void WeekOrDayinit() {
		System.setProperty("file.encoding","UTF-8");
		JFrame frame;
		frame = new JFrame();
		frame.setBounds(100, 100, 501, 193);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnDayView = new JButton("Napi nézet");
		btnDayView.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				CalendarDayView dayView = new CalendarDayView();
                dayView.listView();
                frame.dispose();
			}
		});
		btnDayView.setBounds(69, 76, 139, 49);
		frame.getContentPane().add(btnDayView);
		
		JButton btnWeekView = new JButton("Heti nézet");
		btnWeekView.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				CalendarWeekView week = new CalendarWeekView();
		    	week.start(getchosenDate());
		    	frame.dispose();
			}
		});
		btnWeekView.setBounds(277, 76, 139, 49);
		frame.getContentPane().add(btnWeekView);
		
		JLabel lblText = new JLabel("A választott dátum:");
		lblText.setBounds(109, 27, 113, 13);
		frame.getContentPane().add(lblText);
		
		JLabel lblDate = new JLabel(printFormatDate(getchosenDate(), false));
		lblDate.setBounds(232, 27, 184, 13);
		frame.getContentPane().add(lblDate);
		frame.setVisible(true);
	}
}
