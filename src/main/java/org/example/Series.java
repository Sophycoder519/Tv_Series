package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Series {
    private final ArrayList<SeriesModelClass> seriesList;
    private final Scanner scanner;
    private final boolean testMode;

    public Series() {
        this(false);
    }

    public Series(boolean testMode) {
        this.seriesList = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.testMode = testMode;
    }

    public void displayMenu() {
        System.out.println("\nLATEST SERIES - 2025");
        System.out.println("***********************************************");
        System.out.println("Enter (1) to launch menu or any other key to exit");

        String input = scanner.nextLine();
        if (input.equals("1")) {
            showMainMenu();
        } else {
            exitSeriesApplication();
        }
    }

    private void showMainMenu() {
        System.out.println("\nPlease select one of the following menu items:");
        System.out.println("(1) Capture a new series.");
        System.out.println("(2) Search for a series.");
        System.out.println("(3) Update series age restriction");
        System.out.println("(4) Delete a series.");
        System.out.println("(5) Print series report - 2025");
        System.out.println("(6) Exit Application.");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1" -> captureSeries();
            case "2" -> searchSeries();
            case "3" -> updateSeries();
            case "4" -> deleteSeries();
            case "5" -> seriesReport();
            case "6" -> exitSeriesApplication();
            default -> {
                System.out.println("Invalid option. Please try again.");
                showMainMenu();
            }
        }
    }

    public void captureSeries() {
        System.out.println("\nCAPTURE A NEW SERIES");
        System.out.println("***************");

        System.out.print("Enter the series id: ");
        String id = scanner.nextLine();

        System.out.print("Enter the series name: ");
        String name = scanner.nextLine();

        String age;
        while (true) {
            System.out.print("Enter the series age restriction: ");
            age = scanner.nextLine();
            try {
                int ageNum = Integer.parseInt(age);
                if (ageNum >= 2 && ageNum <= 18) break;
            } catch (NumberFormatException ignored) {}
            showInvalidAgeMessage();
        }

        System.out.print("Enter the number of episodes for " + name + ": ");
        String episodes = scanner.nextLine();

        seriesList.add(new SeriesModelClass(id, name, age, episodes));
        System.out.println("Series processed successfully!!!");

        if (!testMode) displayMenu();
    }

    public void searchSeries() {
        System.out.print("\nEnter the series id to search: ");
        String searchId = scanner.nextLine();

        SeriesModelClass found = findSeriesById(searchId);
        if (found != null) {
            System.out.println("---");
            System.out.println("SERIES ID: " + found.getSeriesId());
            System.out.println("SERIES NAME: " + found.getSeriesName());
            System.out.println("SERIES AGE RESTRICTION: " + found.getSeriesAge());
            System.out.println("SERIES NUMBER OF EPISODES: " + found.getSeriesNumberOfEpisodes());
            System.out.println("---");
        } else {
            System.out.println("---");
            System.out.println("Series with Series Id: " + searchId + " was not found!");
            System.out.println("---");
        }

        if (!testMode) displayMenu();
    }

    public void updateSeries() {
        System.out.print("\nEnter the series id to update: ");
        String updateId = scanner.nextLine();

        SeriesModelClass toUpdate = findSeriesById(updateId);
        if (toUpdate == null) {
            System.out.println("Series not found!");
            if (!testMode) displayMenu();
            return;
        }

        System.out.print("Enter the series name (" + toUpdate.getSeriesName() + "): ");
        String newName = scanner.nextLine();
        if (!newName.isEmpty()) toUpdate.setSeriesName(newName);

        System.out.print("Enter the age restriction (" + toUpdate.getSeriesAge() + "): ");
        String newAge = scanner.nextLine();
        if (!newAge.isEmpty()) {
            try {
                int ageNum = Integer.parseInt(newAge);
                if (ageNum >= 2 && ageNum <= 18) {
                    toUpdate.setSeriesAge(newAge);
                } else {
                    System.out.println("Invalid age. Keeping previous value.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Keeping previous value.");
            }
        }

        System.out.print("Enter the number of episodes (" + toUpdate.getSeriesNumberOfEpisodes() + "): ");
        String newEpisodes = scanner.nextLine();
        if (!newEpisodes.isEmpty()) toUpdate.setSeriesNumberOfEpisodes(newEpisodes);

        System.out.println("Series updated successfully!");
        if (!testMode) displayMenu();
    }

    public void deleteSeries() {
        System.out.print("\nEnter the series id to delete: ");
        String deleteId = scanner.nextLine();

        SeriesModelClass toDelete = findSeriesById(deleteId);
        if (toDelete == null) {
            System.out.println("Series not found!");
            if (!testMode) displayMenu();
            return;
        }

        System.out.print("Are you sure you want to delete series " + deleteId + " from the system? Yes (y) to delete: ");
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("y")) {
            seriesList.remove(toDelete);
            System.out.println("---");
            System.out.println("Series with Series Id: " + deleteId + " WAS deleted!");
            System.out.println("---");
        } else {
            System.out.println("Deletion cancelled.");
        }

        if (!testMode) displayMenu();
    }

    public void seriesReport() {
        if (seriesList.isEmpty()) {
            System.out.println("No series data available.");
            if (!testMode) displayMenu();
            return;
        }

        System.out.println("\nSERIES REPORT - 2025");
        int count = 1;
        for (SeriesModelClass series : seriesList) {
            System.out.println("Series " + count);
            System.out.println("---");
            System.out.println("SERIES ID: " + series.getSeriesId());
            System.out.println("SERIES NAME: " + series.getSeriesName());
            System.out.println("SERIES AGE RESTRICTION: " + series.getSeriesAge());
            System.out.println("NUMBER OF EPISODES: " + series.getSeriesNumberOfEpisodes());
            System.out.println("---");
            count++;
        }

        if (!testMode) displayMenu();
    }

    public void exitSeriesApplication() {
        System.out.println("Exiting application...");
        if (!testMode) {
            scanner.close();
            System.exit(0);
        }
    }

    public List<SeriesModelClass> getSeriesList() {
        return seriesList;
    }

    private void showInvalidAgeMessage() {
        System.out.println("You have entered an incorrect series age!!!");
        System.out.println("Please re-enter the series age >>");
    }

    private SeriesModelClass findSeriesById(String id) {
        for (SeriesModelClass s : seriesList) {
            if (s.getSeriesId().equals(id)) {
                return s;
            }
        }
        return null;
    }
}
