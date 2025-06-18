package gui;

import game.GameLogic;
import data.DatabaseManager;
import data.FileManager;
import model.Clue;
import model.Suspect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

/**
 * The main game window for "Java Detective".
 * Displays the case narrative, clues, and suspect interactions.
 * Provides controls for starting the case, viewing clues, questioning suspects,
 * making an accusation, and exiting the game.
 */
public class GameFrame extends JFrame implements ActionListener {

    // Buttons for the UI
    private JButton startCaseButton;
    private JButton viewCluesButton;
    private JButton questionSuspectsButton;
    private JButton makeAccusationButton;
    private JButton exitButton;

    // A text area for displaying the case narrative and game responses
    private JTextArea displayArea;

    // The game logic that drives core functionality
    private GameLogic gameLogic;

    /**
     * Constructs the main game window.
     */
    public GameFrame() {
        // Set up main JFrame properties.
        setTitle("Java Detective");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create control buttons.
        startCaseButton = new JButton("Start Case");
        viewCluesButton = new JButton("View Clues");
        questionSuspectsButton = new JButton("Question Suspects");
        makeAccusationButton = new JButton("Make Accusation");
        exitButton = new JButton("Exit");

        // Register ActionListeners for the buttons.
        startCaseButton.addActionListener(this);
        viewCluesButton.addActionListener(this);
        questionSuspectsButton.addActionListener(this);
        makeAccusationButton.addActionListener(this);
        exitButton.addActionListener(this);

        // Create the display area for showing text information.
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Arrange the buttons in a panel.
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
        buttonPanel.add(startCaseButton);
        buttonPanel.add(viewCluesButton);
        buttonPanel.add(questionSuspectsButton);
        buttonPanel.add(makeAccusationButton);
        buttonPanel.add(exitButton);

        // Set the layout and add components.
        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Initialize the database manager and load game logic.
        // Adjust file paths according to your project structure (e.g., "resources/case1.txt", "resources/clues.txt").
        DatabaseManager dbManager = new DatabaseManager("jdbc:sqlite:javadet.db");
        gameLogic = new GameLogic("src/case1.txt", "src/clues.txt", dbManager);
    }

    /**
     * Event handler for button clicks.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Start Case: Display and load the narrative.
        if (e.getSource() == startCaseButton) {
            displayArea.setText(gameLogic.getCaseStory());
        }
        // View Clues: Display all clues and log their discovery.
        else if (e.getSource() == viewCluesButton) {
            StringBuilder cluesText = new StringBuilder("Clues Discovered:\n\n");
            for (Clue clue : gameLogic.getClues()) {
                cluesText.append("- ").append(clue.getDetailedDescription()).append("\n");
                String logEntry = "[" + LocalDateTime.now() + "] Clue discovered: " + clue.getDetailedDescription();
                FileManager.saveInvestigationLog("src/investigation_log.txt", logEntry);
            }
            displayArea.setText(cluesText.toString());
        }
        // Question Suspects: Let the user pick a suspect and choose an initial question.
        else if (e.getSource() == questionSuspectsButton) {
            // Prompt the user to select a suspect.
            Object[] suspectNames = gameLogic.getSuspects().stream()
                    .map(Suspect::getName)
                    .toArray();

            String selectedSuspect = (String) JOptionPane.showInputDialog(
                    this,
                    "Select a suspect to question:",
                    "Question Suspects",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    suspectNames,
                    suspectNames[0]
            );

            if (selectedSuspect != null) {
                // Retrieve initial dialogue options from the suspect (using context "Start").
                java.util.List<String> options = new java.util.ArrayList<>(
                        gameLogic.getSuspects().stream()
                                .filter(s -> s.getName().equals(selectedSuspect))
                                .findFirst().get().getDialogueOptions("Start").keySet()
                );

                String selectedQuestion = (String) JOptionPane.showInputDialog(
                        this,
                        "Ask " + selectedSuspect + ":",
                        "Select a Question",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options.toArray(),
                        options.get(0)
                );

                if (selectedQuestion != null) {
                    String response = gameLogic.askQuestion(selectedSuspect, "Start", selectedQuestion);
                    displayArea.setText("Suspect: " + selectedSuspect + "\nQ: " + selectedQuestion + "\nA: " + response);
                }
            }
        }
        // Make Accusation: User enters a suspect's name to accuse, and the outcome is shown.
        else if (e.getSource() == makeAccusationButton) {
            String suspectAccused = JOptionPane.showInputDialog(
                    this, "Enter the name of the suspect you accuse:"
            );
            if (suspectAccused != null && !suspectAccused.trim().isEmpty()) {
                String outcome = gameLogic.makeAccusation(suspectAccused);
                displayArea.setText(outcome);
                // Log the accusation with a timestamp.
                String logEntry = "[" + LocalDateTime.now() + "] Accusation made on: " + suspectAccused + ". Outcome: " + outcome;
                FileManager.saveInvestigationLog("src/investigation_log.txt", logEntry);
            }
        }
        // Exit: Close the application.
        else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }

    /**
     * Entry point for the game.
     */
    public static void main(String[] args) {
        // Safely start the GUI on the Event Dispatch Thread.
        SwingUtilities.invokeLater(() -> {
            GameFrame frame = new GameFrame();
            frame.setVisible(true);
        });
    }
}