package src.test;

import org.junit.jupiter.api.Test;
import src.main.JournalEntry;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

public class TestJournalEntry {

    @Test
    void testWriteDebit() {
        JournalEntry testJE = new JournalEntry("./data/testFile.xlsx", "Apr 1 - 3",
                "test 3 - debit and credit",
                10.53, "Capital", "Bank");
        try {
            testJE.writeJE();
        } catch (IOException e) {
            fail("IOException thrown but not expected.");
        }
    }
}
