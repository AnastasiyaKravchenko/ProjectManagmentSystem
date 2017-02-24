package dao;

import exceptions.DeleteException;
import model.Skill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class SkillDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(SkillDAO.class);

    private String url = "jdbc:mysql://localhost:3306/javaee_hw2?useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private String user = "user";
    private String password = "qwe123";

    public SkillDAO() {
        loadDriver();
    }

    public void update(int id, Skill skillToUpdate) throws RuntimeException {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            connection.setAutoCommit(false);
            PreparedStatement statement =
                    connection.prepareStatement("UPDATE skills SET skill_description=? WHERE skill_id=?");
            statement.setString(1, skillToUpdate.getDescription());
            statement.setInt(2, id);
            try {
                load(id);
                statement.execute();
                connection.commit();
                LOGGER.info("Skill description with ID=" + id + " updated to \"" + skillToUpdate.getDescription() + "\"");
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
            }
        } catch (SQLException e) {
            LOGGER.error("Exception occured while connecting to DB");
            throw new RuntimeException(e);
        }
    }

    public void delete(int id) throws DeleteException {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            connection.setAutoCommit(false);
            PreparedStatement statement =
                    connection.prepareStatement("DELETE FROM skills WHERE skill_id = ?");
            statement.setInt(1, id);
            try {
                statement.execute();
                connection.commit();
                LOGGER.info("Skill with id=" + id + " deleted");
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
                throw new DeleteException();
            }
        } catch (SQLException e) {
            LOGGER.error("Exception occurred while connecting to DB");
            throw new RuntimeException(e);
        }
    }


    public void add(String skillDesc) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            connection.setAutoCommit(false);
            PreparedStatement statement =
                    connection.prepareStatement("INSERT INTO skills(skill_description) VALUES (?)");
            statement.setString(1, skillDesc);
            statement.execute();
            connection.commit();
            LOGGER.info("Skill \""+ skillDesc+"\" added");
        } catch (SQLException e) {
            LOGGER.error("Exception occurred while connecting to DB");
            throw new RuntimeException(e);
        }
    }

    public List<Skill> getAll() {
        List<Skill> skillList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM skills");

            while (resultSet.next()) {
                Skill skill = createSkill(resultSet);
                skillList.add(skill);
            }

        } catch (SQLException e) {
            LOGGER.error("Exception occurred while connecting to DB");
            throw new RuntimeException(e);
        }
        return skillList;
    }

    public Skill load(int id) throws RuntimeException {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            PreparedStatement statement =
                    connection.prepareStatement("SELECT * FROM skills WHERE skill_id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return createSkill(resultSet);
            } else {
                LOGGER.error("NoSuchElementException occurred. Cannot find skill with id " + id);
                throw new NoSuchElementException("Cannot find skill with id " + id);
            }

        } catch (SQLException e) {
            LOGGER.error("Exception occurred while connecting to DB");
            throw new RuntimeException(e);
        }
    }

    public Skill createSkill(ResultSet resultSet) throws SQLException {
        return new Skill(resultSet.getInt("skill_id"), resultSet.getString("skill_description"));
    }

    private void loadDriver() {
        try {
            LOGGER.info("Loading JDBC driver: com.mysql.cj.jdbc.Driver");
            Class.forName("com.mysql.cj.jdbc.Driver");
            LOGGER.info("Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            LOGGER.error("Cannot find driver: com.mysql.cj.jdbc.Driver");
            throw new RuntimeException(e);
        }
    }

}
