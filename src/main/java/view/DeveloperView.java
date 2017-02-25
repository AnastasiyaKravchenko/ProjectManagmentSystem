package view;

import dao.jdbc.DeveloperDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DeveloperView {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeveloperView.class);

    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static String input;
    private DeveloperDAOImpl developerDAO = DeveloperDAOImpl.getInstance();

    public void displayDevMenu() {

    }
}
