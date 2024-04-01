package src.main;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

// represents a single Journal Entry with a date, comment, two accounts to debit and credit, and the amount
public class JournalEntry {
    private String excelFilePath;
    private String date;
    private String comment;
    private double amount;
    private String accDebit;
    private String accCredit;

    // EFFECTS: constructs a Journal Entry with user inputted values
    public JournalEntry(String file, String date, String comment, double amount, String accDebit, String accCredit) {
        this.excelFilePath = file;
        this.date = date;
        this.comment = comment;
        this.amount = amount;
        this.accDebit = accDebit;
        this.accCredit = accCredit;
    }

    // MODIFIES: Excel file
    // EFFECTS: write current Journal Entry into excelFilePath
    public void writeJE() throws IOException {
        FileInputStream inputStream= new FileInputStream(excelFilePath);
        XSSFWorkbook workbook= new XSSFWorkbook(inputStream);

        writeDebitAccount(workbook);
        writeCreditAccount(workbook);

        // write to file
        inputStream.close();
        FileOutputStream outstream = new FileOutputStream(excelFilePath);
        workbook.write(outstream);
        outstream.close();
    }

    // MODIFIES: workbook
    // EFFECTS: write corresponding data into the sheet of account that is debited
    private void writeDebitAccount(XSSFWorkbook workbook) {
        XSSFSheet sheetDebit = null;
        sheetDebit = workbook.getSheet(accDebit);
        int newRowNum = sheetDebit.getLastRowNum() + 1;
        XSSFRow newRow = sheetDebit.createRow(newRowNum);
        XSSFCell dateCell = newRow.createCell(0);
        XSSFCell commentCell = newRow.createCell(1);
        XSSFCell debitCell = newRow.createCell(2);
        dateCell.setCellValue(date);
        commentCell.setCellValue(comment);
        debitCell.setCellValue(amount);
    }

    // MODIFIES: workbook
    // EFFECTS: write corresponding data into the sheet of account that is credited
    private void writeCreditAccount(XSSFWorkbook workbook) {
        XSSFSheet sheetCredit = null;
        sheetCredit = workbook.getSheet(accCredit);
        int newRowNum = sheetCredit.getLastRowNum() + 1;
        XSSFRow newRow = sheetCredit.createRow(newRowNum);
        XSSFCell dateCell = newRow.createCell(0);
        XSSFCell commentCell = newRow.createCell(1);
        XSSFCell creditCell = newRow.createCell(3);
        dateCell.setCellValue(date);
        commentCell.setCellValue(comment);
        creditCell.setCellValue(amount);
    }
}
