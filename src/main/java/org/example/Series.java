
package org.example;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Series {

    private final List<SeriesModelClass> seriesList = new ArrayList<>();
    private Scanner scanner;

    public Series() {
        this.scanner = new Scanner(System.in);
    }

    // ===== 1.1 Start Screen =====
    public void displayMenu() {
        System.out.println();
        System.out.println("LATEST SERIES - 2025");
        System.out.println("***********************************************");
        System.out.print("Enter (1) to launch menu or any other key to exit\n");

        String input = scanner.nextLine().trim();
        if ("1".equals(input)) {
            showMainMenu();
        } else {
            ExitSeriesApplication();
        }
    }

    // ===== Main Menu =====
    private void showMainMenu() {
        while (true) {
            System.out.println();
            System.out.println("Please select one of the following menu items:");
            System.out.println("(1) Capture a new series.");
            System.out.println("(2) Search for a series.");
            System.out.println("(3) Update series age restriction");
            System.out.println("(4) Delete a series.");
            System.out.println("(5) Print series report - 2025");
            System.out.println("(6) Exit Application.");
            System.out.print("Enter your choice");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": CaptureSeries(); break;
                case "2": SearchSeries(); break;
                case "3": UpdateSeries(); break;
                case "4": DeleteSeries(); break;
                case "5": SeriesReport(); break;
                case "6": ExitSeriesApplication(); return;
                default:  System.out.println("Invalid option. Please select 1-6.");
            }
        }
    }

    // ===== 1.2 & 1.4 Capture (save to memory) =====
    public void CaptureSeries() {
        System.out.println();
        System.out.println("CAPTURE A NEW SERIES");
        System.out.println("***********************************************");

        String id = prompt("Enter the series id: ");
        String name = prompt("Enter the series name: ");
        String age = promptValidAge();               // 1.3 numeric 2..18 only
        String episodes = promptValidEpisodes(name); // positive integer

        seriesList.add(new SeriesModelClass(id, name, age, episodes));

        System.out.println("Series processed successfully!!!");
        promptReturnToMenu();
    }

    // ===== 1.5 Search =====
    public void SearchSeries() {
        System.out.print("\nEnter the series id to search: ");
        String id = scanner.nextLine().trim();

        SeriesModelClass s = findById(id);
        if (s == null) {
            System.out.println();
            System.out.println("Series with Series Id: " + id + " was not found!");
            promptReturnToMenu();
            return;
        }

        System.out.println("-----------------------------------------------------------");
        System.out.println("SERIES ID: " + s.getSeriesId());
        System.out.println("SERIES NAME: " + s.getSeriesName());
        System.out.println("SERIES AGE RESTRICTION: " + s.getSeriesAge());
        System.out.println("SERIES NUMBER OF EPISODES: " + s.getSeriesNumberOfEpisodes());
        System.out.println("-----------------------------------------------------------");
        promptReturnToMenu();
    }

    // ===== 1.6 Update (replace object; no setters) =====
    public void UpdateSeries() {
        System.out.print("\nEnter the series id to update: ");
        String id = scanner.nextLine().trim();

        int index = findIndexById(id);
        if (index < 0) {
            System.out.println("Series with Series Id: " + id + " was not found!");
            promptReturnToMenu();
            return;
        }

        String name = prompt("Enter the series name: ");
        String age = promptValidAge();
        String episodes = promptValidEpisodes(name);

        seriesList.set(index, new SeriesModelClass(id, name, age, episodes));
        promptReturnToMenu();
    }

    // ===== 1.7 Delete (confirm with y) =====
    public void DeleteSeries() {
        System.out.print("\nEnter the series id to delete: ");
        String id = scanner.nextLine().trim();

        int index = findIndexById(id);
        if (index < 0) {
            System.out.println("Series with Series Id: " + id + " was not found!");
            promptReturnToMenu();
            return;
        }

        System.out.print("Are you sure you want to delete series " + id + " from the system? Yes (y) to delete.\n> ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("y")) {
            seriesList.remove(index);
            System.out.println("----------------------------------------");
            System.out.println("Series with Series Id: " + id + " WAS deleted!");
            System.out.println("----------------------------------------");
        } else {
            System.out.println("Delete cancelled.");
        }
        promptReturnToMenu();
    }

    // ===== 1.10 Report =====
    public void SeriesReport() {
        System.out.println("\n SERIES REPORT - 2025");
        System.out.println("-----------------------------------------------------------------");
        if (seriesList.isEmpty()) {
            System.out.println("No series captured yet.");
        } else {
            for (SeriesModelClass s : seriesList) {
                System.out.println("SERIES ID: " + s.getSeriesId());
                System.out.println("SERIES NAME: " + s.getSeriesName());
                System.out.println("SERIES AGE RESTRICTION: " + s.getSeriesAge());
                System.out.println("SERIES NUMBER OF EPISODES: " + s.getSeriesNumberOfEpisodes());
                System.out.println("-----------------------------------------------------------");
            }
        }
        promptReturnToMenu();
    }

    // ===== Exit =====
    public void ExitSeriesApplication() {
        System.out.println("\nExiting application. Goodbye!");
    }

    // ------------------ Console + validation helpers ------------------

    private String prompt(String label) {
        System.out.print(label);
        return scanner.nextLine().trim();
    }

    // 1.3: numbers only and between 2..18; prompts & messages like the screenshots
    private String promptValidAge() {
        while (true) {
            System.out.print("Enter the series age restriction: ");
            String first = scanner.nextLine().trim();
            if (isValidAge(first)) return first;

            System.out.println("You have entered a incorrect series age!!!");
            System.out.print("Please re-enter the series age >> ");
            String retry = scanner.nextLine().trim();
            if (isValidAge(retry)) return retry;

            System.out.println("You have entered a incorrect series age!!!");
        }
    }

    private boolean isValidAge(String value) {
        if (!value.matches("\\d+")) return false;
        int n = Integer.parseInt(value);
        return n >= 2 && n <= 18;
    }

    private String promptValidEpisodes(String seriesName) {
        while (true) {
            System.out.print("Enter the number of episodes for " + seriesName + ": ");
            String input = scanner.nextLine().trim();
            if (input.matches("\\d+") && Integer.parseInt(input) > 0) return input;

            System.out.println("Invalid number of episodes!");
            System.out.print("Please re-enter the number of episodes >> ");
            String retry = scanner.nextLine().trim();
            if (retry.matches("\\d+") && Integer.parseInt(retry) > 0) return retry;

            System.out.println("Invalid number of episodes!");
        }
    }

    private SeriesModelClass findById(String id) {
        int i = findIndexById(id);
        return i >= 0 ? seriesList.get(i) : null;
    }

    private int findIndexById(String id) {
        for (int i = 0; i < seriesList.size(); i++) {
            if (seriesList.get(i).getSeriesId().equals(id)) return i;
        }
        return -1;
    }

    private void promptReturnToMenu() {
        System.out.print("\nEnter (1) to launch menu or any other key to exit\n ");
        String input = scanner.nextLine().trim();
        if (!"1".equals(input)) {
            ExitSeriesApplication();
            System.exit(0);
        }
    }

    // ===================== TEST HOOK =====================
    /** Rebind the internal Scanner to a new input stream (use in unit tests after System.setIn(...)). */
    public void resetInputForTest(InputStream in) {
        this.scanner = new Scanner(in);
    }
}
