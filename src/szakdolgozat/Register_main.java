package szakdolgozat;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import java.awt.Toolkit;

public class Register_main extends Login_main{

	private JFrame frmRegistration;
	private static JTextField t_usrn;
	private JPasswordField t_passwd;
	private JTextField t_email;

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
		frmRegistration.setIconImage(Toolkit.getDefaultToolkit().getImage(Register_main.class.getResource("/szakdolgozat/calendar.png")));
		frmRegistration.setTitle("Regisztráció");
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
					if(registration(t_usrn.getText(), t_email.getText(), t_passwd.getText())) {
						Login_main.user = t_usrn.getText();
						frmRegistration.dispose();
						frmLogin.dispose();
						CalendarFrame.main(null);					
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
				if(registration(t_usrn.getText(), t_email.getText(), t_passwd.getText())) {
					Login_main.user = t_usrn.getText();
					frmRegistration.dispose();
					frmLogin.dispose();
					CalendarFrame.main(null);					
				}
			}
		});
		b_registration.setBounds(68, 244, 135, 32);
		frmRegistration.getContentPane().add(b_registration);
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblEmail.setBounds(214, 91, 48, 26);
		frmRegistration.getContentPane().add(lblEmail);
		
		t_email = new JTextField();
		t_email.setBounds(119, 130, 238, 26);
		frmRegistration.getContentPane().add(t_email);
		
		JButton b_login = new JButton("Bejelentkezés >>");
		b_login.addActionListener(new ActionListener() { //Back to the login window
			public void actionPerformed(ActionEvent e) {
				frmRegistration.dispose();
				frmLogin.setVisible(true);
			}
		});
		b_login.setBounds(271, 244, 135, 32);
		frmRegistration.getContentPane().add(b_login);
	}
	
	/**
	 * Uploads the user data to the database if not exist with password encryption
	 * @param name : User name
	 * @param email : User email
	 * @param passwd : User password
	 * @return True if the registration is successful
	 */
	private Boolean registration(String name, String email, String passwd) {
			Boolean success = true;
			try {
				if(isExist(name,email)==true) {
					JOptionPane.showMessageDialog(null, "Ez az email cím/felhasználónév már létezik!");
					t_usrn.setText(null);
					t_email.setText(null);
					t_passwd.setText(null);
					success = false;
				}
				else {
					if(emailValid(email)) {
						Argon2 argon2 = Argon2Factory.create();
						String hashedPassword = argon2.hash(10, 65536, 1, passwd);
						Connection connection = Func.connectToSql();
						String procedureCall = "{call InsertUserWithTable(?, ?, ?)}";
			    		CallableStatement callableStatement = connection.prepareCall(procedureCall);
			    		callableStatement.setString(1, name);
			    		callableStatement.setString(2, email);
			    		callableStatement.setString(3, hashedPassword);
			    		callableStatement.execute();
						connection.close();	
					}
					else {
						JOptionPane.showMessageDialog(null, "Helytelen email cím!");
						t_email.setText(null);
						success = false;
					}
				}
			} catch (SQLException e) {
				throw new IllegalStateException("Cannot connect the database!", e);
			}
			return success;
	}
	
	/**
	 * Checks the user is exist in the database
	 * @param name : Username
	 * @param email : Email
	 * @return	Boolean value if true the user is exist
	 */
	private Boolean isExist(String name, String email) {
			
			Boolean exist = false;
			try {
				Connection connection = Func.connectToSql();
				String procedureCall = "{call register(?, ?)}";
	    		CallableStatement callableStatement = connection.prepareCall(procedureCall);
				callableStatement.setString(1, name);
				callableStatement.setString(2, email);
				callableStatement.execute();
				ResultSet result = callableStatement.getResultSet();
				result.next();
				if(result.getRow() == 1) {
					exist = true;
				}
				connection.close();
			} catch (SQLException e) {
				throw new IllegalStateException("Cannot connect the database!", e);
			}
			return exist;
		}
	
	/**
	 * Validate the email address
	 * @param email : Email address to validate
	 * @return	True if the email have correct form
	 */
	private Boolean emailValid(String email) {
		String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
		Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
	}
	}
