package chatapp;

// ChatApp.java – Console Registration & Login (single file for NetBeans)
// Run this file. You'll see a menu: 1 = Register, 2 = Login, 3 = Exit.

import java.util.Scanner;
import java.util.regex.Pattern;

public class ChatApp {

    // The Login object holds all user data and the six required methods.
    private static Login login = new Login();
    private static Scanner scanner = new Scanner(System.in);

    // ------------------------------------------------------------------
    // The main menu – runs until you choose Exit
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("      Welcome to ChatApp Console        ");
        System.out.println("========================================\n");

        boolean keepRunning = true;
        while (keepRunning) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Register a new account");
            System.out.println("2. Login to existing account");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // eat the leftover newline

            switch (choice) {
                case 1:
                    registerNewUser();   // asks for details, validates, stores user
                    break;
                case 2:
                    performLogin();      // asks for username/password, shows welcome or error
                    break;
                case 3:
                    keepRunning = false;
                    System.out.println("\nGoodbye! Thanks for using ChatApp.");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2 or 3.");
            }
        }
        scanner.close();
    }

    // ------------------------------------------------------------------
    // Registration flow – collects data and calls the registerUser method
    // ------------------------------------------------------------------
    private static void registerNewUser() {
        System.out.println("\n--- New User Registration ---");

        System.out.print("First name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Last name: ");
        String lastName = scanner.nextLine().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            System.out.println("First name and last name cannot be empty.\n");
            return;
        }

        System.out.print("Username (must contain '_' and max 5 characters): ");
        String username = scanner.nextLine().trim();

        System.out.print("Password (min 8 chars, 1 capital, 1 number, 1 special): ");
        String password = scanner.nextLine();

        System.out.print("Cell phone (+27... e.g. +27712345678): ");
        String phone = scanner.nextLine().trim();

        // Call the registerUser method from the Login class.
        // It returns a message telling us what happened.
        String resultMessage = login.registerUser(firstName, lastName, username, password, phone);
        System.out.println(resultMessage);
        System.out.println();
    }

    // ------------------------------------------------------------------
    // Login flow – checks credentials and prints the right message
    // ------------------------------------------------------------------
    private static void performLogin() {
        System.out.println("\n--- Login ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        // loginUser returns true/false, returnLoginStatus gives the actual message
        boolean success = login.loginUser(username, password);
        String message = login.returnLoginStatus(username, password);
        System.out.println(message);
        System.out.println();
    }

    // ==================================================================
    // Login class – contains the six required methods and stores users
    // ==================================================================
    static class Login {
        // Simple storage: an array of User objects (max 100 users)
        private User[] users = new User[100];
        private int userCount = 0;

        // --------------------------------------------------------------
        // 1. checkUserName – returns true if username has '_' and is ≤5 chars
        // --------------------------------------------------------------
        public boolean checkUserName(String username) {
            if (username == null) return false;
            return username.contains("_") && username.length() <= 5;
        }

        // --------------------------------------------------------------
        // 2. checkPasswordComplexity – strong password rules:
        //    at least 8 chars, one capital, one number, one special char
        // --------------------------------------------------------------
        public boolean checkPasswordComplexity(String password) {
            if (password == null || password.length() < 8) return false;
            boolean hasCapital = false;
            boolean hasNumber = false;
            boolean hasSpecial = false;

            for (char c : password.toCharArray()) {
                if (c >= 'A' && c <= 'Z') hasCapital = true;
                else if (c >= '0' && c <= '9') hasNumber = true;
                else if (!(c >= 'a' && c <= 'z') && !(c >= '0' && c <= '9')) {
                    hasSpecial = true; // anything that's not a letter or digit
                }
            }
            return hasCapital && hasNumber && hasSpecial;
        }

        // --------------------------------------------------------------
        // 3. checkCellPhoneNumber – uses regex to validate SA international numbers
        //    Must start with +27 and then have 9 or 10 digits.
        //    Reference: https://www.regextester.com/94657 (ITU E.164 style)
        // --------------------------------------------------------------
        public boolean checkCellPhoneNumber(String phone) {
            if (phone == null) return false;
            // ^\\+27   : starts with +27
            // [0-9]{9,10} : then 9 or 10 digits
            String regex = "^\\+27[0-9]{9,10}$";
            return phone.matches(regex);
        }

        // --------------------------------------------------------------
        // 4. registerUser – validates everything, stores the user if valid,
        //    and returns the appropriate message (success or error)
        // --------------------------------------------------------------
        public String registerUser(String firstName, String lastName,
                                   String username, String password, String phone) {
            // Check each field using the other methods
            if (!checkUserName(username)) {
                return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
            }
            if (!checkPasswordComplexity(password)) {
                return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
            }
            if (!checkCellPhoneNumber(phone)) {
                return "Cell phone number incorrectly formatted or does not contain international code.";
            }
            // Make sure the username isn't already taken
            if (isUsernameTaken(username)) {
                return "Username already taken. Please choose another.";
            }

            // All good – create a new User and add it to the array
            User newUser = new User(firstName, lastName, username, password, phone);
            users[userCount] = newUser;
            userCount++;

            return "Registration successful! You can now log in.";
        }

        // Helper method that checks if a username already exists in our stored users
        private boolean isUsernameTaken(String username) {
            for (int i = 0; i < userCount; i++) {
                if (users[i].username.equals(username)) {
                    return true;
                }
            }
            return false;
        }

        // --------------------------------------------------------------
        // 5. loginUser – returns true if username & password match a stored user
        // --------------------------------------------------------------
        public boolean loginUser(String username, String password) {
            for (int i = 0; i < userCount; i++) {
                if (users[i].username.equals(username) && users[i].password.equals(password)) {
                    return true;
                }
            }
            return false;
        }

        // --------------------------------------------------------------
        // 6. returnLoginStatus – returns the welcome message if login succeeds,
        //    otherwise returns the error message.
        // --------------------------------------------------------------
        public String returnLoginStatus(String username, String password) {
            for (int i = 0; i < userCount; i++) {
                if (users[i].username.equals(username) && users[i].password.equals(password)) {
                    return "Welcome " + users[i].firstName + ", " + users[i].lastName +
                           " it is great to see you again.";
                }
            }
            return "Username or password incorrect, please try again.";
        }

        // --------------------------------------------------------------
        // Inner class to represent a single registered user
        // --------------------------------------------------------------
        private static class User {
            String firstName;
            String lastName;
            String username;
            String password;
            String phoneNumber;

            User(String firstName, String lastName, String username, String password, String phoneNumber) {
                this.firstName = firstName;
                this.lastName = lastName;
                this.username = username;
                this.password = password;
                this.phoneNumber = phoneNumber;
            }
        }
    }
}
