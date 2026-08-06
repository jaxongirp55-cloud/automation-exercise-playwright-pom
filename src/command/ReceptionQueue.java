package command;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Stack;

/**
 * Invoker and manager for Commands in the Command design pattern.
 * Manages a queue of pending hotel requests and a history stack to support undo operations.
 */
public class ReceptionQueue {
    private Deque<Command> pendingQueue;
    private Stack<Command> historyStack;

    /**
     * Constructor for ReceptionQueue.
     */
    public ReceptionQueue() {
        this.pendingQueue = new ArrayDeque<>();
        this.historyStack = new Stack<>();
    }

    /**
     * Adds a command to the pending queue.
     * @param cmd The command to queue.
     */
    public synchronized void enqueue(Command cmd) {
        if (cmd != null) {
            pendingQueue.addLast(cmd);
        }
    }

    /**
     * Executes the next command in the queue and moves it to history.
     */
    public synchronized boolean processNext() {
        if (pendingQueue.isEmpty()) {
            return false;
        }
        Command cmd = pendingQueue.pollFirst();
        cmd.execute();
        historyStack.push(cmd);
        return true;
    }

    /**
     * Reverses the last executed command from history.
     */
    public synchronized boolean undoLast() {
        if (historyStack.isEmpty()) {
            return false;
        }
        Command cmd = historyStack.pop();
        cmd.undo();
        return true;
    }

    /**
     * Gets a list of pending commands.
     * @return List of commands in the queue.
     */
    public synchronized List<Command> getPendingCommands() {
        return new ArrayList<>(pendingQueue);
    }

    /**
     * Gets a list of executed commands in reverse chronological order.
     * @return List of executed commands.
     */
    public synchronized List<Command> getHistory() {
        return new ArrayList<>(historyStack);
    }

    /**
     * Clears both the queue and history.
     */
    public synchronized void clearAll() {
        pendingQueue.clear();
        historyStack.clear();
    }
}
