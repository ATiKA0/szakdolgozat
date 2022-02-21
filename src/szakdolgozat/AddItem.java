package szakdolgozat;

import java.awt.EventQueue;
import java.util.Date;

import javax.swing.JFrame;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import java.awt.Component;
import javax.swing.Box;
import com.toedter.components.JLocaleChooser;
import com.toedter.components.JSpinField;

public class AddItem {
	
	String Uid;
	Date Dtstart;
	Date Dtend;
	String Location;
	String Summary;
	private JTextField textSummary;
	private JTextField textField;

	public static void main(String[] args) {
		EventQueue.invokeLater(new AddItem()::initialize);
	}


	/**
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		JFrame frame  = new JFrame();
		frame.setResizable(false);
		frame.setTitle("Új bejegyzés");
		frame.setBounds(100, 100, 360, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{65, 212, 65, 0};
		gridBagLayout.rowHeights = new int[]{55, 36, 50, 36, 50, 36, 50, 36, 0, 36, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JLabel lblSummary = new JLabel("Leírás");
		GridBagConstraints gbc_lblSummary = new GridBagConstraints();
		gbc_lblSummary.anchor = GridBagConstraints.SOUTH;
		gbc_lblSummary.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummary.gridx = 1;
		gbc_lblSummary.gridy = 0;
		frame.getContentPane().add(lblSummary, gbc_lblSummary);
		
		textSummary = new JTextField();
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
		
		textField = new JTextField();
		textField.setColumns(10);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 3;
		frame.getContentPane().add(textField, gbc_textField);
		
		JLabel lblDtstart = new JLabel("Kezdés ideje");
		GridBagConstraints gbc_lblDtstart = new GridBagConstraints();
		gbc_lblDtstart.anchor = GridBagConstraints.SOUTH;
		gbc_lblDtstart.insets = new Insets(0, 0, 5, 5);
		gbc_lblDtstart.gridx = 1;
		gbc_lblDtstart.gridy = 4;
		frame.getContentPane().add(lblDtstart, gbc_lblDtstart);
		
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("y. MMM d. hh:mm");
		GridBagConstraints gbc_dateChooser = new GridBagConstraints();
		gbc_dateChooser.fill = GridBagConstraints.BOTH;
		gbc_dateChooser.insets = new Insets(0, 0, 5, 5);
		gbc_dateChooser.gridx = 1;
		gbc_dateChooser.gridy = 5;
		frame.getContentPane().add(dateChooser, gbc_dateChooser);
		
		JLabel lblDtend = new JLabel("Befejezés");
		GridBagConstraints gbc_lblDtend = new GridBagConstraints();
		gbc_lblDtend.anchor = GridBagConstraints.SOUTH;
		gbc_lblDtend.insets = new Insets(0, 0, 5, 5);
		gbc_lblDtend.gridx = 1;
		gbc_lblDtend.gridy = 6;
		frame.getContentPane().add(lblDtend, gbc_lblDtend);
		
		JDateChooser dateChooser_1 = new JDateChooser();
		dateChooser_1.setDateFormatString("y. MMM d. hh:mm");
		GridBagConstraints gbc_dateChooser_1 = new GridBagConstraints();
		gbc_dateChooser_1.fill = GridBagConstraints.BOTH;
		gbc_dateChooser_1.insets = new Insets(0, 0, 5, 5);
		gbc_dateChooser_1.gridx = 1;
		gbc_dateChooser_1.gridy = 7;
		frame.getContentPane().add(dateChooser_1, gbc_dateChooser_1);
		
		JButton btnNewButton = new JButton("New button");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.fill = GridBagConstraints.VERTICAL;
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 9;
		frame.getContentPane().add(btnNewButton, gbc_btnNewButton);
	}
}
