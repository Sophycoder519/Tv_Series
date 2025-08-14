package org.TvSeries;

import org.example.SeriesModelClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeriesModelClassTest {

    @Test
    void testConstructorAndGetters() {
        SeriesModelClass series = new SeriesModelClass("S001", "Breaking Code", "16", "10");

        assertEquals("S001", series.getSeriesId());
        assertEquals("Breaking Code", series.getSeriesName());
        assertEquals("16", series.getSeriesAge());
        assertEquals("10", series.getSeriesNumberOfEpisodes());
    }

    @Test
    void testSetters() {
        SeriesModelClass series = new SeriesModelClass("S001", "Breaking Code", "16", "10");

        series.setSeriesId("S002");
        series.setSeriesName("Debugging Wars");
        series.setSeriesAge("12");
        series.setSeriesNumberOfEpisodes("20");

        assertEquals("S002", series.getSeriesId());
        assertEquals("Debugging Wars", series.getSeriesName());
        assertEquals("12", series.getSeriesAge());
        assertEquals("20", series.getSeriesNumberOfEpisodes());
    }

    @Test
    void testToStringFormat() {
        SeriesModelClass series = new SeriesModelClass("S001", "Breaking Code", "16", "10");
        String result = series.toString();

        assertTrue(result.contains("S001"));
        assertTrue(result.contains("Breaking Code"));
        assertTrue(result.contains("16"));
        assertTrue(result.contains("10"));
    }
}
