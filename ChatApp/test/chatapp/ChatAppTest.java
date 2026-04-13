package chatapp;

// ChatAppTest.java – JUnit 4 tests for the ChatApp login methods
// Tests every requirement: username, password, phone, registration, login, and status messages.

import org.junit.Test;
import static org.junit.Assert.*;

public class ChatAppTest {

    // We create a fresh Login object for each test
    private ChatApp.Login login = new ChatApp.Login();

    // ---------- Username tests ----------
    @Test
    public void testUserName_Valid() {
        // Valid: contains underscore and length ≤5
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void testUserName_Invalid() {
        // Invalid: no underscore and too long
        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }

    // ---------- Password tests ----------
    @Test
    public void testPassword_Valid() {
        // Strong password – meets all complexity rules
        assertTrue(login.checkPasswordComplexity("Ch&@sec@ke99!"));
    }

    @Test
    public void testPassword_Invalid() {
        // Weak password – "password" has no caps, no number, no special
        assertFalse(login.checkPasswordComplexity("password"));
    }

    // ---------- Cell phone tests ----------
    @Test
    public void testCellPhone_Valid() {
        // Valid SA international number
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testCellPhone_Invalid() {
        // No international code, too short
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }

    // ---------- Full registration test ----------
    @Test
    public void testRegisterUser_Success() {
        String result = login.registerUser("John", "Doe", "j_doe", "Pass123!", "+27731234567");
        assertEquals("Registration successful! You can now log in.", result);
        // Also verify that the user was actually stored
        assertTrue(login.loginUser("j_doe", "Pass123!"));
    }

    @Test
    public void testRegisterUser_DuplicateUsername() {
        // Register once
        login.registerUser("John", "Doe", "j_doe", "Pass123!", "+27731234567");
        // Try to register again with the same username
        String result = login.registerUser("Jane", "Smith", "j_doe", "Another1!", "+27821234567");
        assertEquals("Username already taken. Please choose another.", result);
    }

    // ---------- Login method tests ----------
    @Test
    public void testLoginUser_True() {
        login.registerUser("Alice", "Wonder", "al_w", "Secret1!", "+27731234567");
        assertTrue(login.loginUser("al_w", "Secret1!"));
    }

    @Test
    public void testLoginUser_False() {
        login.registerUser("Bob", "Builder", "b_b", "Build2@", "+27731234567");
        assertFalse(login.loginUser("b_b", "wrongpass"));
    }

    // ---------- returnLoginStatus tests (the actual messages) ----------
    @Test
    public void testReturnLoginStatus_WelcomeMessage() {
        login.registerUser("Charlie", "Brown", "c_b", "Ch@rlie1", "+27731234567");
        String msg = login.returnLoginStatus("c_b", "Ch@rlie1");
        assertEquals("Welcome Charlie, Brown it is great to see you again.", msg);
    }

    @Test
    public void testReturnLoginStatus_ErrorMessage() {
        login.registerUser("Diana", "Prince", "d_p", "Wond3r!", "+27731234567");
        String msg = login.returnLoginStatus("d_p", "wrong");
        assertEquals("Username or password incorrect, please try again.", msg);
    }
}