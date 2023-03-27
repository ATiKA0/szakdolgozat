package szakdolgozat;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileFilter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Frame_main {

	private JFrame frame;
	private JTextField t_usrn;
	private String username;
	private String password;
	private JPasswordField t_passwd;
	protected String combo;

/**
 * Launch the application with the initialize method.
 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Frame_main() :: initialize);
	}

	private WebDriver login(String usr, String passwd){
		WebDriverManager.chromedriver().setup();
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();	//Create a hashmap for the chrome preferences
		chromePrefs.put("profile.default_content_settings.popups", 0);	//In the default profile disable popups
		chromePrefs.put("download.default_directory", System.getProperty("user.dir"));
		ChromeOptions options = new ChromeOptions();	//Creating a chrome option where we can change the settings of the chromedriver
		options.setExperimentalOption("prefs", chromePrefs);	//Add the hashmap to the chromedriver
		options.addArguments("--remote-allow-origins=*");
		options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);	//Set to accept SSL certifications
		options.addArguments("disable-infobars");	//Disabling the yellow chrome infobars
		options.addArguments("--headless");	//Run chrome in headless because we don't need to see it
		options.setAcceptInsecureCerts(true);	//If the site don't have an SSL certification accept the insecure site
		options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);	//If an chrome popup appears the chromedriver accept it 
		WebDriver driver = new ChromeDriver(options);	//Here creating the chromedriver with the given options
		WebDriverWait wait = new WebDriverWait(driver, 5);	//Here creating a wait element for the webdriver
		driver.get("https://neptun-web"+combo+".tr.pte.hu/hallgato/login.aspx");	//Open the neptun site with a choosable neptun server 
		WebElement login = driver.findElement(By.name("btnSubmit"));	//Searching on the site for the "Bejelentkezés" button
		if(login.isDisplayed()) {	//If the button is appeared on the site we know the site is loaded
			WebElement user = driver.findElement(By.name("user"));	//Finding the username field and write the username given by the user
			user.clear();
			user.sendKeys(usr);
			WebElement password = driver.findElement(By.name("pwd"));	//Do the same for the password
			password.clear();
			password.sendKeys(passwd);
			login.click();		//Click on the "Bejelentkezés" button
			try {	//A try-catch block for the Neptun "multiple login" popup, if it's not appear the catch block will run
				wait.until(ExpectedConditions.alertIsPresent());	//Here with the webdriverwait element we wait for 5 seconds for the popup
				Alert alert = driver.switchTo().alert();	//With this Alert element we accept the popup
				alert.accept();
				Thread.sleep(5000);	//Wait 5 sec to load the site
				/*
				 * Searching for the calendar export button on the site, if it's not there the site not loaded and the login is failed
				 * If the site not loaded the program resets the text fields and closing the chromedriver
				 */
				if(!driver.findElements(By.id("upBoxes_upCalendar_gdgCalendar_gdgCalendar_calendaroutlookexport")).isEmpty()){
					JOptionPane.showMessageDialog(null, "Sikeres bejelentkezés!");
					return driver;
				}
				else {
					JOptionPane.showMessageDialog(null, "Sikertelen bejelentkezés! Kérem próbálja újra!");
					driver.close();
					t_usrn.setText("");
					t_passwd.setText("");
				}
			}
			catch(Exception e){
				/*
				 * Searching for the calendar export button on the site, if it's not there the site not loaded and the login is failed
				 * If the site not loaded the program resets the text fields and closing the chromedriver
				 */
				if(!driver.findElements(By.id("upBoxes_upCalendar_gdgCalendar_gdgCalendar_calendaroutlookexport")).isEmpty()){
					JOptionPane.showMessageDialog(null, "Sikeres bejelentkezés!");
					return driver;
				}
				else {
					JOptionPane.showMessageDialog(null, "Sikertelen bejelentkezés! Kérem próbálja újra!");
					driver.close();
					t_usrn.setText("");
					t_passwd.setText("");
			}
		}
		}
		else{
			JOptionPane.showMessageDialog(null, "Az oldal nem elérhető! Kérem próbálja újra később!");
			driver.close();
			t_usrn.setText("");
			t_passwd.setText("");
		}
		return driver;
		
	}
	
