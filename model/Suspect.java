package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a suspect in the mystery game.
 * Each suspect has a name and a dialogue tree to support branching interrogation.
 */
public class Suspect {
    private String name;
    // The dialogueTree maps a context branch (e.g., "Start") to a map of question->response.
    private Map<String, Map<String, String>> dialogueTree;

    public Suspect(String name, Map<String, Map<String, String>> dialogueTree) {
        this.name = name;
        this.dialogueTree = dialogueTree;
    }

    public String getName() {
        return name;
    }

    /**
     * Returns the dialogue options for the given context (e.g. "Start").
     */
    public Map<String, String> getDialogueOptions(String context) {
        if (context == null || context.isEmpty()) {
            context = "Start";
        }
        return dialogueTree.getOrDefault(context, new HashMap<>());
    }

    /**
     * Provides the response associated with a given question in the current context.
     */
    public String getResponse(String context, String question) {
        Map<String, String> branch = dialogueTree.getOrDefault(context, new HashMap<>());
        return branch.getOrDefault(question, "I have nothing further to add.");
    }

    @Override
    public String toString() {
        return name;
    }
}