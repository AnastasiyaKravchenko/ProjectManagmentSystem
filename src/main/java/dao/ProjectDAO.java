package dao;

public class ProjectDAO {
    private static ProjectDAO ourInstance = new ProjectDAO();

    public static ProjectDAO getInstance() {
        return ourInstance;
    }

    private ProjectDAO() {
    }
}
