package carsharing.db;

import carsharing.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DbCustomerDao implements CustomerDao {

  private static final String SELECT_ALL = "SELECT * FROM CUSTOMER ORDER BY ID";
  private static final String INSERT_DATA = "INSERT INTO CUSTOMER (NAME) VALUES ('%s');";
  private static final String SELECT_BY_ID = "SELECT * FROM CUSTOMER WHERE ID = %d";
  private static final String UPDATE_DATA = "UPDATE CUSTOMER SET NAME = '%s', RENTED_CAR_ID = %d WHERE ID = %d";
  private static final String UPDATE_SET_NULL = "UPDATE CUSTOMER SET NAME = '%s', RENTED_CAR_ID = NULL WHERE ID = %d";
  // private static final String DELETE_DATA = "DELETE FROM CUSTOMER WHERE ID = %d";


  @Override
  public List<Customer> findAll() {
    List<Customer> customers = new ArrayList<>();
    List<Map<String, String>> entities = DbClient.select(SELECT_ALL);
    if (entities == null) {
      System.out.println("Failed getting customers data");
      return null;
    }
    for (Map<String, String> entity : entities) {
      int id = Integer.parseInt(entity.get("ID"));
      String name = entity.get("NAME");
      String carIdString = entity.get("RENTED_CAR_ID");
      int carId = 0;
      if (carIdString != null) {
        carId = Integer.parseInt(carIdString);
      }
      customers.add(new Customer(id, name, carId));
    }
    // customers.sort(Comparator.comparing(Customer::getId));
    return customers;
  }

  @Override
  public Optional<Customer> findById(int id) {
    List<Map<String, String>> entities = DbClient.select(String.format(SELECT_BY_ID, id));
    if (entities == null) {
      System.out.println("Failed getting customers data");
      return Optional.empty();
    }
    if (entities.size() == 0) {
      return Optional.empty();
    }
    if (entities.size() > 1) {
      System.out.println("Multiple entities found for id: " + id);
    }
    Map<String, String> entity = entities.get(0);
      String name = entity.get("NAME");
      String carIdString = entity.get("RENTED_CAR_ID");
      int carId = 0;
      if (carIdString != null) {
        carId = Integer.parseInt(carIdString);
      }
      return Optional.of(new Customer(id, name, carId));
  }


  @Override
  public boolean add(Customer customer) {
    try {
      DbClient.execute(String.format(INSERT_DATA, customer.getName()));
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean update(Customer customer) {
    try {
      if (customer.getRentedCarId() == 0) {
        DbClient.execute(String.format(UPDATE_SET_NULL, customer.getName(), customer.getId()));
        return true;
      }
      DbClient.execute(String.format(UPDATE_DATA, customer.getName(), customer.getRentedCarId(),
          customer.getId()));
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  // void deleteById(int id);
}
