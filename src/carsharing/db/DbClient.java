package carsharing.db;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DbClient {

  // JDBC driver name and database URL
  static final String JDBC_DRIVER = "org.h2.Driver";
  //static final String DB_URL = "jdbc:h2:file:../task/src/carsharing/db/carsharing";
  // static String DB_URL = "jdbc:h2:file:~/Java_Study/idea/JetBrains_my/Car Sharing/Car Sharing/task/src/carsharing/db/carsharing";
  static String DB_URL;
  static final String DB_URL_PREFIX = "jdbc:h2:file:~/Java_Study/idea/JetBrains_my/Car Sharing/Car Sharing/task/src/carsharing/db/";
  static final String PREDEFINED_DB_NAME = "carsharing";

  public DbClient(String dbName) {
    DB_URL = DB_URL_PREFIX + Objects.requireNonNullElse(dbName, PREDEFINED_DB_NAME);

    try {
      // STEP 1: Register JDBC driver
      Class.forName(JDBC_DRIVER);

      //STEP 2: Open a connection
      //System.out.println("Connecting to database...");
      try (Connection conn = DriverManager.getConnection(DB_URL)) {
        conn.setAutoCommit(true);

        //STEP 3: Execute a query
        //System.out.println("Creating table in given database...");
        try (Statement stmt = conn.createStatement()) {
          String sql;
//          sql = "DROP TABLE IF EXISTS CUSTOMER";
//          stmt.executeUpdate(sql);
//          sql = "DROP TABLE IF EXISTS CAR";
//          stmt.executeUpdate(sql);
//          sql = "DROP TABLE IF EXISTS COMPANY";
//          stmt.executeUpdate(sql);
          sql = "CREATE TABLE IF NOT EXISTS COMPANY " +
              "(ID INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
              "NAME VARCHAR(255) UNIQUE NOT NULL)";
          //" PRIMARY KEY (ID))";
          stmt.executeUpdate(sql);
          sql = "CREATE TABLE IF NOT EXISTS CAR " +
              "(ID INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
              "NAME VARCHAR(255) UNIQUE NOT NULL, " +
              "COMPANY_ID INTEGER NOT NULL, " +
              "CONSTRAINT fk_company_id FOREIGN KEY (COMPANY_ID) " +
              "REFERENCES COMPANY(ID) " +
              "ON DELETE CASCADE " +
              "ON UPDATE CASCADE)";
          stmt.executeUpdate(sql);
          sql = "CREATE TABLE IF NOT EXISTS CUSTOMER " +
              "(ID INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
              "NAME VARCHAR(255) UNIQUE NOT NULL, " +
              "RENTED_CAR_ID INTEGER DEFAULT NULL, " +
              "CONSTRAINT fk_car_id FOREIGN KEY (RENTED_CAR_ID) " +
              "REFERENCES CAR(ID) " +
              "ON DELETE SET NULL " +
              "ON UPDATE CASCADE);";
          stmt.executeUpdate(sql);
          //System.out.println("Created table in given database...");
        }
      }

    } catch (SQLException se) {
      //Handle errors for JDBC
      se.printStackTrace();

    } catch (Exception e) {
      //Handle errors for Class.forName
      e.printStackTrace();

    }
  }

  public DbClient() {
    this(PREDEFINED_DB_NAME);
  }


  // method to create and update data in database
  public static void execute(String sql) throws SQLException {
    try (Connection conn = DriverManager.getConnection(DB_URL);
        Statement statement = conn.createStatement()
    ) {
      conn.setAutoCommit(true);
      statement.executeUpdate(sql);
    }
//    catch (SQLException e) {
//      e.printStackTrace();
//    }
  }

/*
  public Optional<T> selectOne(String query) {
    List<T> entities = select(query);
    if (entities.size() == 1) {
      return Optional.of(entities.get(0));
    } else if (entities.size() == 0) {
      return Optional.empty();
    } else {
      throw new IllegalStateException("Query returned more than one object");
    }
  }
 */

  // method for selecting data.
  public static List<Map<String, String>> select(String query) {
    List<Map<String, String>> entities = new ArrayList<>();
    try (Connection conn = DriverManager.getConnection(DB_URL);
        Statement statement = conn.createStatement()
    ) {
      ResultSet rs = statement.executeQuery(query);
      ResultSetMetaData rsmd = rs.getMetaData();
      int columnCount = rsmd.getColumnCount();

      while (rs.next()) {
        Map<String, String> entity = new HashMap<>();
        for (int i = 1; i <= columnCount; i++) {
          String column = rsmd.getColumnName(i);
          String value = rs.getString(i);
          entity.put(column, value);
        }
        entities.add(entity);
      }
      return entities;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

}
