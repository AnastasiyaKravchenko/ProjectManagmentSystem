package controller;

import dao.SkillDAO;
import exceptions.DeleteException;
import model.Skill;

public class SkillController {
    private static SkillDAO skillDAO = new SkillDAO();

    public static void getAll() {
        System.out.println("\n===============================================\n");
        System.out.println("List of all skills:");
        skillDAO.getAll().stream().sorted((o1, o2) -> o1.getId() - o2.getId()).forEach(System.out::println);
    }

    public static void update(Skill skill) {
        skillDAO.update(skill.getId(),skill);
    }

    public static void delete(int id) throws DeleteException {
        skillDAO.delete(id);
    }

    public static void insert(String description) {
        skillDAO.add(description);
    }
}
