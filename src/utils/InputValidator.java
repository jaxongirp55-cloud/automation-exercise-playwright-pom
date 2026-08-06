package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Utility class to validate and sanitize various user inputs.
 * Ensures the robustness of the command-line console application.
 */
public class InputValidator {

    /**
     * Reads and validates an integer from the console within a specific range.
     *
     * @param scanner Console Scanner.
     * @param prompt  Input instructions.
     * @param min     Minimum permissible bound.
     * @param max     Maximum permissible bound.
     * @return Validated integer.
     */
    public static int readInteger(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String raw = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(raw);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Invalid Choice: Option must be between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input: Please enter a valid integer.");
            }
        }
    }

    /**
     * Reads a double from the console within a specific range.
     *
     * @param scanner Console Scanner.
     * @param prompt  Input instructions.
     * @param min     Minimum permissible bound.
     * @param max     Maximum permissible bound.
     * @return Validated double.
     */
    public static double readDouble(Scanner scanner, String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String raw = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(raw);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Invalid Choice: Value must be between " + String.format("%.2f", min) + " and " + String.format("%.2f", max) + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input: Please enter a valid double number.");
            }
        }
    }

    /**
     * Reads a non-empty string.
     *
     * @param scanner Console Scanner.
     * @param prompt  Input instructions.
     * @return Sanitized non-empty string.
     */
    public static String readString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Invalid Input: Input cannot be empty.");
        }
    }

    /**
     * Reads a valid date formatted as "yyyy-MM-dd".
     *
     * @param scanner Console Scanner.
     * @param prompt  Input instructions.
     * @return Validated date string.
     */
    public static String readDate(Scanner scanner, String prompt) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                LocalDate.parse(input, dtf);
                return input;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid Format: Please enter the date as yyyy-MM-dd (e.g., 2026-05-15).");
            }
        }
    }

    /**
     * Reads a valid email string.
     *
     * @param scanner Console Scanner.
     * @param prompt  Input instructions.
     * @return Validated email.
     */
    public static String readEmail(Scanner scanner, String prompt) {
        while (true) {
            String input = readString(scanner, prompt);
            if (input.contains("@") && input.contains(".") && input.indexOf("@") < input.lastIndexOf(".")) {
                return input;
            }
            System.out.println("Invalid Email: Email must contain '@' and a domain (e.g. guest@stayease.com).");
        }
    }

    /**
     * Reads a yes/no option.
     *
     * @param scanner Console Scanner.
     * @param prompt  Input instructions.
     * @return True for 'y' or 'yes', false for 'n' or 'no'.
     */
    public static boolean readBoolean(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            }
            if (input.equals("n") || input.equals("no")) {
                return false;
            }
            System.out.println("Invalid Response: Please enter 'y' (yes) or 'n' (no).");
        }
    }
}
