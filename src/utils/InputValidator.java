package utils;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Utility class to capture and validate user keyboard inputs safely.
 * Prevents application crashes due to type mismatches and handles empty strings,
 * range validation, and regular expression checks.
 */
public class InputValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@(.+)$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+?[0-9\\s-]{7,15}$"
    );

    /**
     * Reads and parses an integer input from the console with boundary bounds.
     */
    public static int readInteger(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Error: Input must be between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid numeric format. Please enter a valid integer.");
            }
        }
    }

    /**
     * Reads and parses a double input from the console with boundary bounds.
     */
    public static double readDouble(Scanner scanner, String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Error: Input must be between %.2f and %.2f.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid numeric format. Please enter a valid decimal.");
            }
        }
    }

    /**
     * Reads a non-empty string input.
     */
    public static String readString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("Error: This field cannot be empty. Please enter a value.");
        }
    }

    /**
     * Reads and validates an email address.
     */
    public static String readEmail(Scanner scanner, String prompt) {
        while (true) {
            String email = readString(scanner, prompt);
            if (EMAIL_PATTERN.matcher(email).matches()) {
                return email;
            }
            System.out.println("Error: Invalid email format (e.g., user@example.com). Try again.");
        }
    }

    /**
     * Reads and validates a phone number.
     */
    public static String readPhone(Scanner scanner, String prompt) {
        while (true) {
            String phone = readString(scanner, prompt);
            if (PHONE_PATTERN.matcher(phone).matches()) {
                return phone;
            }
            System.out.println("Error: Invalid phone format. (7 to 15 digits, digits/hyphens/spaces/plus allowed).");
        }
    }
}
