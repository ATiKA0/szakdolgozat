package szakdolgozat;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.bouncycastle.crypto.generators.BCrypt;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class Login_main {

	private JFrame frmBejelentkezs;
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
					window.frmBejelentkezs.setVisible(true);
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
		frmBejelentkezs = new JFrame();
		frmBejelentkezs.setTitle("Bejelentkezés");
		frmBejelentkezs.setBounds(100, 100, 490, 334);
		frmBejelentkezs.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton b_login = new JButton("Bejelentkezés");
		b_login.setBounds(68, 194, 135, 32);
		b_login.addActionListener(new ActionListener() {	//Login button
			public void actionPerformed(ActionEvent e) {
				if(isExist(t_usrn.getText(),t_passwd.getText())) {
					frmBejelentkezs.dispose();
					CalendarFrame.main(null);
				}
				else {
					JOptionPane.showMessageDialog(null, "Nincs ilyen felhasználó!\n"+"Kérem regisztráljon!");
				}
			}
		});
		frmBejelentkezs.getContentPane().setLayout(null);
		frmBejelentkezs.getContentPane().add(b_login);
		
		t_usrn = new JTextField();
		t_usrn.setBounds(118, 62, 238, 26);
		frmBejelentkezs.getContentPane().add(t_usrn);
		t_usrn.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Jelsz\u00F3");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblNewLabel.setBounds(213, 106, 48, 26);
		frmBejelentkezs.getContentPane().add(lblNewLabel);
		
		JLabel lblFelhasznlnv = new JLabel("Felhaszn\u00E1l\u00F3n\u00E9v");
		lblFelhasznlnv.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblFelhasznlnv.setBounds(179, 18, 117, 26);
		frmBejelentkezs.getContentPane().add(lblFelhasznlnv);
		
		t_passwd = new JPasswordField();
		t_passwd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {	//This do the same what the button do but with an "ENTER" button hit
				 if (e.getKeyCode()==KeyEvent.VK_ENTER){
					 if(isExist(t_usrn.getText(),t_passwd.getText())) {
						 frmBejelentkezs.dispose();
						 CalendarFrame.main(null);
					 }
					 else {
						 JOptionPane.showMessageDialog(null, "Nincs ilyen felhasználó!\n"+"Kérem regisztráljon!");
					 }
				 }
				 }
		});
		t_passwd.setEchoChar('*');
		t_passwd.setBounds(118, 150, 238, 26);
		frmBejelentkezs.getContentPane().add(t_passwd);
		
		JButton b_registration = new JButton("Regisztráció");
		b_registration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		//Registration button
				registration(t_usrn.getText(),t_passwd.getText());
				frmBejelentkezs.dispose();
				CalendarFrame.main(null);
			}
		});
		b_registration.setBounds(271, 194, 135, 32);
		frmBejelentkezs.getContentPane().add(b_registration);
	}
	
	public Boolean isExist(String name, String passwd) {
		
		Boolean exist = false;
		Argon2 argon2 = Argon2Factory.create();
		try {
			Connection connection = Func.connectToSql();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM login WHERE name = '"+name+"' LIMIT 1;");
			result.next();
			if(result.getRow() == 1) {
				String pass = result.getString(3);
				exist = argon2.verify(pass, passwd);
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			throw new IllegalStateException("Cannot connect the database!", e);
		}
		return exist;
	}
	
	private void registration(String name, String passwd) {
		
		try {
			if(isExist(name,passwd)==true) {
				JOptionPane.showMessageDialog(null, "Ez a felhasználó már létezik, most be lesz léptetve!");
			}
			else {
				String email = "";
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static String getUsrn() {
		return t_usrn.getText();
	}
}
