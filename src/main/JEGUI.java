package src.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

// the main GUI to run
public class JEGUI extends ExcelReader implements ActionListener {
    private static final String defaultPath = "./data/testFile.xlsx";

    private static String excelFilePath;
    private static final int FRAME_WIDTH = 500;
    private static final int FRAME_HEIGHT = 240;

    private JFrame frame;
    private JPanel panel;
    private JLabel fileLabel;
    private JButton fileChooserButton;

    private JLabel dateLabel;
    private JTextField dateField;
    private JLabel commentLabel;
    private JTextField commentField;
    private JLabel amountLabel;
    private JLabel debitLabel;
    private JLabel creditLabel;
    private JTextField amountField;
    private JComboBox<String> accOpDebit;
    private JComboBox<String> accOpCredit;
    private JButton confirmButton;
    private JLabel invalidInputLabel;
    private JLabel failedWriteLabel;
    private JLabel invalidFileLabel;

    // EFFECTS: constructs a Journal Entry GUI
    public JEGUI() {
        loadProperties();

        setupPanel();

        setupFrame();

        setupFileLabel();

        setupFileButton(); //new

        setupDateComment();

        setupAmount();

        setupDebitCredit();

        setupButton();

        setupWarningLabels();

        frame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: add excelFilePath as a label at top right corner of panel
    private void setupFileLabel() {
        String label = "Selected file: " + excelFilePath;
        fileLabel = new JLabel(label);
        fileLabel.setBounds(10, 2, 380, 25);
        panel.add(fileLabel);
    }

    // MODIFIES: this
    // EFFECTS: add and set up file chooser
    private void setupFileButton() {
        fileChooserButton = new JButton("Choose File");
        fileChooserButton.setBounds(400, 5, 100, 25);
        fileChooserButton.addActionListener(this);
        panel.add(fileChooserButton);
    }

    // MODIFIES: this
    // EFFECTS: add hidden warning labels to panel (will be visible if conditions met at button pressed)
    private void setupWarningLabels() {
        invalidInputLabel = new JLabel("Invalid input: double-check amount and accounts");
        invalidInputLabel.setBounds(10, 180, 350, 25);
        invalidInputLabel.setForeground(Color.red);
        panel.add(invalidInputLabel);
        invalidInputLabel.setVisible(false);

        failedWriteLabel = new JLabel("Failed to write: check file path");
        failedWriteLabel.setBounds(10, 180, 350, 25);
        failedWriteLabel.setForeground(Color.red);
        panel.add(failedWriteLabel);
        failedWriteLabel.setVisible(false);

        invalidFileLabel = new JLabel("Invalid file: select a .xlsx file");
        invalidFileLabel.setBounds(10, 180, 350, 25);
        invalidFileLabel.setForeground(Color.red);
        panel.add(invalidFileLabel);
        invalidFileLabel.setVisible(false);
    }

    // MODIFIES: this
    // EFFECTS: add confirmButton to the bottom right of panel
    private void setupButton() {
        confirmButton = new JButton("Confirm");
        confirmButton.setBounds(400, 180, 100, 25);
        confirmButton.addActionListener(this);
        panel.add(confirmButton);
    }

    // MODIFIES: this
    // EFFECTS: add label and combo box for debit and credit
    private void setupDebitCredit() {
        String[] accOptions;
        try {
            accOptions = readAccounts(excelFilePath).toArray(new String[0]);
            System.out.println(" > successfully read accounts");

            accOpDebit = new JComboBox<>(accOptions);
            accOpDebit.setBounds(170, 135, 130, 25);
            panel.add(accOpDebit);

            accOpCredit = new JComboBox<>(accOptions);
            accOpCredit.setBounds(300, 135, 130, 25);
            panel.add(accOpCredit);

        } catch (IOException e) {
            System.out.println(" > failed to read accounts");
        }

        debitLabel = new JLabel("Debit");
        debitLabel.setBounds(180, 110, 80, 25);
        panel.add(debitLabel);

        creditLabel = new JLabel("Credit");
        creditLabel.setBounds(310, 110, 80, 25);
        panel.add(creditLabel);
    }

    // MODIFIES: this
    // EFFECTS: update the combo box for the account options for this excel file
    private void updateAccountOptions() {
        String[] accOptions = null;
        try {
            accOptions = readAccounts(excelFilePath).toArray(new String[0]);
        } catch (IOException e) {
            System.out.println("failed to read accounts");
        }
        updateComboBox(accOpDebit, accOptions);
        updateComboBox(accOpCredit, accOptions);
    }

    private void updateComboBox(JComboBox<String> comboBox, String[] accOptions) {
        comboBox.removeAllItems();
        for (String option : accOptions) {
            comboBox.addItem(option);
        }
    }

    // MODIFIES: this
    // EFFECTS: add label and comment for amount
    private void setupAmount() {
        amountLabel = new JLabel("Amount");
        amountLabel.setBounds(55, 110, 80, 25);
        panel.add(amountLabel);
        amountField = new JTextField(20);
        amountField.setBounds(50, 135, 100, 25);
        panel.add(amountField);
    }

    // MODIFIES: this
    // EFFECTS: add label and fields for date and comment
    private void setupDateComment() {
        dateLabel = new JLabel("Date");
        dateLabel.setBounds(10, 40, 80, 25);
        panel.add(dateLabel);
        dateField = new JTextField(20);
        dateField.setBounds(100, 40, 165, 25);
        panel.add(dateField);

        commentLabel = new JLabel("Comment");
        commentLabel.setBounds(10, 70, 80, 25);
        panel.add(commentLabel);
        commentField = new JTextField();
        commentField.setBounds(100, 70, 300, 25);
        panel.add(commentField);
    }

    // MODIFIES: this
    // EFFECTS: setup frame
    private void setupFrame() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Journal Entry - revamp");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.add(panel, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: setup panel
    private void setupPanel() {
        panel = new JPanel();
        panel.setLayout(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == fileChooserButton) {
            invalidFileLabel.setVisible(false);
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                excelFilePath = selectedFile.getAbsolutePath();
                if (!excelFilePath.endsWith(".xlsx")) { // check that it's an excel file
                    invalidFileLabel.setVisible(true);
                } else {
                    this.updateAccountOptions();
                    fileLabel.setText("Selected file: " + excelFilePath);
                }
            }
        }
        else { // confirmButton pressed
            saveProperties();
            // reset labels
            invalidInputLabel.setVisible(false);
            failedWriteLabel.setVisible(false);

            try {
                writeJE();
            } catch (IOException ex) {
                failedWriteLabel.setVisible(true);
            } catch (Exception ex) {
                invalidInputLabel.setVisible(true);
            }
//            catch (InvalidInputException ex) {
//                invalidInputLabel.setVisible(true);
//            } catch (NumberFormatException ex) {
//                invalidInputLabel.setVisible(true);
//            }
        }
    }

    private void writeJE() throws InvalidInputException, NumberFormatException, IOException {
        // get user input
        String dateInput = dateField.getText();
        String commentInput = commentField.getText();
        String debitAcc = accOpDebit.getSelectedItem().toString();
        String creditAcc = accOpCredit.getSelectedItem().toString();

        if (dateField.getText().isBlank() || amountField.getText().isBlank() || debitAcc.equals(creditAcc)) {
            throw new InvalidInputException();
        }
        double amountInput = Double.parseDouble(amountField.getText());

        // make new JournalEntry
        JournalEntry je = new JournalEntry(excelFilePath, dateInput, commentInput, amountInput, debitAcc, creditAcc);
        je.writeJE();

        // everything is fine and JE written successfully
        new JEGUI();
        frame.dispose();
    }

    private void loadProperties() {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream("config.properties")) {
            properties.load(in);
            excelFilePath = properties.getProperty("excelFilePath", defaultPath);
        } catch (IOException e) {
            excelFilePath = defaultPath;
        }
    }

    private void saveProperties() {
        Properties properties = new Properties();
        properties.setProperty("excelFilePath", excelFilePath);
        try (FileOutputStream out = new FileOutputStream("config.properties")) {
            properties.store(out, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
