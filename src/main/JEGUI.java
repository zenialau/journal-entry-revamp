package src.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

// the main GUI to run
public class JEGUI extends ExcelReader implements ActionListener {
    private static final String excelFilePath = "./data/testFile.xlsx"; //!!! change later
    private static final int FRAME_WIDTH = 500;
    private static final int FRAME_HEIGHT = 240;

    private JFrame frame;
    private JPanel panel;
    private JLabel fileLabel;
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

    // EFFECTS: constructs a Journal Entry GUI
    public JEGUI() {
        setupPanel();

        setupFrame();

        setupFileLabel();

        setupDateComment();

        setupAmount();

        setupDebitCredit();

        setupButton();

        setupWarningLabels();

        frame.setVisible(true);
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
        String[] accOptions = null;
        try {
            accOptions = readAccounts(excelFilePath).toArray(new String[0]);
        } catch (IOException e) {
            System.out.println("failed to read accounts");
        }

        debitLabel = new JLabel("Debit");
        debitLabel.setBounds(180, 100, 80, 25);
        panel.add(debitLabel);
        accOpDebit = new JComboBox<>(accOptions);
        accOpDebit.setBounds(170, 125, 130, 25);
        panel.add(accOpDebit);

        creditLabel = new JLabel("Credit");
        creditLabel.setBounds(310, 100, 80, 25);
        panel.add(creditLabel);
        accOpCredit = new JComboBox<>(accOptions);
        accOpCredit.setBounds(300, 125, 130, 25);
        panel.add(accOpCredit);
    }

    // MODIFIES: this
    // EFFECTS: add label and comment for amount
    private void setupAmount() {
        amountLabel = new JLabel("Amount");
        amountLabel.setBounds(55, 100, 80, 25);
        panel.add(amountLabel);
        amountField = new JTextField(20);
        amountField.setBounds(50, 125, 100, 25);
        panel.add(amountField);
    }

    // MODIFIES: this
    // EFFECTS: add label and fields for date and comment
    private void setupDateComment() {
        dateLabel = new JLabel("Date");
        dateLabel.setBounds(10, 20, 80, 25);
        panel.add(dateLabel);
        dateField = new JTextField(20);
        dateField.setBounds(100, 20, 165, 25);
        panel.add(dateField);

        commentLabel = new JLabel("Comment");
        commentLabel.setBounds(10, 50, 80, 25);
        panel.add(commentLabel);
        commentField = new JTextField();
        commentField.setBounds(100, 50, 300, 25);
        panel.add(commentField);
    }

    // MODIFIES: this
    // EFFECTS: add excelFilePath as a label at top right corner of panel
    private void setupFileLabel() {
        fileLabel = new JLabel(excelFilePath);
        fileLabel.setBounds(370, 2, 120, 25);
        panel.add(fileLabel);
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
    public void actionPerformed(ActionEvent e) { // confirmButton pressed
        // reset labels
        invalidInputLabel.setVisible(false);
        failedWriteLabel.setVisible(false);

        try {
            writeJE();
        } catch (InvalidInputException ex) {
            invalidInputLabel.setVisible(true);
        } catch (NumberFormatException ex) {
            invalidInputLabel.setVisible(true);
        } catch (IOException ex) {
            failedWriteLabel.setVisible(true);
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


}
