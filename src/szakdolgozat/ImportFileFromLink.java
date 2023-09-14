package szakdolgozat;

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

public class ImportFileFromLink {

	private JFrame frame;
	private JTextField textLink;

	/**
	 * Create the application.
	 * @return 
	 */
	public void run() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 654, 196);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textLink = new JTextField();
		textLink.setBounds(26, 73, 587, 26);
		frame.getContentPane().add(textLink);
		textLink.setColumns(10);
		
		JButton btnImport = new JButton("Importálás");
		btnImport.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String link = textLink.getText();
				DownloadFile(link);
				frame.setVisible(false);
				CalendarFrame cf = new CalendarFrame();
				cf.importCalendar();
				cf.main(null);
			}
		});
		btnImport.setBounds(277, 116, 85, 26);
		frame.getContentPane().add(btnImport);
		
		JLabel lblNewLabel = new JLabel("Illessze be a webes naptárakhoz használahtó linket a Neptunból");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblNewLabel.setBounds(137, 30, 365, 20);
		frame.getContentPane().add(lblNewLabel);
		frame.setVisible(true);
	}
	
	/**
	 * 
	 * @param Input link to download
	 */
	private void DownloadFile(String link) {
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
                    System.out.println("File downloaded successfully to: " + destinationPath);
                }
            }
        } catch (IOException e) {
            System.err.println("Error downloading file: " + e.getMessage());
        }
	}
}
