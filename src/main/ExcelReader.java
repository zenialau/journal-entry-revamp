package src.main;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public abstract class ExcelReader {

    protected ArrayList<String> readAccounts(String excelFilePath) throws IOException {
        System.out.println(" > in readAccounts with excelFilePath: " + excelFilePath);

        FileInputStream inputStream = new FileInputStream(excelFilePath);
        System.out.println(" > inputStream: " + inputStream);

        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        System.out.println(" > workbook: " + workbook);

        int numOfSheets = workbook.getNumberOfSheets();

        ArrayList<String> accs = new ArrayList<>();

        for (int i = 0; i < numOfSheets; i++) {
            String sheetName = workbook.getSheetName(i);
            accs.add(sheetName);
        }
        return accs;
    }

}
