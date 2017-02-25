package dao.jdbc;

import dao.DeveloperDAO;
import exceptions.DeleteException;
import exceptions.ItemExistException;
import exceptions.NoItemToUpdateException;
import model.Developer;

import java.util.List;

public class DeveloperDAOImpl extends DeveloperDAO {

    private static DeveloperDAOImpl instance;

    public static DeveloperDAOImpl getInstance(){
        if (instance==null){
            instance = new DeveloperDAOImpl();
        }
        return instance;
    }

    private final static String UPDATE_SQL_QUERY = "UPDATE skills SET skill_description=? WHERE skill_id=?";
    private static final String DELETE_SQL_QUERY = "DELETE FROM skills WHERE skill_id = ? AND skill_description=?";
    private static final String INSERT_SQL_QUERY = "INSERT INTO skills(skill_id, skill_description) VALUES (?, ?)";
    private static final String GET_ALL_SQL_QUERY = "SELECT * FROM skills";
    private static final String GET_BY_ID_SQL_QUERY = "SELECT * FROM skills WHERE skill_id = ?";



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
    public Developer getById(Integer integer) {
        return null;
    }

    @Override
    public List<Developer> getAll() {
        return null;
    }
}
