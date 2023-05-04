package carsharing.db;

import carsharing.Car;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DbCarDao implements CarDao {

  private static final String SELECT_ALL =
      "SELECT CAR.ID, CAR.NAME, COMPANY_ID, CUSTOMER_ID "
          + "FROM CAR LEFT OUTER JOIN CUSTOMER AS CUSTOMER (CUSTOMER_ID, CUSTOMER_NAME, RENTED_CAR_ID) "
          + "ON CAR.ID = RENTED_CAR_ID "
          + "WHERE COMPANY_ID = %d "
          + "ORDER BY CAR.ID;";

  private static final String SELECT_ALL_FREE =
      "SELECT CAR.ID, CAR.NAME, COMPANY_ID, CUSTOMER_ID "
          + "FROM CAR LEFT OUTER JOIN CUSTOMER AS CUSTOMER (CUSTOMER_ID, CUSTOMER_NAME, RENTED_CAR_ID) "
          + "ON CAR.ID = RENTED_CAR_ID "
          + "WHERE COMPANY_ID = %d AND CUSTOMER_ID IS NULL "
          + "ORDER BY CAR.ID;";
  private static final String SELECT_BY_ID =
      "SELECT CAR.ID, CAR.NAME, COMPANY_ID, CUSTOMER_ID "
          + "FROM CAR LEFT OUTER JOIN CUSTOMER AS CUSTOMER (CUSTOMER_ID, CUSTOMER_NAME, RENTED_CAR_ID) "
          + "ON CAR.ID = RENTED_CAR_ID "
          + "WHERE CAR.ID = %d;";
  private static final String INSERT_DATA = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES ('%s', %d);";

  // private static final String UPDATE_DATA = "UPDATE CAR SET NAME = '%s'  WHERE ID = %d";
  // private static final String DELETE_DATA = "DELETE FROM CAR WHERE ID = %d";


  @Override
  public List<Car> findAll(int companyId) {
    List<Map<String, String>> entities = DbClient.select(String.format(SELECT_ALL, companyId));
    if (entities == null) {
      System.out.println("Failed getting cars data");
      return null;
    }
    // cars.sort(Comparator.comparing(Car::getId));
    return toCars(entities);
  }

  private static List<Car> toCars(List<Map<String, String>> entities) {
    List<Car> cars = new ArrayList<>();
    for (Map<String, String> entity : entities) {
      String idString = entity.get("ID");
      int id = idString == null ? 0 : Integer.parseInt(idString);
      String name = entity.get("NAME");
      int companyId = Integer.parseInt(entity.get("COMPANY_ID"));
      boolean free = entity.get("CUSTOMER_ID") == null;
      cars.add(new Car(id, name, companyId, free));
    }
    return cars;
  }

  @Override
  public List<Car> findAllFree(int companyId) {
    List<Map<String, String>> entities = DbClient.select(String.format(SELECT_ALL_FREE, companyId));
    if (entities == null) {
      System.out.println("Failed getting cars data");
      return null;
    }
    return toCars(entities);
  }

  @Override
  public Optional<Car> findById(int id) {
    List<Map<String, String>> entities = DbClient.select(String.format(SELECT_BY_ID, id));
    if (entities == null) {
      System.out.println("Failed getting data about car with id = " + id);
      return Optional.empty();
    }
    if (entities.size() == 0) {
      return Optional.empty();
    }
    if (entities.size() > 1) {
      System.out.println("Multiple entities found for id: " + id);
    }
    List<Car> cars = toCars(entities);
    return Optional.of(cars.get(0));
  }

  @Override
  public boolean add(Car car) {
    try {
      DbClient.execute(String.format(INSERT_DATA, car.getName(), car.getCompanyId()));
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

//   public boolean update(Car car, String[] params) {
//     try {
//       DbClient.execute(String.format(INSERT_DATA, car.getName(), car.getCompanyId()));
//       return true;
//     } catch (SQLException e) {
//       e.printStackTrace();
//     }
//     return false;
//   }
  // boolean deleteById(int id);
}
