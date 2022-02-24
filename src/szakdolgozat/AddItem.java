package szakdolgozat;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AddItem{
	
	String Uid;
	Date Dtstart;
	Date Dtend;
	String Location;
	String Summary;
	CalendarItem returned = new CalendarItem(null, null, null, null, null);
	
    protected void MouseAdapter() {}

    /**
     * {@inheritDoc}
     */
    public void mouseClicked(MouseEvent e) {}

    /**
     * {@inheritDoc}
     */
    public void mousePressed(MouseEvent e) {}

    /**
     * {@inheritDoc}
     */
    public void mouseReleased(MouseEvent e) {}

    /**
     * {@inheritDoc}
     */
    public void mouseEntered(MouseEvent e) {}

    /**
     * {@inheritDoc}
     */
    public void mouseExited(MouseEvent e) {}

    /**
     * {@inheritDoc}
     * @since 1.6
     */
    public void mouseWheelMoved(MouseWheelEvent e){}

    /**
     * {@inheritDoc}
     * @since 1.6
     */
    public void mouseDragged(MouseEvent e){}

    /**
     * {@inheritDoc}
     * @since 1.6
     */
    public void mouseMoved(MouseEvent e){}

	public static void main(String[] args) {
		EventQueue.invokeLater(new AddItem()::initialize);
	}


	/**
	 * @wbp.parser.entryPoint
	 */
	void initialize() {
		System.setProperty("file.encoding","UTF-8");
		JFrame frame  = new JFrame();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setTitle("Új bejegyzés");
		frame.setBounds(100, 100, 360, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{65, 212, 65, 0};
		gridBagLayout.rowHeights = new int[]{55, 36, 50, 36, 50, 36, 50, 36, 10, 37, 17, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JLabel lblSummary = new JLabel("Leírás");
		GridBagConstraints gbc_lblSummary = new GridBagConstraints();
		gbc_lblSummary.anchor = GridBagConstraints.SOUTH;
		gbc_lblSummary.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummary.gridx = 1;
		gbc_lblSummary.gridy = 0;
		frame.getContentPane().add(lblSummary, gbc_lblSummary);
		
		JTextField textSummary = new JTextField();
		GridBagConstraints gbc_textSummary = new GridBagConstraints();
		gbc_textSummary.fill = GridBagConstraints.BOTH;
		gbc_textSummary.insets = new Insets(0, 0, 5, 5);
		gbc_textSummary.gridx = 1;
		gbc_textSummary.gridy = 1;
		frame.getContentPane().add(textSummary, gbc_textSummary);
		textSummary.setColumns(10);
		
		JLabel lblLocation = new JLabel("Helyszín");
		GridBagConstraints gbc_lblLocation = new GridBagConstraints();
		gbc_lblLocation.anchor = GridBagConstraints.SOUTH;
		gbc_lblLocation.insets = new Insets(0, 0, 5, 5);
		gbc_lblLocation.gridx = 1;
		gbc_lblLocation.gridy = 2;
		frame.getContentPane().add(lblLocation, gbc_lblLocation);
		
		JTextField textLocation = new JTextField();
		textLocation.setColumns(10);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 3;
		frame.getContentPane().add(textLocation, gbc_textField);
		
		JLabel lblDtstart = new JLabel("Kezdés ideje");
		GridBagConstraints gbc_lblDtstart = new GridBagConstraints();
		gbc_lblDtstart.anchor = GridBagConstraints.SOUTH;
		gbc_lblDtstart.insets = new Insets(0, 0, 5, 5);
		gbc_lblDtstart.gridx = 1;
		gbc_lblDtstart.gridy = 4;
		frame.getContentPane().add(lblDtstart, gbc_lblDtstart);
		
		JDateChooser dateDtstart = new JDateChooser();
		dateDtstart.setDateFormatString("yyyy.MM.dd. hh:mm");
		GridBagConstraints gbc_dateDtstart = new GridBagConstraints();
		gbc_dateDtstart.fill = GridBagConstraints.BOTH;
		gbc_dateDtstart.insets = new Insets(0, 0, 5, 5);
		gbc_dateDtstart.gridx = 1;
		gbc_dateDtstart.gridy = 5;
		frame.getContentPane().add(dateDtstart, gbc_dateDtstart);
		
		JLabel lblDtend = new JLabel("Befejezés");
		GridBagConstraints gbc_lblDtend = new GridBagConstraints();
		gbc_lblDtend.anchor = GridBagConstraints.SOUTH;
		gbc_lblDtend.insets = new Insets(0, 0, 5, 5);
		gbc_lblDtend.gridx = 1;
		gbc_lblDtend.gridy = 6;
		frame.getContentPane().add(lblDtend, gbc_lblDtend);
		
		JDateChooser dateDtend = new JDateChooser();
		dateDtend.setDateFormatString("yyyy.MM.dd. hh:mm");
		GridBagConstraints gbc_dateDtend = new GridBagConstraints();
		gbc_dateDtend.fill = GridBagConstraints.BOTH;
		gbc_dateDtend.insets = new Insets(0, 0, 5, 5);
		gbc_dateDtend.gridx = 1;
		gbc_dateDtend.gridy = 7;
		frame.getContentPane().add(dateDtend, gbc_dateDtend);
		
		JButton btnSend = new JButton("Hozzáadás");
		btnSend.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnSend.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						String summary = textSummary.getText();
						String location = textLocation.getText();
						Date dtstart = dateDtstart.getDate();
						Date dtend = dateDtend.getDate();
						
						if(summary == null || location == null || dtstart == null || dtend == null) {
							JOptionPane.showMessageDialog(null, "Kitöltetlen mező!");
							textSummary.setText(null); textLocation.setText(null); dateDtstart.setDate(null); dateDtend.setDate(null);
						}
						else {
							System.out.println("NYers  "+ summary + " " +  location + " " + dtstart + " " + dtend);
							//CalendarItem item = new CalendarItem(getRandomUid(), dtstart, dtend, location, summary);
							//System.out.println("Előtte "+item.getLocation());
							returned.setDtend(dtend);
							returned.setLocation(location);
							returned.setSummary(summary);
							returned.setDtstart(dtstart);
							returned.setUid(getRandomUid());
							System.out.println("Returned2  "+returned.getLocation());
							frame.setVisible(false);
							CalendarFrame.main(null);
							}
					}
				});
			}
		});
		/*btnSend.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				String summary = textSummary.getText();
				String location = textLocation.getText();
				Date dtstart = dateDtstart.getDate();
				Date dtend = dateDtend.getDate();
				
				if(summary == null || location == null || dtstart == null || dtend == null) {
					JOptionPane.showMessageDialog(null, "Kitöltetlen mező!");
					textSummary.setText(null); textLocation.setText(null); dateDtstart.setDate(null); dateDtend.setDate(null);
				}
				else {
					System.out.println("NYers  "+ summary + " " +  location + " " + dtstart + " " + dtend);
					//CalendarItem item = new CalendarItem(getRandomUid(), dtstart, dtend, location, summary);
					//System.out.println("Előtte "+item.getLocation());
					returned.setDtend(dtend);
					returned.setLocation(location);
					returned.setSummary(summary);
					returned.setDtstart(dtstart);
					returned.setUid(getRandomUid());
					System.out.println("Returned2  "+returned.getLocation());
					frame.setVisible(false);
					CalendarFrame.main(null);
					}
				}
		});*/
		
		
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 5, 5);
		gbc_btnSend.fill = GridBagConstraints.VERTICAL;
		gbc_btnSend.gridx = 1;
		gbc_btnSend.gridy = 9;
		frame.getContentPane().add(btnSend, gbc_btnSend);
		System.out.println(returned);
	}
	
	protected String getRandomUid() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder rand = new StringBuilder();
        Random rnd = new Random();
        while (rand.length() < 12) { // length of the random string.
            int index = (int) (rnd.nextFloat() * chars.length());
            rand.append(chars.charAt(index));
        }
        String saltStr = rand.toString();
        return saltStr;

    }
	
    public CalendarItem getItem(){
    	return returned;
    };
    
    public void setItem(CalendarItem item) {
    	item = returned;
    }
}
