package view;

import dao.jdbc.SkillDAOImpl;
import exceptions.DeleteException;
import exceptions.ItemExistException;
import exceptions.NoItemToUpdateException;
import model.Skill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SkillView extends View {
    private static final Logger LOGGER = LoggerFactory.getLogger(SkillView.class);

    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static String input;
    private SkillDAOImpl skillDAO = SkillDAOImpl.getInstance();

    void displaySkillMenu() {
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
        isQuitInput(input, LOGGER);

        try {
            choice = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            LOGGER.error("NumberFormatException occurred:" + e.getMessage());
            System.out.println("An incorrect value. Please try again.");
            displaySkillMenu();
        }

        if (choice == 1) {
            skillDAO.getAll();
            displaySkillMenu();
        } else if (choice == 2) {
            insertSkill();
        } else if (choice == 3) {
            updateSkill();
        } else if (choice == 4) {
            deleteSkill();
        } else if (choice == 5) {
            new ConsoleHelper().displayStartMenu();
        } else {
            System.out.println("An incorrect value. Please try again.");
            new ConsoleHelper().displayStartMenu();
        }

    }

    private void updateSkill() {
        Skill skill = new Skill();
        printLine();
        System.out.print("Please enter skill ID to update: ");
        try {
            input = reader.readLine();
        } catch (IOException e) {
            LOGGER.error("IOException occurred:" + e.getMessage());
            new ConsoleHelper().displayStartMenu();
        }

        try {
            skill.setId(Integer.valueOf(input));
            System.out.print("Please input new description of skill:");
            try {
                input = reader.readLine();
            } catch (IOException e) {
                LOGGER.error("IOException occurred:" + e.getMessage());
                displaySkillMenu();
            }
            skillDAO.update(skill);
            displaySkillMenu();
        } catch (NumberFormatException e) {
            LOGGER.error("NumberFormatException occurred:" + e.getMessage());
            System.out.println("An incorrect value. Please try again.");
            displaySkillMenu();
        } catch (NoItemToUpdateException e) {
            LOGGER.error("NoItemToUpdateException" + e.getMessage());
            System.out.println("There is no skill to update with requested id:" + skill.getId());
            displaySkillMenu();
        }
    }

    private void insertSkill() {
        printLine();
        Skill skill = new Skill();
        System.out.print("Please enter the description of new skill: ");
        try {
            input = reader.readLine();
        } catch (IOException e) {
            LOGGER.error("IOException occurred:" + e.getMessage());
        }
        try {
            skillDAO.save(skill);
        } catch (ItemExistException e) {
            System.out.print("Cannot add " + skill + ". There is already skill with id:" + skill.getId());
        }
        displaySkillMenu();
    }

    private void deleteSkill() {
        printLine();
        System.out.print("Please enter skill ID to delete: ");
        try {
            input = reader.readLine();
        } catch (IOException e) {
            LOGGER.error("IOException occurred:" + e.getMessage());
            displaySkillMenu();
        }
        try {
            skillDAO.delete(skillDAO.getById(Integer.valueOf(input)));
        } catch (NumberFormatException e) {
            LOGGER.error("NumberFormatException occurred:" + e.getMessage());
            System.out.println("An incorrect value. Please try again.");
        } catch (DeleteException e) {
            LOGGER.error(e.getMessage());
        }
        displaySkillMenu();
    }
}


