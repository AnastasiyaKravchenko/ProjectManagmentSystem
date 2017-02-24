package view;

import controller.SkillController;
import exceptions.DeleteException;
import model.Skill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Menu {
    private static final Logger LOGGER = LoggerFactory.getLogger(Menu.class);

    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static String input;

    public Menu() {
    }

    public static void displayStartMenu() {
        int choice = 0;
        printLine();
        System.out.println("What data you want to operate with?");
        System.out.println("1 - Developers");
        System.out.println("2 - Skills");
        System.out.println("3 - Companies");
        System.out.println("4 - Projects");
        System.out.println("5 - Customers");
        printExitMessage();
        printLine();
        System.out.print("Please enter your choice:");

        try {
            input = reader.readLine();
        } catch (IOException e) {
            LOGGER.error("IOException occurred:" + e.getMessage());
            displayStartMenu();
        }
        isQuitInput();

        try {
            choice = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            LOGGER.error("NumberFormatException occurred:" + e.getMessage());
            System.out.println("An incorrect value. Please try again.");
            displayStartMenu();
        }

        if (choice == 1) {
            displayDevMenu();
        } else if (choice == 2) {
            displaySkillMenu();
        } else if (choice == 3) {
            displayCompanyMenu();
        } else if (choice == 4) {
            displayProjectsMenu();
        } else if (choice == 5) {
            displayCustomersMenu();
        } else {
            System.out.println("An incorrect value. Please try again.");
            displayStartMenu();
        }

    }

    private static void displayCustomersMenu() {

    }

    private static void displayProjectsMenu() {

    }

    private static void displayCompanyMenu() {

    }

    private static void displaySkillMenu() {
        int choice = 0;
        printLine();
        System.out.println("Please select option:");
        System.out.println("1 - Display all available skills");
        System.out.println("2 - Insert new skill");
        System.out.println("3 - Update skill by ID");
        System.out.println("4 - Delete skill by ID");
        System.out.println("5 - Return to main menu");
        printExitMessage();
        printLine();
        System.out.print("Please enter your choice:");

        try {
            input = reader.readLine();
        } catch (IOException e) {
            LOGGER.error("IOException occurred:" + e.getMessage());
            displaySkillMenu();
        }
        isQuitInput();

        try {
            choice = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            LOGGER.error("NumberFormatException occurred:" + e.getMessage());
            System.out.println("An incorrect value. Please try again.");
            displaySkillMenu();
        }

        if (choice == 1) {
            SkillController.getAll();
            displaySkillMenu();
        } else if (choice == 2) {
            insertSkill();
        } else if (choice == 3) {
            updateSkill();
        } else if (choice == 4) {
            deleteSkill();
        } else if (choice == 5) {
            displayStartMenu();
        } else {
            System.out.println("An incorrect value. Please try again.");
            displayStartMenu();
        }

    }

    private static void updateSkill() {
        printLine();
        System.out.print("Please enter skill ID to update: ");
        try {
            input = reader.readLine();
        } catch (IOException e) {
            LOGGER.error("IOException occurred:" + e.getMessage());
            displaySkillMenu();
        }
        try {
            int id = Integer.valueOf(input);
            System.out.print("Please input new description of skill:");
            try {
                input = reader.readLine();
            } catch (IOException e) {
                LOGGER.error("IOException occurred:" + e.getMessage());
                displaySkillMenu();
            }
            SkillController.update(new Skill(id,input));
            displaySkillMenu();
        } catch (NumberFormatException e) {
            LOGGER.error("NumberFormatException occurred:" + e.getMessage());
            System.out.println("An incorrect value. Please try again.");
            displaySkillMenu();
        }
    }

    private static void insertSkill() {
        printLine();
        System.out.print("Please enter the description of new skill: ");
        try {
            input = reader.readLine();
        } catch (IOException e) {
            LOGGER.error("IOException occurred:" + e.getMessage());
            displaySkillMenu();
        }
        SkillController.insert(input);
        displaySkillMenu();
    }

    private static void deleteSkill() {
        printLine();
        System.out.print("Please enter skill ID to delete: ");
        try {
            input = reader.readLine();
        } catch (IOException e) {
            LOGGER.error("IOException occurred:" + e.getMessage());
            displaySkillMenu();
        }
        try {
            SkillController.delete(Integer.valueOf(input));
            displaySkillMenu();
        } catch (NumberFormatException e) {
            LOGGER.error("NumberFormatException occurred:" + e.getMessage());
            System.out.println("An incorrect value. Please try again.");
            displaySkillMenu();
        } catch (DeleteException e){
            LOGGER.error(e.getMessage());
            displaySkillMenu();
        }
    }

    private static void displayDevMenu() {

    }

    private static void isQuitInput() {
        if (input.toLowerCase().equals("quit")) {
            LOGGER.info("Application shutdown");
            System.exit(0);
        }
    }

    private static void printExitMessage() {
        System.out.println("To close application please enter \"Quit\"");
    }

    private static void printLine() {
        System.out.println("\n===============================================\n");
    }
}
