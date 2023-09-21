package szakdolgozat;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import java.awt.Toolkit;

public class Login_main {

	protected static String user;
	protected JFrame frmLogin;
	private static JTextField t_usrn;
	private JPasswordField t_passwd;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login_main window = new Login_main();
					window.frmLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Login_main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		System.setProperty("file.encoding","UTF-8");
		frmLogin = new JFrame();
		frmLogin.setIconImage(Toolkit.getDefaultToolkit().getImage(Login_main.class.getResource("/szakdolgozat/calendar.png")));
		frmLogin.setTitle("Bejelentkezés");
		frmLogin.setBounds(100, 100, 490, 334);
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton b_login = new JButton("Bejelentkezés");
		b_login.setBounds(73, 218, 135, 32);
		b_login.addActionListener(new ActionListener() {	//Login button
			public void actionPerformed(ActionEvent e) {
				if(isValid(t_usrn.getText(),t_passwd.getText())) {
					user = t_usrn.getText();
					frmLogin.dispose();
					CalendarFrame.main(null);
				}
				else {
					t_usrn.setText(null);
					t_passwd.setText(null);
					JOptionPane.showMessageDialog(null, "Hibás bejelentkezési adatok!");
				}
			}
		});
		frmLogin.getContentPane().setLayout(null);
		frmLogin.getContentPane().add(b_login);
		
		t_usrn = new JTextField();
		t_usrn.setBounds(118, 62, 238, 26);
		frmLogin.getContentPane().add(t_usrn);
		t_usrn.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Jelsz\u00F3");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblNewLabel.setBounds(213, 106, 48, 26);
		frmLogin.getContentPane().add(lblNewLabel);
		
		JLabel lblFelhasznlnv = new JLabel("Felhaszn\u00E1l\u00F3n\u00E9v");
		lblFelhasznlnv.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblFelhasznlnv.setBounds(179, 18, 117, 26);
		frmLogin.getContentPane().add(lblFelhasznlnv);
		
		t_passwd = new JPasswordField();
		t_passwd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {	//This do the same what the button do but with an "ENTER" button hit
				 if (e.getKeyCode()==KeyEvent.VK_ENTER){
					 if(isValid(t_usrn.getText(),t_passwd.getText())) {
						 user = t_usrn.getText();
						 frmLogin.dispose();
						 CalendarFrame.main(null);
					 }
					 else {
						 t_usrn.setText(null);
						 t_passwd.setText(null);
						 JOptionPane.showMessageDialog(null, "Hibás bejelentkezési adatok!");
					 }
				 }
				 }
		});
		t_passwd.setEchoChar('*');
		t_passwd.setBounds(118, 150, 238, 26);
		frmLogin.getContentPane().add(t_passwd);
		
		JButton b_registration = new JButton("Regisztráció >>");
		b_registration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		//Registration button
				frmLogin.setVisible(false);
				Register_main.main(null);
			}
		});
		b_registration.setBounds(276, 218, 135, 32);
		frmLogin.getContentPane().add(b_registration);
	}
	
	private Boolean isValid(String name, String passwd) {
		
		Boolean valid = false;
		Argon2 argon2 = Argon2Factory.create();
		try {
			Connection connection = Func.connectToSql();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM login WHERE name = '"+name+"' LIMIT 1;");
			result.next();
			if(result.getRow() == 1) {
				String pass = result.getString(3);
				valid = argon2.verify(pass, passwd);
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			throw new IllegalStateException("Cannot connect the database!", e);
		}
		return valid;
	}
	
	public static String getUsrn() {
		return user;
	}
}
