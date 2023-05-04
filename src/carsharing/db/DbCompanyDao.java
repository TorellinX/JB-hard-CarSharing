package carsharing.db;

import carsharing.Company;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DbCompanyDao implements CompanyDao {

  private static final String SELECT_ALL = "SELECT * FROM COMPANY ORDER BY ID";
  private static final String INSERT_DATA = "INSERT INTO COMPANY (NAME) VALUES ('%s');";
  private static final String SELECT_BY_ID = "SELECT * FROM COMPANY WHERE ID = %d";
  // private static final String UPDATE_DATA = "UPDATE COMPANY SET NAME = '%s' WHERE ID = %d";
  // private static final String DELETE_DATA = "DELETE FROM COMPANY WHERE ID = %d";

  @Override
  public List<Company> findAll() {
    List<Map<String, String>> entities = DbClient.select(String.format(SELECT_ALL));
    if (entities == null) {
      System.out.println("Failed getting companies data");
      return null;
    }
    return toCompanies(entities);
  }

  private static List<Company> toCompanies(List<Map<String, String>> entities) {
    List<Company> companies = new ArrayList<>();
    for (Map<String, String> entity : entities) {
      int id = Integer.parseInt(entity.get("ID"));
      String name = entity.get("NAME");
      companies.add(new Company(id, name));
    }
    return companies;
  }

  @Override
  public Optional<Company> findById(int id) {
    List<Map<String, String>> entities = DbClient.select(String.format(SELECT_BY_ID, id));
    if (entities == null) {
      System.out.println("Failed getting data about company with id = " + id);
      return Optional.empty();
    }
    if (entities.size() == 0) {
      return Optional.empty();
    }
    if (entities.size() > 1) {
      System.out.println("Multiple entities found for id: " + id);
    }
    List<Company> companies = toCompanies(entities);
    return Optional.of(companies.get(0));
  }

  @Override
  public boolean add(Company company) {
    try {
      DbClient.execute(String.format(INSERT_DATA, company.getName()));
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }
}
