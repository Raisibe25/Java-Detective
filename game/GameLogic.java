package game;

import java.util.*;
import model.Clue;
import model.Suspect;
import model.CaseDifficulty;
import data.FileManager;
import data.DatabaseManager;

/**
 * Core game logic that loads the case narrative, clues, and suspects.
 * It also handles suspect interrogation and evaluating accusations.
 */
public class GameLogic {
    private String caseStory;
    private List<Clue> clues;
    // Map to store suspects keyed by their name.
    private Map<String, Suspect> suspects;
    private CaseDifficulty difficulty;
    private DatabaseManager dbManager;

    /**
     * Constructs the GameLogic instance.
     *
     * @param caseFilePath  Path to the case narrative text file.
     * @param cluesFilePath Path to the clues text file.
     * @param dbManager     The DatabaseManager handling database interactions.
     */
    public GameLogic(String caseFilePath, String cluesFilePath, DatabaseManager dbManager) {
        // Load the case narrative from file.
        this.caseStory = FileManager.loadCase(caseFilePath);
        // Load clues from file.
        this.clues = FileManager.loadClues(cluesFilePath);
        this.dbManager = dbManager;
        // Initialize suspects with branching dialogue.
        initializeSuspects();
        // Set a default difficulty.
        difficulty = CaseDifficulty.MEDIUM;
    }

    public String getCaseInformation() {
        StringBuilder info = new StringBuilder();
        info.append(getCaseStory());
        info.append("\n\n---------- Case Information ----------\n");
        info.append("Suspects:\n");
        // Append each suspect's name.
        for (Suspect suspect : getSuspects()) {
            info.append("- ").append(suspect.getName()).append("\n");
        }
        info.append("\nFollow the clues, question the suspects and make your accusation wisely.\n");
        return info.toString();
    }

    /**
     * Initializes the suspects and their branching dialogue trees.
     */
    /**
     * Initializes the suspects with dialogue branches that reflect the new clues.
     *
     * New clues indicate:
     * - Sipho was last seen near the pantry and holds a key.
     * - Nomsa had a heated conversation with Gogo over the secret scone recipe.
     * - Thando was unexpectedly seen in the kitchen without a clear explanation.
     */
    private void initializeSuspects() {
        suspects = new LinkedHashMap<>();

        // Suspect: Nomsa
        // Clues: Had a heated conversation with Gogo about the secret recipe.
        Map<String, Map<String, String>> nomsaDialogue = new LinkedHashMap<>();
        Map<String, String> nomsaStart = new LinkedHashMap<>();
        nomsaStart.put("Where were you when the scones disappeared?",
                "I was in the main hall, keeping an eye on the setup. I admit I argued with Gogo earlier, but I wouldn't steal from my own family.");
        nomsaStart.put("Can you explain your argument with Gogo?",
                "Yes, we disagreed about the secret recipe, but that was just a passing conflict. I value our traditions too much.");
        nomsaDialogue.put("Start", nomsaStart);
        Suspect nomsa = new Suspect("Nomsa", nomsaDialogue);

        // Suspect: Sipho
        // Clues: Last seen near the pantry; has key access; anonymous tip suggests frustration.
        Map<String, Map<String, String>> siphoDialogue = new LinkedHashMap<>();
        Map<String, String> siphoStart = new LinkedHashMap<>();
        siphoStart.put("Where were you last seen?",
                "I was near the pantry, checking the ingredients. Yes, I have a key—but I only use it when necessary.");
        siphoStart.put("What about the tip saying you feel overlooked?",
                "I admit I've felt underappreciated sometimes, but that doesn't mean I'd sabotage our traditions.");
        siphoDialogue.put("Start", siphoStart);
        Suspect sipho = new Suspect("Sipho", siphoDialogue);

        // Suspect: Thando
        // Clues: Unexpected kitchen visit without clear motive.
        Map<String, Map<String, String>> thandoDialogue = new LinkedHashMap<>();
        Map<String, String> thandoStart = new LinkedHashMap<>();
        thandoStart.put("Why were you in the kitchen shortly before the scones went missing?",
                "I was just curious; the kitchen is where the magic happens, and I wanted to see if everything was in order.");
        thandoStart.put("Do you think you had anything to do with the disappearance?",
                "No, I was simply passing through. I have no motive to disrupt the family tradition.");
        thandoDialogue.put("Start", thandoStart);
        Suspect thando = new Suspect("Thando", thandoDialogue);

        // Store the newly created suspects in the map.
        suspects.put(nomsa.getName(), nomsa);
        suspects.put(sipho.getName(), sipho);
        suspects.put(thando.getName(), thando);
    }


    /**
     * Retrieves the case narrative.
     *
     * @return The case story as a String.
     */
    public String getCaseStory() {
        return caseStory;
    }

    /**
     * Retrieves the list of clues.
     *
     * @return A list of Clue objects.
     */
    public List<Clue> getClues() {
        return clues;
    }

    /**
     * Retrieves all suspects.
     *
     * @return A collection of Suspect objects.
     */
    public Collection<Suspect> getSuspects() {
        return suspects.values();
    }

    /**
     * Simulates asking a suspect a particular question.
     *
     * It uses the specified dialogue context (e.g., "Start") and returns the suspect's response.
     * In addition, it updates the suspect's record in the database as having been questioned.
     *
     * @param suspectName The name of the suspect.
     * @param context     The dialogue context (e.g., "Start" or follow-up branch).
     * @param question    The chosen question.
     * @return The suspect's response text.
     */
    public String askQuestion(String suspectName, String context, String question) {
        Suspect suspect = suspects.get(suspectName);
        if (suspect == null) {
            return "No such suspect found.";
        }
        // Update suspect's status as questioned.
        dbManager.updateSuspect(suspectName, true);

        return suspect.getResponse(context, question);
    }

    /**
     * Evaluates the player's accusation.
     *
     * For demonstration purposes, the outcome is pre-determined based on the suspect name.
     *
     * @param accusedSuspect The name of the suspect being accused.
     * @return A message indicating whether the accusation was correct.
     */
    public String makeAccusation(String accusedSuspect) {
        String outcome;
        // For this example, assume that only Sipho is guilty.
        if ("Sipho".equalsIgnoreCase(accusedSuspect)) {
            outcome = "Correct! Sipho was caught trying to steal Gogo's secret scone recipe. Case closed.";
        } else {
            outcome = "Incorrect. " + accusedSuspect + " is not responsible. The mystery deepens.";
        }
        return outcome;
    }
}