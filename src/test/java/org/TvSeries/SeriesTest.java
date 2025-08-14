package org.TvSeries;

import org.example.Series;
import org.example.SeriesModelClass;
import org.junit.jupiter.api.*;


import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class SeriesTest {

    private InputStream systemInBackup;
    private PrintStream systemOutBackup;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    void setUp() {
        systemInBackup = System.in;
        systemOutBackup = System.out;
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void tearDown() throws IOException {
        System.setIn(systemInBackup);
        System.setOut(systemOutBackup);
        testOut.close();
    }

    private void setInput(String... lines) {
        String joined = String.join(System.lineSeparator(), lines) + System.lineSeparator();
        System.setIn(new ByteArrayInputStream(joined.getBytes(StandardCharsets.UTF_8)));
    }

    private String out() {
        return testOut.toString(StandardCharsets.UTF_8);
    }

    private static String normalize(String s) {
        // Unify newlines and collapse trailing spaces to reduce brittle contains() checks
        return s.replace("\r\n", "\n").replace("\r", "\n").trim();
    }

    private static int countOccurrences(String haystack, String needle) {
        int count = 0, from = 0;
        while (true) {
            int idx = haystack.indexOf(needle, from);
            if (idx < 0) break;
            count++;
            from = idx + needle.length();
        }
        return count;
    }

    @Test
    void captureSeries_validInput_addsToList() {
        setInput("S1", "Breaking Bots", "16", "10");

        Series app = new Series(true);
        app.captureSeries();

        assertEquals(1, app.getSeriesList().size());
        SeriesModelClass s = app.getSeriesList().get(0);
        assertEquals("S1", s.getSeriesId());
        assertEquals("Breaking Bots", s.getSeriesName());
        assertEquals("16", s.getSeriesAge());
        assertEquals("10", s.getSeriesNumberOfEpisodes());
        assertTrue(normalize(out()).contains("Series processed successfully!!!"));
    }

    @Test
    void captureSeries_invalidAgeThenValid_showsErrorUntilValid() {
        setInput("S2", "Robo Chef", "abc", "1", "19", "5", "8");

        Series app = new Series(true);
        app.captureSeries();

        assertEquals(1, app.getSeriesList().size());
        assertEquals("5", app.getSeriesList().get(0).getSeriesAge());
        String output = out();
        int occurrences = countOccurrences(output, "You have entered an incorrect series age!!!");
        assertTrue(occurrences >= 3, "Expected age error message at least 3 times");
    }

    @Test
    void captureSeries_boundaryAgeMin_accepted() {
        setInput("S_MIN", "Min Age", "2", "1");

        Series app = new Series(true);
        app.captureSeries();

        assertEquals("2", app.getSeriesList().get(0).getSeriesAge());
    }

    @Test
    void captureSeries_boundaryAgeMax_accepted() {
        setInput("S_MAX", "Max Age", "18", "1");

        Series app = new Series(true);
        app.captureSeries();

        assertEquals("18", app.getSeriesList().get(0).getSeriesAge());
    }

    @Test
    void searchSeries_found_printsDetails() {
        Series app = new Series(true);
        app.getSeriesList().add(new SeriesModelClass("S3", "Code Noir", "13", "12"));
        setInput("S3");

        app.searchSeries();

        String output = normalize(out());
        assertTrue(output.contains("SERIES ID: S3"));
        assertTrue(output.contains("SERIES NAME: Code Noir"));
        assertTrue(output.contains("SERIES AGE RESTRICTION: 13"));
        assertTrue(output.contains("SERIES NUMBER OF EPISODES: 12"));
    }

    @Test
    void searchSeries_notFound_printsNotFound() {
        Series app = new Series(true);
        setInput("NOPE");

        app.searchSeries();

        assertTrue(normalize(out()).contains("Series with Series Id: NOPE was not found!"));
    }

    @Test
    void updateSeries_allValid_updatesAllFields() {
        Series app = new Series(true);
        app.getSeriesList().add(new SeriesModelClass("S4", "Initial", "10", "20"));

        setInput("S4", "Updated Name", "15", "22");
        app.updateSeries();

        SeriesModelClass s = app.getSeriesList().get(0);
        assertEquals("Updated Name", s.getSeriesName());
        assertEquals("15", s.getSeriesAge());
        assertEquals("22", s.getSeriesNumberOfEpisodes());
        assertTrue(normalize(out()).contains("Series updated successfully!"));
    }

    @Test
    void updateSeries_emptyInputs_keepExistingValues() {
        Series app = new Series(true);
        app.getSeriesList().add(new SeriesModelClass("S4b", "KeepAll", "9", "11"));

        // id, name(blank), age(blank), episodes(blank)
        setInput("S4b", "", "", "");
        app.updateSeries();

        SeriesModelClass s = app.getSeriesList().get(0);
        assertEquals("KeepAll", s.getSeriesName());
        assertEquals("9", s.getSeriesAge());
        assertEquals("11", s.getSeriesNumberOfEpisodes());
    }

    @Test
    void updateSeries_invalidAge_keepsPreviousAge() {
        Series app = new Series(true);
        app.getSeriesList().add(new SeriesModelClass("S5", "Keep Age", "12", "9"));

        setInput("S5", "", "50", "");
        app.updateSeries();

        SeriesModelClass s = app.getSeriesList().get(0);
        assertEquals("12", s.getSeriesAge(), "Age should remain unchanged on invalid input");
        assertTrue(normalize(out()).contains("Invalid age. Keeping previous value."));
    }

    @Test
    void updateSeries_notFound_printsMessage() {
        Series app = new Series(true);
        setInput("MISSING");

        app.updateSeries();

        assertTrue(normalize(out()).contains("Series not found!"));
    }

    @Test
    void deleteSeries_confirmYes_removesItem() {
        Series app = new Series(true);
        app.getSeriesList().add(new SeriesModelClass("S6", "ToDelete", "14", "6"));

        setInput("S6", "y");
        app.deleteSeries();

        assertTrue(app.getSeriesList().isEmpty());
        assertTrue(normalize(out()).contains("WAS deleted!"));
    }

    @Test
    void deleteSeries_confirmUppercaseY_removesItem_caseInsensitive() {
        Series app = new Series(true);
        app.getSeriesList().add(new SeriesModelClass("S6Y", "ToDelete", "14", "6"));

        setInput("S6Y", "Y");
        app.deleteSeries();

        assertTrue(app.getSeriesList().isEmpty());
    }

    @Test
    void deleteSeries_confirmNo_keepsItem() {
        Series app = new Series(true);
        app.getSeriesList().add(new SeriesModelClass("S7", "Keep", "14", "6"));

        setInput("S7", "n");
        app.deleteSeries();

        assertEquals(1, app.getSeriesList().size());
        assertTrue(normalize(out()).contains("Deletion cancelled."));
    }

    @Test
    void deleteSeries_notFound_printsMessage() {
        Series app = new Series(true);
        setInput("UNKNOWN");

        app.deleteSeries();

        assertTrue(normalize(out()).contains("Series not found!"));
    }

    @Test
    void seriesReport_empty_printsNoData() {
        Series app = new Series(true);

        app.seriesReport();

        assertTrue(normalize(out()).contains("No series data available."));
    }

    @Test
    void seriesReport_withData_printsAll() {
        Series app = new Series(true);
        app.getSeriesList().add(new SeriesModelClass("S8", "Shown", "16", "10"));
        app.getSeriesList().add(new SeriesModelClass("S9", "Also Shown", "8", "5"));

        app.seriesReport();

        String o = normalize(out());
        assertTrue(o.contains("SERIES REPORT - 2025"));
        assertTrue(o.contains("SERIES ID: S8"));
        assertTrue(o.contains("SERIES ID: S9"));
    }

    @Test
    void displayMenu_exitPath_printsExiting() {
        Series app = new Series(true);
        setInput("x");

        app.displayMenu();

        assertTrue(normalize(out()).contains("Exiting application..."));
    }

    @Test
    void displayMenu_invalidOption_thenExit() {
        // "1" enter main menu, "9" invalid, then "6" exit
        setInput("1", "9", "6");

        Series app = new Series(true);
        app.displayMenu();

        String o = normalize(out());
        assertTrue(o.contains("Invalid option. Please try again."));
        assertTrue(o.contains("Exiting application..."));
    }

    @Test
    void displayMenu_captureViaMenu_flowWorks() {
        setInput("1", "1", "M1", "Menu Show", "17", "3");

        Series app = new Series(true);
        app.displayMenu();

        assertEquals(1, app.getSeriesList().size());
        SeriesModelClass s = app.getSeriesList().get(0);
        assertEquals("M1", s.getSeriesId());
        assertEquals("Menu Show", s.getSeriesName());
        assertEquals("17", s.getSeriesAge());
        assertEquals("3", s.getSeriesNumberOfEpisodes());
    }
}
