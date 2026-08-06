package command;

/**
 * Interface representing a Command following the Command design pattern.
 * Supports execution and reversing (undoing) operations.
 */
public interface Command {

    /**
     * Executes the command.
     */
    void execute();

    /**
     * Undoes the command's execution, restoring the previous state.
     */
    void undo();

    /**
     * Returns a user-friendly description of the command.
     * @return Description text.
     */
    String getDescription();
}
