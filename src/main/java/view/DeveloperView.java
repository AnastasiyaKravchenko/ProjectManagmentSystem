package view;

import dao.jdbc.DeveloperDAOImpl;
import dao.jdbc.SkillDAOImpl;
import exceptions.DeleteException;
import exceptions.ItemExistException;
import exceptions.NoItemToUpdateException;
import model.Developer;
import model.Skill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class DeveloperView extends View {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeveloperView.class);

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private String input;
    private DeveloperDAOImpl developerDAO = DeveloperDAOImpl.getInstance();
    private SkillDAOImpl skillDAO = SkillDAOImpl.getInstance();

    void displayDevMenu() {
        int choice = 0;
        printLine();
        System.out.println("Please select option:");
        System.out.println("1 - Display all available developers");
        System.out.println("2 - Insert new developer");
        System.out.println("3 - Update developer by ID");
        System.out.println("4 - Delete developer by ID");
        System.out.println("5 - Return to main menu");
        printExitMessage();
        printLine();
        System.out.print("Please enter your choice:");

        try {
            input = reader.readLine();
        } catch (IOException e) {
            LOGGER.error("IOException occurred:" + e.getMessage());
            displayDevMenu();
        }
        isQuitInput(input, LOGGER);

        try {
            choice = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            LOGGER.error("NumberFormatException occurred:" + e.getMessage());
            System.out.println("An incorrect value. Please try again.");
            displayDevMenu();
        }

        if (choice == 1) {
            displayAll();
        } else if (choice == 2) {
            insertDev();
        } else if (choice == 3) {
            updateDev();
        } else if (choice == 4) {
            deleteDev();
        } else if (choice == 5) {
            new ConsoleHelper().displayStartMenu();
        } else {
            System.out.println("An incorrect value. Please try again.");
            displayDevMenu();
        }
    }

    private void displayAll() {
        developerDAO.getAll().stream().sorted((d1, d2) -> d1.getId() - d2.getId()).forEach(System.out::println);
        displayDevMenu();
    }

    private void updateDev() {//TODO
        Developer developer = new Developer();
        printLine();
        System.out.print("Please enter skill ID to update: ");
        try {
            input = reader.readLine();
        } catch (IOException e) {
            LOGGER.error("IOException occurred:" + e.getMessage());
            new ConsoleHelper().displayStartMenu();
        }

        try {
            developer.setId(Integer.valueOf(input));
            System.out.print("Please input new description of developer:");
            try {
                input = reader.readLine();
            } catch (IOException e) {
                LOGGER.error("IOException occurred:" + e.getMessage());
            }
            developerDAO.update(developer);
        } catch (NumberFormatException e) {
            LOGGER.error("NumberFormatException occurred:" + e.getMessage());
            System.out.println("An incorrect value. Please try again.");
        } catch (NoItemToUpdateException e) {
            LOGGER.error("NoItemToUpdateException" + e.getMessage());
            System.out.println("There is no skill to update with requested id:" + developer.getId());
        }
        displayDevMenu();
    }

    private void insertDev() {
        printLine();
        Developer developer = new Developer();
        try {
            System.out.print("Please enter ID of new developer: ");
            developer.setId(Integer.valueOf(reader.readLine()));
            if (developerDAO.isExistDeveloper(developer.getId())){
                System.out.print("There is already developer with id: " + developer.getId());
                displayDevMenu();
            }
            System.out.print("Please enter Name of new developer: ");
            developer.setName(reader.readLine());
            System.out.print("Please enter Age of new developer: ");
            developer.setAge(Integer.valueOf(reader.readLine()));
            System.out.print("Please enter Country of new developer: ");
            developer.setCountry(reader.readLine());
            System.out.print("Please enter City of new developer: ");
            developer.setCity(reader.readLine());
            System.out.print("Please enter Join Date of new developer (in format dd.mm.yyyy): ");
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            input =reader.readLine();
            Date date = format.parse(input);
            developer.setJoinDate(date);


            try {
                developer.setSkills(setDeveloperSkills());
                developerDAO.save(developer);
            } catch (ItemExistException e) {
                System.out.print("Cannot add " + developer + ". There is already developer with id:" + developer.getId());
            }
        } catch (NumberFormatException | ParseException e){
            LOGGER.error("NumberFormatException occurred:" + e.getMessage());
            System.out.println("An incorrect value. Please try again.");
        } catch (IOException e) {
            LOGGER.error("IOException occurred:" + e.getMessage());
        }
        displayDevMenu();
    }

    private List<Skill> setDeveloperSkills() throws IOException {
        List<Skill> skills = new ArrayList<>();
        printLine();

        while (true) {
            System.out.println("\nPlease choose id of skill to add as " +
                    "developer skill from list (to stop adding enter \"stop\"):");
            skillDAO.getAll().stream().sorted((s1, s2) -> s1.getId() - s2.getId()).forEach(System.out::println);
            System.out.print("Add skill with ID:");
            input = reader.readLine();
            if (input.toLowerCase().equals("stop")){
             break;
            } else {
                skills.add(skillDAO.getById(Integer.valueOf(input)));
            }
        }
        return skills;
    }

    private void deleteDev() {//TODO
        printLine();
        System.out.print("Please enter developer ID to delete: ");
        try {
            input = reader.readLine();
        } catch (IOException e) {
            LOGGER.error("IOException occurred:" + e.getMessage());
        }
        try {
            developerDAO.delete(developerDAO.getById(Integer.valueOf(input)));
        } catch (NumberFormatException e) {
            LOGGER.error("NumberFormatException occurred:" + e.getMessage());
            System.out.println("An incorrect developer ID value. Please try again.");
        } catch (DeleteException e) {
            LOGGER.error(e.getMessage());
        }
        displayDevMenu();
    }
}
