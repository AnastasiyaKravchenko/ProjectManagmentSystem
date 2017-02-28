package view;

import dao.jdbc.ProjectDAOImpl;
import exceptions.DeleteException;
import exceptions.ItemExistException;
import exceptions.NoItemToUpdateException;
import model.Project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ProjectView extends View {
    public void displayProjectsMenu() {



    }

    private static Logger logger = LoggerFactory.getLogger(Project.class);
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        boolean con = true;
        while (con) {
            System.out.println("1 - delete\n2 - update\n3 - select by id\n4 - insert\n5 - select all\nInput number of operation: ");

            ProjectDAOImpl dao = new ProjectDAOImpl();
            List<Project> items = dao.getAll();

            int numberOfOperation = readInt(1, 5);
            if (numberOfOperation == 1 || numberOfOperation == 2) {
                printList(items);
                System.out.println("Input number of project: ");
                int elementNumber = readInt(1, items.size()) - 1;
                Project element = items.get(elementNumber);

                if (numberOfOperation == 1) {
                    try {
                        dao.delete(element);
                    } catch (DeleteException e) {
                        System.out.println("Cannot delete a project");
                    }
                }

                if (numberOfOperation == 2) {
                    System.out.println("Input new name:");
                    element.setName(readStr());
                    System.out.println("Input new description:");
                    element.setDescription(readStr());
                    try {
                        dao.update(element);
                    } catch (NoItemToUpdateException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (numberOfOperation == 3) {
                System.out.println("Input id:");
                int id = readInt();
                System.out.print("Project with id = " + id + ": ");
                System.out.println(dao.getById(id));
            }

            if (numberOfOperation == 4) {
                Project element = new Project();
                System.out.println("Input new name:");
                element.setName(readStr());
                System.out.println("Input new description:");
                element.setDescription(readStr());
                try {
                    dao.save(element);
                } catch (ItemExistException e) {
                    e.printStackTrace();
                }
            }

            if (numberOfOperation == 5) {
                System.out.println("Existent projects:");
                printList(items);
            }

            System.out.println("Do you want to continue?  (Y - yes/N - no) ");
            con = readBoolean();
        }
    }

    private static boolean readBoolean() {
        try {
            String s = reader.readLine();
            if (!s.isEmpty() && (s.toLowerCase().equals("y"))){
                return true;
            }
            if (!s.isEmpty() && (s.toLowerCase().equals("n"))){
                return false;
            }
        } catch (IOException e){
            System.out.println("Input failed");
        }
        System.out.print("You should input either \"y\" or \"n\". Try input again.\n");
        return readBoolean();
    }

    private static int readInt() {
        String str = readStr();
        try {
            int i = Integer.parseInt(str);
            return i;
        } catch (IllegalArgumentException e) {
            System.out.println("Value should an integer. Try input again: ");
            return readInt();
        }
    }

    private static int readInt(int min, int max) {
        int i = readInt();
        if (i <= max && i >= min) {
            return i;
        } else {
            System.out.println("Value should be between " + min + " and " + max + ". Try input again: ");
            return readInt(max, min);
        }
    }

    private static String readStr(){
        String str = null;
        try {
            return reader.readLine();
        } catch (IOException e) {
            System.out.println("Input failed");
            throw new RuntimeException(e);
        }
    }

    private static void printList(List<Project> list){
        for (int i=0; i<list.size(); i++){
            System.out.println((i+1)+". "+list.get(i).getName());
        }
    }
}