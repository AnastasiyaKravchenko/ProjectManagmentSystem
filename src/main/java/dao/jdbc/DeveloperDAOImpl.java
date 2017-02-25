package dao.jdbc;

import dao.DeveloperDAO;
import exceptions.DeleteException;
import exceptions.ItemExistException;
import exceptions.NoItemToUpdateException;
import model.Developer;
import model.Skill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeveloperDAOImpl extends DeveloperDAO {

    private static DeveloperDAOImpl instance;

    public static DeveloperDAOImpl getInstance() {
        if (instance == null) {
            instance = new DeveloperDAOImpl();
        }
        return instance;
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(DeveloperDAOImpl.class);

    private final static String UPDATE_SQL_QUERY = "UPDATE developers SET skill_description=? WHERE skill_id=?";
    private static final String DELETE_SQL_QUERY = "DELETE FROM skills WHERE skill_id = ? AND skill_description=?";
    private static final String INSERT_SQL_QUERY = "INSERT INTO skills(skill_id, skill_description) VALUES (?, ?)";
    private static final String GET_ALL_SQL_QUERY = "SELECT * FROM developers";
    private static final String GET_ALL_SKILLS_BY_DEV_ID = "SELECT skill_id, skill_description FROM skills AS sk " +
            "INNER JOIN dev_skills AS dsk ON skill_id = dsk.skills_id " +
            "WHERE dsk.developer_id = ?;";
    private static final String GET_BY_ID_SQL_QUERY = "SELECT * FROM developers WHERE id = ?";


    @Override
    public void save(Developer item) throws ItemExistException {

    }

    @Override
    public void delete(Developer item) throws DeleteException {

    }

    @Override
    public void update(Developer item) throws NoItemToUpdateException {

    }

    @Override
    public Developer getById(Integer id) {
        try (Connection connection = DBConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(GET_BY_ID_SQL_QUERY);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                try {
                    return createDeveloper(resultSet);
                } catch (SQLException e) {
                    LOGGER.error("Exception occurred while getting developer data: " + e.getMessage());
                    return null;
                }
            } else {
                LOGGER.error("NoSuchElementException occurred. Cannot find developer with id " + id);
                return null;
            }

        } catch (SQLException e) {
            LOGGER.error("Exception occurred while connecting to DB");
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Developer> getAll() {
        List<Developer> developers = new ArrayList<>();

        try (Connection connection = DBConnectionPool.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(GET_ALL_SQL_QUERY);

            while (resultSet.next()){
                developers.add(createDeveloper(resultSet));
            }

        } catch (SQLException e) {
            LOGGER.error("Exception occurred while connecting to DB");
            throw new RuntimeException(e);
        }
        return developers;
    }

    private List<Skill> getDeveloperSkills(Integer devId) throws SQLException {
        List<Skill> skills = new ArrayList<>();
        try (Connection connection = DBConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(GET_ALL_SKILLS_BY_DEV_ID);
            statement.setInt(1, devId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                skills.add(new Skill(resultSet.getInt("skill_id"), resultSet.getString("skill_description")));
            }
        } catch (SQLException e) {
            LOGGER.error("Exception occurred while getting skills for developer");
            throw e;
        }

        return skills;
    }

    private Developer createDeveloper(ResultSet resultSet) throws SQLException {
        Developer developer = new Developer();
        developer.setId(resultSet.getInt("developer_id"));
        developer.setName(resultSet.getString("name"));
        developer.setAge(resultSet.getInt("age"));
        developer.setCountry(resultSet.getString("country"));
        developer.setJoinDate(resultSet.getDate("join_date"));
        developer.setSkills(getDeveloperSkills(developer.getId()));
        return developer;
    }
}
