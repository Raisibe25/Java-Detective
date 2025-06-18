Narrative and Clues

The game begins by reading a mystery story about the missing scones at the Khumalo’s Christmas Luncheon. Clues are crafted to point specifically to suspects based on motive, means,
and opportunity.

Suspects and Dialogue

Each suspect (Nomsa, Sipho, and Thando) is defined with a dialogue tree that addresses key plot points (such as arguments or last seen locations), allowing for branching interactions based on the player’s questions.

File I/O and Database Handling

The FileManager is responsible for loading the text files that contain the narrative and clues, and saving the investigation log. The DatabaseManager uses JDBC to track suspect 
interrogation details and player progress.

User Interface

A Swing-based GUI collects input from the user through buttons and dialogs, then communicates with the game logic to update the display area with narrative text, clues, suspect 
responses, and outcomes of accusations.