/*
 * The getNewestFile method returns the newest ics file from the user's desktop direcctory
 * Ics is the calendar file what we can export from the Neptun
 */
	public static File getNewestFile() {
	    File theNewestFile = null;
	    File dir = new File(System.getProperty("user.dir"));
	    FileFilter fileFilter = new WildcardFileFilter("*." + "ics");
	    File[] files = dir.listFiles(fileFilter);

	    if (files.length > 0) {
	        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
	        theNewestFile = files[0];
	    }
	    return theNewestFile;
	}
/*
 * This export method is exporting the ics file from Neptun
 * In here the program run the login method, then clicks in the export calendar button
 * In the and closing the webdriver and wait a bit for the download 
 */
	private void export(String username, String password){
		WebDriver driver = login(username, password);
		WebDriverWait wait = new WebDriverWait(driver, 3);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("upBoxes_upCalendar_gdgCalendar_gdgCalendar_calendaroutlookexport")));
		WebElement cal = driver.findElement(By.id("upBoxes_upCalendar_gdgCalendar_gdgCalendar_calendaroutlookexport"));
		cal.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("calexport_lblLinkGmail")));
		String cale = driver.findElement(By.id("calexport_lblLinkGmail")).getText();
		driver.get(cale);
		try {
			TimeUnit.SECONDS.sleep(4);
			driver.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
/*
 * Initialize the contents of the frame.
 */
	/**
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		System.setProperty("file.encoding","UTF-8");
		frame = new JFrame();
		frame.setBounds(100, 100, 490, 334);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton b_login = new JButton("Bejelentkezés");
		b_login.setBounds(159, 199, 135, 32);
		b_login.addActionListener(new ActionListener() {	//If the user press the Login button the app gets the login data and change to the next window
			public void actionPerformed(ActionEvent e) {
				username = t_usrn.getText();
				password = t_passwd.getText();
				export(username, password);
				frame.setVisible(false);
				CalendarFrame cf = new CalendarFrame();
				cf.main(null);
			}
		});
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(b_login);
		
		t_usrn = new JTextField();
		t_usrn.setBounds(109, 50, 238, 26);
		frame.getContentPane().add(t_usrn);
		t_usrn.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Jelsz\u00F3");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblNewLabel.setBounds(210, 103, 84, 26);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblFelhasznlnv = new JLabel("Felhaszn\u00E1l\u00F3n\u00E9v");
		lblFelhasznlnv.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblFelhasznlnv.setBounds(176, 14, 135, 26);
		frame.getContentPane().add(lblFelhasznlnv);
		
		t_passwd = new JPasswordField();
		t_passwd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {	//This do the same what the button do but with an "ENTER" button hit
				 if (e.getKeyCode()==KeyEvent.VK_ENTER){
					username = t_usrn.getText();
					password = t_passwd.getText();
					export(username, password);
					frame.setVisible(false);
					CalendarFrame cf = new CalendarFrame();
					cf.main(null);
				 }
				 }
		});
		t_passwd.setEchoChar('*');
		t_passwd.setBounds(109, 153, 238, 26);
		frame.getContentPane().add(t_passwd);
		/*
		 *	This part is the combo box. Whit this we can change the Neptun server. 
		 */
		String[] choices = {"1", "2", "3", "4"};
		final JComboBox comboServer = new JComboBox(choices);
		comboServer.setBounds(126, 268, 38, 21);
		combo = comboServer.getSelectedItem().toString();
		frame.getContentPane().add(comboServer);
		
		JLabel lblServer = new JLabel("Neptun szerver:");
		lblServer.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblServer.setBounds(10, 270, 118, 13);
		frame.getContentPane().add(lblServer);
		frame.setVisible(true);
	}
	
	
}
