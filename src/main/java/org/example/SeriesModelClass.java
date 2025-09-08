package org.example;

public class SeriesModelClass {
    private final String seriesId;
    private final String seriesName;
    private final String seriesAge;
    private final String seriesNumberOfEpisodes;

    public SeriesModelClass(String seriesId, String seriesName, String seriesAge, String seriesNumberOfEpisodes) {
        this.seriesId = seriesId;
        this.seriesName = seriesName;
        this.seriesAge = seriesAge;
        this.seriesNumberOfEpisodes = seriesNumberOfEpisodes;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public String getSeriesAge() {
        return seriesAge;
    }

    public String getSeriesNumberOfEpisodes() {
        return seriesNumberOfEpisodes;
    }
}
