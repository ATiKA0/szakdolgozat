package szakdolgozat;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class ImportFileFromLink {

	private JFrame frame;
	protected static JPasswordField textPasswd;
	protected static JTextField textUsern;



	/**
	 * @wbp.parser.entryPoint
	 */
	public void run() {
		EventQueue.invokeLater(new ImportFileFromLink()::initialize);
	}


	/**
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 370, 381);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Felhasználónév");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(130, 81, 95, 26);
		frame.getContentPane().add(lblNewLabel);
		
		textPasswd = new JPasswordField();
		textPasswd.setBounds(82, 207, 191, 32);
		frame.getContentPane().add(textPasswd);
		textPasswd.setColumns(10);
		
		JButton btnLogin = new JButton("Bejelentkezés");
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnLogin.addActionListener(new ActionListener() {	//Launch the HTML Unit class to login to the Neptun
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				HtmlUnitLoginNeptun.main(null);
			}
		});
		
		textPasswd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {	//This do the same what the button do but with an "ENTER" button hit
				 if (e.getKeyCode()==KeyEvent.VK_ENTER){
						frame.dispose();
						HtmlUnitLoginNeptun.main(null);					
					}
			 	}
		});
		
		btnLogin.setBounds(115, 267, 125, 43);
		frame.getContentPane().add(btnLogin);
		
		textUsern = new JTextField();
		textUsern.setColumns(10);
		textUsern.setBounds(82, 117, 191, 32);
		frame.getContentPane().add(textUsern);
		
		JLabel lblJelsz = new JLabel("Jelszó");
		lblJelsz.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblJelsz.setBounds(153, 171, 50, 26);
		frame.getContentPane().add(lblJelsz);
		
		JLabel lblNewLabel_1 = new JLabel("Írja be a Neptun bejelentkezési adatait!");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(57, 28, 242, 20);
		frame.getContentPane().add(lblNewLabel_1);
		frame.setVisible(true);
	}
	
	
}
