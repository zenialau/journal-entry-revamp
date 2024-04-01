package src.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.main.ExcelReader;
import src.main.JEGUI;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.fail;

public class ExcelReaderTest extends ExcelReader {
    String testFile = "./data/testFile.xlsx";

    @Test
    void printAccounts() {
        JEGUI testGUI = new JEGUI();
        ArrayList<String> accounts = null;
        try {
            accounts = readAccounts(testFile);
        } catch (IOException e) {
            fail("IOException thrown but not expected.");
        }
        for (String s: accounts) {
            System.out.println(s);
        }
    }


}
