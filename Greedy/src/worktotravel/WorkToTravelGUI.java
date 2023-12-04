package worktotravel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WorkToTravelGUI {

    private JFrame frame;
    private JTextField daysTextField;
    private int totalDays;
    private JPanel resultPanel;  // Declare resultPanel as an instance variable

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                WorkToTravelGUI window = new WorkToTravelGUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public WorkToTravelGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridBagLayout());

        JLabel lblHowManyDays = new JLabel("How many days will you work?");
        GridBagConstraints gbc_lblHowManyDays = new GridBagConstraints();
        gbc_lblHowManyDays.insets = new Insets(10, 10, 5, 5);
        gbc_lblHowManyDays.gridx = 1;
        gbc_lblHowManyDays.gridy = 1;
        frame.getContentPane().add(lblHowManyDays, gbc_lblHowManyDays);

        daysTextField = new JTextField();
        GridBagConstraints gbc_daysTextField = new GridBagConstraints();
        gbc_daysTextField.insets = new Insets(10, 0, 5, 5);
        gbc_daysTextField.gridx = 2;
        gbc_daysTextField.gridy = 1;
        frame.getContentPane().add(daysTextField, gbc_daysTextField);
        daysTextField.setColumns(10);

        JButton submitDaysButton = new JButton("Submit");
        submitDaysButton.addActionListener(e -> {
            totalDays = Integer.parseInt(daysTextField.getText());
            showWorkInputForm();
        });
        GridBagConstraints gbc_submitDaysButton = new GridBagConstraints();
        gbc_submitDaysButton.insets = new Insets(10, 0, 5, 10);
        gbc_submitDaysButton.gridx = 3;
        gbc_submitDaysButton.gridy = 1;
        frame.getContentPane().add(submitDaysButton, gbc_submitDaysButton);
    }

    private void showWorkInputForm() {
        frame.getContentPane().removeAll();  // Remove all components from the frame
        frame.repaint();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 5, 5);

        JLabel inputLabel = new JLabel("Enter work details for each day:");
        gbc.gridx = 2;
        gbc.gridy = 1;
        frame.getContentPane().add(inputLabel, gbc);

        JTextField[] workNameFields = new JTextField[totalDays];
        JTextField[] workDaysFields = new JTextField[totalDays];
        JTextField[] paymentFields = new JTextField[totalDays];

        for (int i = 1; i <= totalDays; i++) {
            workNameFields[i - 1] = new JTextField(10);
            workDaysFields[i - 1] = new JTextField(10);
            paymentFields[i - 1] = new JTextField(10);

            gbc.gridx = 1;
            gbc.gridy = i + 1;
            frame.getContentPane().add(new JLabel("Day " + i + ":"), gbc);

            gbc.gridx = 2;
            frame.getContentPane().add(new JLabel("Work Name:"), gbc);
            gbc.gridx = 3;
            frame.getContentPane().add(workNameFields[i - 1], gbc);

            gbc.gridx = 4;
            frame.getContentPane().add(new JLabel("Work Days:"), gbc);
            gbc.gridx = 5;
            frame.getContentPane().add(workDaysFields[i - 1], gbc);

            gbc.gridx = 6;
            frame.getContentPane().add(new JLabel("Payment:"), gbc);
            gbc.gridx = 7;
            frame.getContentPane().add(paymentFields[i - 1], gbc);
        }

        JButton finalSubmitButton = new JButton("Final Submit");
        finalSubmitButton.addActionListener(e -> {
            if (validateFields(workNameFields, workDaysFields, paymentFields)) {
                processFinalSubmit(workNameFields, workDaysFields, paymentFields);
            } else {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 2;
        gbc.gridy = totalDays + 2;
        gbc.gridwidth = 8;
        frame.getContentPane().add(finalSubmitButton, gbc);

        frame.pack();
    }

    private boolean validateFields(JTextField[] workNameFields, JTextField[] workDaysFields, JTextField[] paymentFields) {
        for (int i = 0; i < workNameFields.length; i++) {
            if (workNameFields[i].getText().isEmpty() || workDaysFields[i].getText().isEmpty() || paymentFields[i].getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void processFinalSubmit(JTextField[] workNameFields, JTextField[] workDaysFields, JTextField[] paymentFields) {
        // Create arrays to store the input values
        String[] workNames = new String[totalDays];
        int[] workDays = new int[totalDays];
        int[] payments = new int[totalDays];

        // Process the input for final submission
        for (int i = 0; i < totalDays; i++) {
            workNames[i] = workNameFields[i].getText();
            workDays[i] = Integer.parseInt(workDaysFields[i].getText());
            payments[i] = Integer.parseInt(paymentFields[i].getText());
        }

        // Display the final result
        StringBuilder resultText = new StringBuilder("Optimal Work Sequence:\n");
        int totalPayment = findOptimalWorkSequence(workNames, workDays, payments, 0, resultText);

        resultText.append("\nMaximum Payment: ").append(totalPayment);

        // Show the result in a new panel
        showResultPanel(resultText.toString());
    }

    private int findOptimalWorkSequence(String[] workNames, int[] workDays, int[] payments, int currentDay, StringBuilder sequence) {
        if (currentDay >= workNames.length) {
            return 0;  // Return 0 for the total payment when there are no more days
        }

        if (currentDay > 0) {
            sequence.append("\n");  // Add line break
        }

        // Check if the current work item exceeds the total days
        if (currentDay + workDays[currentDay] <= totalDays) {
            sequence.append(workNames[currentDay]).append(" - Work Days: ").append(workDays[currentDay]).append(", Payment: ").append(payments[currentDay]);

            // Find the next available day after completing the current work
            int nextAvailableDay = currentDay + 1;
            while (nextAvailableDay < workNames.length && nextAvailableDay < currentDay + workDays[currentDay]) {
                nextAvailableDay++;
            }

            // Recursively find the total payment for the remaining days
            int remainingPayment = findOptimalWorkSequence(workNames, workDays, payments, nextAvailableDay, sequence);

            // Return the total payment including the current day's payment
            return payments[currentDay] + remainingPayment;
        } else {
            // Skip the current work item if it exceeds the total days
            return findOptimalWorkSequence(workNames, workDays, payments, currentDay + 1, sequence);
        }
    }


    private void showResultPanel(String resultText) {
        if (resultPanel != null) {
            frame.getContentPane().remove(resultPanel);
        }

        resultPanel = new JPanel();
        resultPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JTextArea resultTextArea = new JTextArea(resultText);
        resultTextArea.setEditable(false);
        resultTextArea.setLineWrap(true);
        resultTextArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        gbc.gridx = 0;
        gbc.gridy = 0;
        resultPanel.add(scrollPane, gbc);

        frame.getContentPane().add(resultPanel);
        frame.pack();
    }
}
