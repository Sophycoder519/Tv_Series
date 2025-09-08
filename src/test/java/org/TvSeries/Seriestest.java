
package org.TvSeries;

import org.example.Series;
import org.junit.jupiter.api.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class SeriesTest {

    // Keep originals so we can restore after each test
    private InputStream sysInBackup;
    private PrintStream sysOutBackup;

    // Capture console output
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUpStreams() {
        sysInBackup = System.in;
        sysOutBackup = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setIn(sysInBackup);
        System.setOut(sysOutBackup);
    }

    /** Build a user-typed multi-line input stream */
    private static ByteArrayInputStream input(String... lines) {
        StringBuilder sb = new StringBuilder();
        for (String l : lines) sb.append(l).append('\n');
        return new ByteArrayInputStream(sb.toString().getBytes());
    }

    private void clearOutput() {
        outContent.reset();
    }

    // -------------------- Tests per brief --------------------

    /** TestSearchSeries(): valid id returns the correct series details */
    @Test
    void TestSearchSeries() {
        Series s = new Series();

        // 1) Capture
        System.setIn(input("101", "Extreme Sports", "12", "10", "1"));
        s.resetInputForTest(System.in);
        s.CaptureSeries();

        clearOutput();

        // 2) Search
        System.setIn(input("101", "1"));
        s.resetInputForTest(System.in);
        s.SearchSeries();

        String out = outContent.toString();
        assertTrue(out.contains("SERIES ID: 101"));
        assertTrue(out.contains("SERIES NAME: Extreme Sports"));
        assertTrue(out.contains("SERIES AGE RESTRICTION: 12"));
        assertTrue(out.contains("SERIES NUMBER OF EPISODES: 10"));
    }

    /** TestSearchSeries_SeriesNotFound(): incorrect id reports not found */
    @Test
    void TestSearchSeries_SeriesNotFound() {
        Series s = new Series();

        System.setIn(input("999", "1"));
        s.resetInputForTest(System.in);
        s.SearchSeries();

        String out = outContent.toString();
        assertTrue(out.contains("Series with Series Id: 999 was not found!"));
    }

    /** TestUpdateSeries(): update existing series and verify new values */
    @Test
    void TestUpdateSeries() {
        Series s = new Series();

        // Capture initial
        System.setIn(input("101", "Extreme Sports", "12", "10", "1"));
        s.resetInputForTest(System.in);
        s.CaptureSeries();

        clearOutput();

        // Update
        System.setIn(input("101", "Extreme Sports 2025", "10", "12", "1"));
        s.resetInputForTest(System.in);
        s.UpdateSeries();

        clearOutput();

        // Verify by searching
        System.setIn(input("101", "1"));
        s.resetInputForTest(System.in);
        s.SearchSeries();

        String out = outContent.toString();
        assertTrue(out.contains("SERIES NAME: Extreme Sports 2025"));
        assertTrue(out.contains("SERIES AGE RESTRICTION: 10"));
        assertTrue(out.contains("SERIES NUMBER OF EPISODES: 12"));
    }

    /** TestDeleteSeries(): delete existing id and confirm it's gone */
    @Test
    void TestDeleteSeries() {
        Series s = new Series();

        // Capture first
        System.setIn(input("202", "Galactic Wars", "16", "8", "1"));
        s.resetInputForTest(System.in);
        s.CaptureSeries();

        clearOutput();

        // Delete (confirm 'y')
        System.setIn(input("202", "y", "1"));
        s.resetInputForTest(System.in);
        s.DeleteSeries();

        String outAfterDelete = outContent.toString();
        assertTrue(outAfterDelete.contains("Series with Series Id: 202 WAS deleted!"));

        clearOutput();

        // Verify not found
        System.setIn(input("202", "1"));
        s.resetInputForTest(System.in);
        s.SearchSeries();

        String out = outContent.toString();
        assertTrue(out.contains("Series with Series Id: 202 was not found!"));
    }

    /** TestDeleteSeries_SeriesNotFound(): deleting missing id shows not found and not deleted */
    @Test
    void TestDeleteSeries_SeriesNotFound() {
        Series s = new Series();

        System.setIn(input("404", "1"));
        s.resetInputForTest(System.in);
        s.DeleteSeries();

        String out = outContent.toString();
        assertTrue(out.contains("Series with Series Id: 404 was not found!"));
        assertFalse(out.contains("WAS deleted!"));
    }

    /** TestSeriesAgeRestriction_AgeValid(): valid ages (2..18) proceed without invalid-age message */
    @Test
    void TestSeriesAgeRestriction_AgeValid() {
        Series s = new Series();

        System.setIn(input("301", "Nature Docu", "18", "6", "1"));
        s.resetInputForTest(System.in);
        s.CaptureSeries();

        String out = outContent.toString();
        assertFalse(out.contains("You have entered a incorrect series age!!!"));
        assertTrue(out.contains("Series processed successfully!!!"));
    }

    /** TestSeriesAgeRestriction_SeriesAgeInValid(): non-numeric/out-of-range prompt for re-entry */
    @Test
    void TestSeriesAgeRestriction_SeriesAgeInValid() {
        Series s = new Series();

        // Age attempts: "Ten" (invalid), "32" (invalid), then "12" (valid)
        System.setIn(input("302", "Cartoon Max", "Ten", "32", "12", "20", "1"));
        s.resetInputForTest(System.in);
        s.CaptureSeries();

        String out = outContent.toString();
        assertTrue(out.contains("You have entered a incorrect series age!!!")); // shown at least once
        assertTrue(out.contains("Series processed successfully!!!"));
    }
}
