package data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import model.Clue;

/**
 * Handles file operations for the game.
 */
public class FileManager {

    /**
     * Loads the entire case story from a text file.
     * @param filePath Path to the case story file.
     * @return The case story as a String.
     */
    public static String loadCase(String filePath) {
        StringBuilder caseText = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                caseText.append(line).append("\n");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return caseText.toString();
    }

    /**
     * Loads clues from a file.
     * Each line contains fields separated by '|' in the format: description|hint|relatedSuspect
     * @param filePath The path to the clues file.
     * @return A List of Clue objects.
     */
    public static List<Clue> loadClues(String filePath) {
        List<Clue> clues = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                String description = parts.length > 0 ? parts[0].trim() : "";
                String hint = parts.length > 1 ? parts[1].trim() : "";
                String relatedSuspect = parts.length > 2 ? parts[2].trim() : "";
                clues.add(new Clue(description, hint, relatedSuspect));
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return clues;
    }

    /**
     * Reads all lines from a file into a list of Strings.
     * @param filePath The file to be read.
     * @return A List of lines.
     */
    public static List<String> readLinesFromFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines; // returning the list of lines read from the file
    }

    /**
     * Appends an entry to the investigation log.
     * @param filePath Path to the investigation log file.
     * @param log The log entry.
     */
    public static void saveInvestigationLog(String filePath, String log) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) { // append mode
            bw.write(log);
            bw.newLine();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}