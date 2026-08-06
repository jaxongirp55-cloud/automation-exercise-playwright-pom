package command;

/**
 * Command pattern interface outlining transactional execute and undo capabilities.
 * Decouples request senders from their direct receivers.
 */
public interface Command {

    /**
     * Executes the specific operation.
     */
    void execute();

    /**
     * Undoes the specific operation, restoring prior state.
     */
    void undo();

    /**
     * Gets a description label for the executed transaction.
     *
     * @return Descriptive log message.
     */
    String getDescription();
}
