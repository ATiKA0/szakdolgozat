package szakdolgozat;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Register_main extends Login_main{

	private JFrame frmRegistration;
	private static JTextField t_usrn;
	private JPasswordField t_passwd;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Register_main window = new Register_main();
					window.frmRegistration.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Register_main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		System.setProperty("file.encoding","UTF-8");
		frmRegistration = new JFrame();
		frmRegistration.setTitle("Bejelentkezés");
		frmRegistration.setBounds(100, 100, 490, 334);
		frmRegistration.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRegistration.getContentPane().setLayout(null);
		
		t_usrn = new JTextField();
		t_usrn.setBounds(119, 52, 238, 26);
		frmRegistration.getContentPane().add(t_usrn);
		t_usrn.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Jelsz\u00F3");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblNewLabel.setBounds(214, 169, 48, 26);
		frmRegistration.getContentPane().add(lblNewLabel);
		
		JLabel lblFelhasznlnv = new JLabel("Felhaszn\u00E1l\u00F3n\u00E9v");
		lblFelhasznlnv.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblFelhasznlnv.setBounds(179, 13, 117, 26);
		frmRegistration.getContentPane().add(lblFelhasznlnv);
		
		t_passwd = new JPasswordField();
		t_passwd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {	//This do the same what the button do but with an "ENTER" button hit
				 if (e.getKeyCode()==KeyEvent.VK_ENTER){
					 if(isExist(t_usrn.getText(),t_passwd.getText())) {
						 frmRegistration.dispose();
						 CalendarFrame.main(null);
					 }
					 else {
						 JOptionPane.showMessageDialog(null, "Nincs ilyen felhasználó!\n"+"Kérem regisztráljon!");
					 }
				 }
				 }
		});
		t_passwd.setEchoChar('*');
		t_passwd.setBounds(119, 208, 238, 26);
		frmRegistration.getContentPane().add(t_passwd);
		
		JButton b_registration = new JButton("Regisztráció");
		b_registration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		//Registration button
				registration(t_usrn.getText(),t_passwd.getText());
				frmRegistration.dispose();
				CalendarFrame.main(null);
			}
		});
		b_registration.setBounds(170, 247, 135, 32);
		frmRegistration.getContentPane().add(b_registration);
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblEmail.setBounds(214, 91, 48, 26);
		frmRegistration.getContentPane().add(lblEmail);
		
		passwordField = new JPasswordField();
		passwordField.setEchoChar('*');
		passwordField.setBounds(119, 130, 238, 26);
		frmRegistration.getContentPane().add(passwordField);
	}
	}
