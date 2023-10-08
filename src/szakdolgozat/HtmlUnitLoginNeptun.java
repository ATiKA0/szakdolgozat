package szakdolgozat;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class HtmlUnitLoginNeptun extends ImportFileFromLink{
	//private static CountDownLatch latch = new CountDownLatch(1);
	private static String captchaText = null;
	
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }
    
    private static void createAndShowGUI() {
    	try (WebClient webClient = new WebClient()) {
            // Disable JavaScript (optional, depending on the website)
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

            // Download and save the CAPTCHA image locally
            File captchaImageFile = new File("captcha.png");
            ImageIO.write(captchaImage.getImageReader().read(0), "png", captchaImageFile);

            // Display the CAPTCHA image to the user
            showCaptchaPopup(captchaImageFile);
            
            captchaImageFile.delete();

			
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
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void showCaptchaPopup(File captchaImageFile) {
        JDialog captchaDialog = new JDialog();
        captchaDialog.setTitle("CAPTCHA Popup");
        captchaDialog.setModal(true);
        captchaDialog.setSize(400, 200);
        captchaDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        try {
            // Load the CAPTCHA image using Swing
            Image captchaSwingImage = new ImageIcon(captchaImageFile.getAbsolutePath()).getImage();
            JLabel captchaLabel = new JLabel(new ImageIcon(captchaSwingImage));

            // Create an input field for the user to enter CAPTCHA text
            JTextField captchaTextField = new JTextField(20);
            JButton submitButton = new JButton("Submit");

            // Create a panel to hold the components
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(captchaLabel, BorderLayout.CENTER);
            panel.add(captchaTextField, BorderLayout.SOUTH);
            panel.add(submitButton, BorderLayout.EAST);

            captchaDialog.add(panel);
            captchaDialog.setLocationRelativeTo(null); // Center the popup window

            // Create a custom action listener to capture the user's input and close the dialog
            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    captchaText = captchaTextField.getText();
                    captchaDialog.dispose();
                }
            });

            captchaDialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

