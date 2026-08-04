package utils;

import java.util.Scanner;

/**
 * Robust static Input Validator to handle terminal exceptions and invalid entries cleanly.
 *
 * Time Complexity: O(1) per validation.
 * Space Complexity: O(1)
 */
public class InputValidator {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Safely reads an integer from the console, reprompting if invalid.
     * @param prompt User prompt text.
     * @return Validated integer.
     */
    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input: Please enter a valid integer number.");
            }
        }
    }

    /**
     * Safely reads a double from the console, reprompting if invalid.
     * @param prompt User prompt text.
     * @return Validated double value.
     */
    public static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                double val = Double.parseDouble(input);
                if (val < 0) {
                    System.out.println("Invalid Input: Negative values are not allowed.");
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input: Please enter a valid decimal number.");
            }
        }
    }

    /**
     * Reads a non-empty string from the console.
     * @param prompt User prompt text.
     * @return Validated non-empty string.
     */
    public static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Invalid Input: Field cannot be empty.");
                continue;
            }
            return input;
        }
    }

    /**
     * Safely reads a boolean value from a yes/no prompt.
     * @param prompt User prompt text.
     * @return Boolean representation.
     */
    public static boolean readBoolean(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            } else {
                System.out.println("Invalid Input: Please enter 'y' for Yes or 'n' for No.");
            }
        }
    }
}
