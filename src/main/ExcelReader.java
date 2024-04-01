package src.main;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public abstract class ExcelReader {

    protected ArrayList<String> readAccounts(String excelFilePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(excelFilePath);

        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        int numOfSheets = workbook.getNumberOfSheets();

        ArrayList<String> accs = new ArrayList<String>();

        for (int i = 0; i < numOfSheets; i++) {
            String sheetName = workbook.getSheetName(i);
            accs.add(sheetName);
        }
        return accs;
    }

}
