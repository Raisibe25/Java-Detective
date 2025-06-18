package model;

/**
 * Represents a clue in the mystery game.
 * Each clue has a description, an optional hint, and an optional link to a suspect.
 */
public class Clue {
    private String description;
    private String hint;
    private String relatedSuspect;

    // Basic constructor (only description)
    public Clue(String description) {
        this(description, "", "");
    }

    // Full constructor with description, hint, and related suspect.
    public Clue(String description, String hint, String relatedSuspect) {
        this.description = description;
        this.hint = hint;
        this.relatedSuspect = relatedSuspect;
    }

    public String getDescription() {
        return description;
    }

    public String getHint() {
        return hint;
    }

    public String getRelatedSuspect() {
        return relatedSuspect;
    }

    /**
     * Returns a formatted string including extra clue details.
     */
    public String getDetailedDescription() {
        StringBuilder sb = new StringBuilder(description);
        if (!hint.isEmpty()) {
            sb.append(" [Hint: ").append(hint).append("]");
        }
        if (!relatedSuspect.isEmpty()) {
            sb.append(" [Related Suspect: ").append(relatedSuspect).append("]");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getDetailedDescription();
    }
}