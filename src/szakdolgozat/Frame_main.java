package szakdolgozat;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
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

public class Frame_main {

	private JFrame frame;
	private JTextField t_usrn;
	public String username;
	public String password;
	private JPasswordField t_passwd;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame_main window = new Frame_main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Frame_main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @param passwd 
	 * @param usr 
	 * @throws InterruptedException 
	 */
	private WebDriver login(String usr, String passwd){
		System.setProperty("webdriver.chrome.driver",".\\src\\szakdolgozat\\ChromeDriver\\chromedriver.exe");
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		options.addArguments("disable-infobars");
		options.addArguments("--headless");
		options.setAcceptInsecureCerts(true);
		options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
		WebDriver driver = new ChromeDriver(options);
		WebDriverWait wait = new WebDriverWait(driver, 5);
		String url = null;
		driver.get("https://neptun-web3.tr.pte.hu/hallgato/login.aspx");
		WebElement login = driver.findElement(By.name("btnSubmit"));
		if(login.isDisplayed()) {
			//http://atika00707.dynu.net:8080/
			WebElement user = driver.findElement(By.name("user"));
			user.clear();
			user.sendKeys(usr);
			WebElement password = driver.findElement(By.name("pwd"));
			password.clear();
			password.sendKeys(passwd);
			login.click();
			try {
				wait.until(ExpectedConditions.alertIsPresent());
				Alert alert = driver.switchTo().alert();
				alert.accept();
				Thread.sleep(5000);
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
	
	public static File getNewestFile() {
	    File theNewestFile = null;
	    File dir = new File("C:/Users/" + System.getProperty("user.name") + "/Downloads/");
	    FileFilter fileFilter = new WildcardFileFilter("*." + "ics");
	    File[] files = dir.listFiles(fileFilter);

	    if (files.length > 0) {
	        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
	        theNewestFile = files[0];
	    }
	    return theNewestFile;
	}
	
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
	
	private void initialize() {
		System.setProperty("file.encoding","UTF-8");
		frame = new JFrame();
		frame.setBounds(100, 100, 490, 334);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton b_login = new JButton("Bejelentkezés");
		b_login.setBounds(159, 231, 135, 32);
		b_login.addActionListener(new ActionListener() {
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
			public void keyPressed(KeyEvent e) {
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
		t_passwd.setBounds(109, 174, 238, 26);
		frame.getContentPane().add(t_passwd);
	}
}
