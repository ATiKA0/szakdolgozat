package szakdolgozat;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import java.awt.Font;
import javax.swing.SwingConstants;

/**
 * Logs in to the Neptun with HTML Unit and extracts the link for the calendar
 * @author gluck
 */
public class HtmlUnitLoginNeptun extends ImportFileFromLink{
	private static String captchaText = null;
	
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }
    
    /**
     * @wbp.parser.entryPoint
     */
    private static void createAndShowGUI() {
    	try (WebClient webClient = new WebClient()) {
            // Enable JavaScript
            webClient.getOptions().setJavaScriptEnabled(true);
            
            // Set a custom alert handler to automatically accept alerts
            webClient.setAlertHandler(new AlertHandler() {
                @Override
                public void handleAlert(Page page, String message) {
                    // Automatically accept the alert (click "OK")
                    System.out.println("Alert message: " + message);
                    page.getEnclosingWindow().getJobManager().removeAllJobs();
                }
            });

            // Navigate to the login page
            HtmlPage loginPage = webClient.getPage("https://neptun-web2.tr.pte.hu/hallgato/login.aspx");

            // Find the CAPTCHA image element
            HtmlImage captchaImage = loginPage.getFirstByXPath("//img[@alt='captchaImage']");

            // Display the CAPTCHA image to the user
            showCaptchaPopup(captchaImage);
			
            // Find the username and password input fields and the login button
            HtmlTextInput usernameField = loginPage.getFirstByXPath("//input[@name='user']");
            HtmlPasswordInput passwordField = loginPage.getFirstByXPath("//input[@name='pwd']");
            HtmlTextInput captchaField = loginPage.getFirstByXPath("//input[@name='cap']");
            HtmlButtonInput loginButton = (HtmlButtonInput) loginPage.getFirstByXPath("//input[@name='btnSubmit']");

            // Fill in the login credentials and CAPTCHA text
            usernameField.type(ImportFileFromLink.textUsern.getText());
            passwordField.type(ImportFileFromLink.textPasswd.getText());
            captchaText = captchaText.trim(); // Remove any leading/trailing spaces
            captchaField.type(captchaText);
            
            System.out.println(captchaText);

            // Submit the login form
            HtmlPage loggedInPage = loginButton.click();
            
            loggedInPage = webClient.getPage("https://neptun-web2.tr.pte.hu/hallgato/main.aspx?ctrl=0104");
            
            HtmlSpan spanElement = loggedInPage.getFirstByXPath("//span[@id='calexport_lblLinkGmail']");
            
            String spanText = spanElement.getTextContent();
            
            DownloadFile(spanText);
            CalendarFrame calendar = new CalendarFrame();
            calendar.importCalendar();
            calendar.main(null);
            
        } catch (Exception e) {
        	CalendarFrame calendar = new CalendarFrame();
        	calendar.display();
        	e.printStackTrace();
        	JOptionPane.showMessageDialog(null, "Váratlan hiba történet! Kérem próbálja újra!");
        }
    }
    
    /**
     * Show a popup window withc the captcha picture to the user to solve it
     * @param captchaImageFile : File for the local captcha image
     */
    private static void showCaptchaPopup(HtmlImage captchaImage) {
        JDialog captchaDialog = new JDialog();
        captchaDialog.setTitle("CAPTCHA Popup");
        captchaDialog.setModal(true);
        captchaDialog.setSize(400, 200);
        captchaDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        try {
            // Load the CAPTCHA image
        	ImageIcon captchaIcon = new ImageIcon(captchaImage.getImageReader().read(0));
            JLabel captchaLabel = new JLabel();
            captchaLabel.setHorizontalAlignment(SwingConstants.CENTER);
            captchaLabel.setIcon(captchaIcon);
            

            // Create an input field for the user to enter CAPTCHA text
            JTextField captchaTextField = new JTextField(20);
            JButton submitButton = new JButton("Rendben");

            // Create a panel to hold the components
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(captchaLabel, BorderLayout.CENTER);
            panel.add(captchaTextField, BorderLayout.SOUTH);
            panel.add(submitButton, BorderLayout.EAST);

            captchaDialog.getContentPane().add(panel);
            
            JLabel lblNewLabel = new JLabel("Kérem írja be a CAPTCHA-t");
            lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
            lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
            panel.add(lblNewLabel, BorderLayout.NORTH);
            captchaDialog.setLocationRelativeTo(null); // Center the popup window

            // Create a custom action listener to capture the user's input and close the dialog
            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    captchaText = captchaTextField.getText();
                    captchaDialog.dispose();
                }
            });

            captchaDialog.revalidate();
            captchaLabel.revalidate();
            captchaLabel.repaint();
            captchaDialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
	 * Downloads the ics file from the link extracted from Neptun
	 * @param link : Input link to download
	 */
	protected static void DownloadFile(String link) {
		try {
            // Create a URL object from the provided file URL
            URL url = new URL(link);

            // Open a connection to the URL
            try (InputStream in = url.openStream()) {
                // Extract the file name from the URL
                String fileName = Paths.get(url.getPath()).getFileName().toString();

                // Create the destination directory if it doesn't exist
                Path directory = Paths.get(System.getProperty("user.dir"));

                // Create the full path for the downloaded file
                Path destinationPath = directory.resolve(fileName);

                // Open a FileOutputStream to save the file
                try (OutputStream out = new FileOutputStream(destinationPath.toFile())) {
                    // Transfer data from the URL input stream to the file output stream
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error downloading file: " + e.getMessage());
        }
	}
}

