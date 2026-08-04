package utils;

import java.util.Scanner;

/**
 * Validates terminal command outputs and user keyboard queries.
 * Prevents system interruptions due to unexpected user characters.
 *
 * @author Senior Java Software Architect
 */
public class InputValidator {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Reads a non-empty string from terminal.
     * @param prompt User instruction prompt.
     * @return Validated string.
     */
    public static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String val = scanner.nextLine();
            if (val != null && !val.trim().isEmpty()) {
                return val.trim();
            }
            System.out.println("Error: Input cannot be empty. Please try again.");
        }
    }

    /**
     * Reads a valid positive double value.
     * @param prompt User prompt.
     * @return Positive double value.
     */
    public static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                double val = Double.parseDouble(input);
                if (val >= 0) {
                    return val;
                }
                System.out.println("Error: Value must be positive. Please try again.");
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid numeric input. Please enter a valid decimal number.");
            }
        }
    }

    /**
     * Reads a valid integer from terminal.
     * @param prompt User prompt.
     * @return Valid integer.
     */
    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid integer input. Please enter a valid number.");
            }
        }
    }

    /**
     * Reads a valid integer within a given range (inclusive).
     * @param prompt User prompt.
     * @param min Min range.
     * @param max Max range.
     * @return Valid integer in range.
     */
    public static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int val = readInt(prompt);
            if (val >= min && val <= max) {
                return val;
            }
            System.out.printf("Error: Input must be between %d and %d (inclusive).\n", min, max);
        }
    }

    /**
     * Reads a yes/no boolean response from user.
     * @param prompt User instruction.
     * @return true for Y/yes, false for N/no.
     */
    public static boolean readBoolean(String prompt) {
        while (true) {
            System.out.print(prompt + " (Y/N): ");
            String ans = scanner.nextLine();
            if (ans != null && !ans.trim().isEmpty()) {
                String clean = ans.trim().toLowerCase();
                if (clean.startsWith("y")) {
                    return true;
                } else if (clean.startsWith("n")) {
                    return false;
                }
            }
            System.out.println("Error: Please enter 'Y' for Yes or 'N' for No.");
        }
    }
}
