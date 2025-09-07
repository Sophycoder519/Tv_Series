package org.TvSeries;

import org.example.Series;
import org.example.SeriesModelClass;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

public class SeriesTest {

    private InputStream originalIn;
    private PrintStream originalOut;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        originalIn = System.in;
        originalOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void tearDown() throws Exception {
        System.setIn(originalIn);
        System.setOut(originalOut);
        outContent.close();
    }

    private Series newSeriesWithInput(String... lines) {
        String joined = String.join(System.lineSeparator(), lines) + System.lineSeparator();
        System.setIn(new ByteArrayInputStream(joined.getBytes(StandardCharsets.UTF_8)));
        return new Series(true);
    }

    private String console() {
        return outContent.toString(StandardCharsets.UTF_8);
    }



    @Test
    void TestSearchSeries() {
        Series s = newSeriesWithInput("S100");
        s.getSeriesList().add(new SeriesModelClass("S100", "Spacelands", "13", "12"));

        s.searchSeries();

        String out = console();
        assertTrue(out.contains("SERIES ID: S100"));
        assertTrue(out.contains("SERIES NAME: Spacelands"));
        assertTrue(out.contains("SERIES AGE RESTRICTION: 13"));
        assertTrue(out.contains("SERIES NUMBER OF EPISODES: 12"));
    }

    @Test
    void TestSearchSeries_SeriesNotFound() {
        Series s = newSeriesWithInput("NOPE-1");
        s.searchSeries();
        assertTrue(console().contains("Series with Series Id: NOPE-1 was not found!"));
    }

    @Test
    void TestUpdateSeries() {
        Series s = newSeriesWithInput("S200", "New Title", "16", "48");
        s.getSeriesList().add(new SeriesModelClass("S200", "Old Title", "12", "10"));

        s.updateSeries();

        SeriesModelClass updated = s.getSeriesList().getFirst();
        assertEquals("New Title", updated.getSeriesName());
        assertEquals("16", updated.getSeriesAge());
        assertEquals("48", updated.getSeriesNumberOfEpisodes());
        assertTrue(console().contains("Series updated successfully!"));
    }

    @Test
    void TestDeleteSeries() {
        Series s = newSeriesWithInput("S300", "y");
        s.getSeriesList().add(new SeriesModelClass("S300", "ToRemove", "15", "20"));

        s.deleteSeries();

        assertTrue(s.getSeriesList().isEmpty());
        assertTrue(console().contains("WAS deleted!"));
    }

    @Test
    void TestDeleteSeries_SeriesNotFound() {
        Series s = newSeriesWithInput("BAD-ID");
        s.getSeriesList().add(new SeriesModelClass("S400", "KeepMe", "14", "8"));

        s.deleteSeries();

        assertEquals(1, s.getSeriesList().size());
        assertEquals("S400", s.getSeriesList().getFirst().getSeriesId());
        assertTrue(console().contains("Series not found!"));
    }

    @Test
    void TestSeriesAgeRestriction_AgeValid() {
        Series s = newSeriesWithInput("S500", "ValidAgeShow", "10", "6");

        s.captureSeries();

        assertEquals(1, s.getSeriesList().size());
        assertEquals("10", s.getSeriesList().getFirst().getSeriesAge());
        assertFalse(console().contains("You have entered an incorrect series age!!!"));
    }

    @Test
    void TestSeriesAgeRestriction_SeriesAgeInValid() {
        Series s = newSeriesWithInput("S600", "InvalidAgeShow", "1", "19", "abc", "12", "22");

        s.captureSeries();

        String out = console();
        assertTrue(out.contains("You have entered an incorrect series age!!!"));
        assertEquals("12", s.getSeriesList().getFirst().getSeriesAge());
    }
}
