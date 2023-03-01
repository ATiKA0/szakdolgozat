package szakdolgozat;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
	
	private Boolean isExist(String name, String passwd) {
		Connection connection;
		Boolean exist;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/orarend",
	                "root", "");
			System.out.println("Database connected!");
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("SELECT id FROM login WHERE name = '"+name+"' && passwd = '"+passwd+"';");
			result.next();
			exist = (result.getRow() == 1) ? true :false;
			statement.close();
			connection.close();
		} catch (SQLException | ClassNotFoundException e) {
			throw new IllegalStateException("Cannot connect the database!", e);
		}
		return exist;
	}
	
	private void registration(String name, String passwd) {
		Connection connection;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/orarend",
	                "root", "");
			System.out.println("Database connected!");
			if(isExist(name,passwd)==true) JOptionPane.showMessageDialog(null, "Ez a felhasználó már létezik!");
			String quary1 = "INSERT INTO `login` (`name`, `passwd`) VALUES ('"+name+"', '"+passwd+"');";
			String quary2 = "CREATE TABLE `orarend`.`"+name+"` (`uid` VARCHAR(40) NOT NULL , `summary` TINYTEXT NOT NULL , `location` TINYTEXT NOT NULL , `startdate` VARCHAR(17) NOT NULL , `enddate` VARCHAR(17) NOT NULL , PRIMARY KEY (`uid`));";
			Statement state = connection.createStatement();
			state.addBatch(quary1);
			state.addBatch(quary2);
			state.executeBatch();
			state.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static String getUsrn() {
		return t_usrn.getText();
	}
}
