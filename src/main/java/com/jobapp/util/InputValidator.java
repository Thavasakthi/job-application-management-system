package com.jobapp.util;

import java.util.Scanner;

public class InputValidator {
    private static final Scanner scanner = new Scanner(System.in);

    public static String readString(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        return input != null ? input.trim() : "";
    }

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                ConsoleUtil.printError("Invalid input! Please enter a valid integer number.");
            }
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                double val = Double.parseDouble(input);
                if (val < 0) {
                    ConsoleUtil.printError("Amount cannot be negative. Try again.");
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                ConsoleUtil.printError("Invalid input! Please enter a valid decimal number (e.g. 75000.00).");
            }
        }
    }

    public static boolean readBoolean(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes") || input.equals("1") || input.equals("true")) {
                return true;
            } else if (input.equals("n") || input.equals("no") || input.equals("0") || input.equals("false")) {
                return false;
            }
            ConsoleUtil.printError("Invalid response. Please type 'y' for Yes or 'n' for No.");
        }
    }
}
