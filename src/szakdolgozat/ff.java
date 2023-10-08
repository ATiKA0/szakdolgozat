package szakdolgozat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ff {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Custom Image Input Dialog Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JButton showDialogButton = new JButton("Show Custom Dialog");
        showDialogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCustomDialog(frame);
                System.out.println("lolka");
            }
        });

        System.out.println("lol");
        frame.add(showDialogButton, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static void showCustomDialog(JFrame parentFrame) {
        JDialog dialog = new JDialog(parentFrame, "Custom Image Input Dialog", true);
        dialog.setSize(400, 200);

        JPanel panel = new JPanel(new BorderLayout());

        // Create a JLabel to display the image
        ImageIcon imageIcon = new ImageIcon("path_to_your_image.png"); // Replace with the path to your image
        JLabel imageLabel = new JLabel(imageIcon);

        // Create an input field
        JTextField inputField = new JTextField(20);

        // Create a button
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = inputField.getText();
                // Process the user input as needed
                System.out.println("User input: " + userInput);
                dialog.dispose(); // Close the dialog
            }
        });

        panel.add(imageLabel, BorderLayout.NORTH);
        panel.add(inputField, BorderLayout.CENTER);
        panel.add(okButton, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }
}