package org.example;

public class SeriesModelClass {
    private String seriesId;
    private String seriesName;
    private String seriesAge;
    private String seriesNumberOfEpisodes;

    // Constructor
    public SeriesModelClass(String seriesId, String seriesName, String seriesAge, String seriesNumberOfEpisodes) {
        this.seriesId = seriesId;
        this.seriesName = seriesName;
        this.seriesAge = seriesAge;
        this.seriesNumberOfEpisodes = seriesNumberOfEpisodes;
    }

    // Getters and Setters
    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getSeriesAge() {
        return seriesAge;
    }

    public void setSeriesAge(String seriesAge) {
        this.seriesAge = seriesAge;
    }

    public String getSeriesNumberOfEpisodes() {
        return seriesNumberOfEpisodes;
    }

    public void setSeriesNumberOfEpisodes(String seriesNumberOfEpisodes) {
        this.seriesNumberOfEpisodes = seriesNumberOfEpisodes;
    }

    @Override
    public String toString() {
        return String.format(
                "Series ID: %s, Name: %s, Age Restriction: %s, Episodes: %s",
                seriesId, seriesName, seriesAge, seriesNumberOfEpisodes
        );
    }
}
