package command;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Manages the reception service request queue and handles command execution history.
 * Implements a FIFO queue for pending hotel transactions,
 * and a LIFO stack to support undo capabilities.
 */
public class ReceptionQueue {
    private final Queue<Command> pendingQueue;
    private final Stack<Command> undoStack;

    /**
     * Constructs a new ReceptionQueue manager.
     */
    public ReceptionQueue() {
        this.pendingQueue = new LinkedList<>();
        this.undoStack = new Stack<>();
    }

    /**
     * Adds a new Command request to the FIFO queue.
     *
     * @param command The Command transaction to queue.
     */
    public void enqueue(Command command) {
        if (command != null) {
            pendingQueue.offer(command);
        }
    }

    /**
     * Executes the next Command in the queue.
     * Pushes the command to the undo stack upon successful execution.
     *
     * @return Description of the executed command, or null if the queue is empty.
     */
    public String processNext() {
        Command nextCommand = pendingQueue.poll();
        if (nextCommand != null) {
            nextCommand.execute();
            undoStack.push(nextCommand);
            return nextCommand.getDescription();
        }
        return null;
    }

    /**
     * Undoes the last executed Command transaction from the stack.
     *
     * @return Description of the undone command, or null if the undo stack is empty.
     */
    public String undoLast() {
        if (!undoStack.isEmpty()) {
            Command commandToUndo = undoStack.pop();
            commandToUndo.undo();
            return commandToUndo.getDescription();
        }
        return null;
    }

    /**
     * Gets the number of pending commands.
     *
     * @return Size of queue.
     */
    public int getPendingCount() {
        return pendingQueue.size();
    }

    /**
     * Gets the number of commands available to undo.
     *
     * @return Size of undo stack.
     */
    public int getUndoCount() {
        return undoStack.size();
    }

    /**
     * Returns an array of pending commands.
     *
     * @return Array of pending Command objects.
     */
    public Command[] getPendingCommands() {
        return pendingQueue.toArray(new Command[0]);
    }

    /**
     * Returns an array of executed commands on the undo stack.
     *
     * @return Array of undoable Command objects.
     */
    public Command[] getUndoHistory() {
        return undoStack.toArray(new Command[0]);
    }

    /**
     * Clears all pending requests and undo history.
     */
    public void clear() {
        pendingQueue.clear();
        undoStack.clear();
    }
}
