package carsharing;

import carsharing.db.CarDao;
import carsharing.db.CompanyDao;
import carsharing.db.CustomerDao;
import carsharing.db.DbCarDao;
import carsharing.db.DbClient;
import carsharing.db.DbCompanyDao;
import carsharing.db.DbCustomerDao;
import java.util.List;
import java.util.Optional;

public class App {

  boolean running = true;
  CustomerDao customerDao = new DbCustomerDao();
  CarDao carDao = new DbCarDao();
  CompanyDao companyDao = new DbCompanyDao();
  DbClient dbClient;

  final String MAIN_MENU = """
      1. Log in as a manager
      2. Log in as a customer
      3. Create a customer
      0. Exit""";
  final String COMPANIES_MENU = """
      1. Company list
      2. Create a company
      0. Back""";

  final String ONE_COMPANY_MENU = """
      1. Car list
      2. Create a car
      0. Back""";

  final String RENT_MENU = """
      1. Rent a car
      2. Return a rented car
      3. My rented car
      0. Back""";

  void start(String dbName) {
    startDB(dbName);
    while (running) {
      handleMainMenu();
    }
  }

  private void startDB(String dbName) {
    if (dbName == null) {
      dbClient = new DbClient();
    } else {
      dbClient = new DbClient(dbName);
    }
  }

  private void handleMainMenu() {
    System.out.println(MAIN_MENU);
    int action = InputManager.getIntInput();
    switch (action) {
      case 1 -> handleManagerMainMenu();
      case 2 -> handleCustomerLogIn();
      case 3 -> createCustomer();
      case 0 -> exit();
      default -> System.out.println("Please enter 1 or 0");
    }
    System.out.println();
  }

  private void handleManagerMainMenu() {
    System.out.println();
    while (true) {
      System.out.println(COMPANIES_MENU);
      int action = InputManager.getIntInput();
      System.out.println();
      switch (action) {
        case 1: handleManagerCompaniesMenu();
                break;
        case 2: createCompany();
                break;
        case 0: return;
        default:
          System.out.println("Please enter 1, 2 or 0");
      }
      System.out.println();
    }
  }

  private void handleManagerCompaniesMenu() {
    List<Company> companies = companyDao.findAll();
    if (companies == null) {
      System.out.println("Error getting data from DB");
      return;
    }
    if (companies.size() == 0) {
      System.out.println("The company list is empty!");
      return;
    }
    printChooseCompanyMenu(companies);

    int action;
    while (true) {
      action = InputManager.getIntInput();
      if (action > companies.size() || action < 0) {
        System.out.println("Please enter a number between 0 and " + companies.size() + ".");
        continue;
      }
      if (action == 0) {
        return;
      }
      break;
    }
    System.out.println();
    handleManagerOneCompanyMenu(companies.get(action - 1));
  }

  private static void printChooseCompanyMenu(List<Company> companies) {
    System.out.println("Choose the company:");
    for (int i = 0; i < companies.size(); i++) {
      System.out.printf("%d. %s\n", i + 1, companies.get(i).getName());
    }
    System.out.println("0. Back");
  }

  private void createCompany() {
    System.out.println("Enter the company name:");
    String name;
    while (true) {
      name = InputManager.getInput();
      if (name == null) {
        System.out.println("Failed getting name input");
        return;
      }
      if (name.length() == 0) {
        System.out.println("Please enter the company name");
        continue;
      }
      try {
        boolean added = companyDao.add(new Company(name));
        if (added) {
          System.out.println("The company was created!");
        } else {
          System.out.println("Failed to add a company");
        }
        return;
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  private void handleManagerOneCompanyMenu(Company company) {
    System.out.printf("'%s' company%n", company.getName());
    int action;
    while (true) {
      System.out.println(ONE_COMPANY_MENU);
      action = InputManager.getIntInput();
      switch (action) {
        case 1: showCarList(company.getId());
          break;
        case 2: createCar(company.getId());
          break;
        case 0: return;
        default:
          System.out.println("Please enter an action's number.");
          continue;
      }
      System.out.println();
    }
  }

  private void showCarList(int companyId) {
    List<Car> cars = carDao.findAll(companyId);
    System.out.println();
    if (cars == null) {
      System.out.println("Error getting data from DB");
      return;
    }
    if (cars.size() == 0) {
      System.out.println("The car list is empty!");
      return;
    }

    System.out.println("Car list:");
    for (int i = 0; i < cars.size(); i++) {
      System.out.printf("%d. %s\n", i + 1, cars.get(i).getName());
    }
  }

  private void createCar(int companyId) {
    System.out.println();
    System.out.println("Enter the car name:");
    String carName;
    while (true) {
      carName = InputManager.getInput();
      if (carName == null) {
        System.out.println("Error input for car name");
        return;
      }
      if (carName.length() == 0) {
        System.out.println("Please enter the car name");
        continue;
      }
      try {
        boolean added = carDao.add(new Car(0, carName, companyId));
        if (added) {
          System.out.println("The car was added!");
          return;
        } else {
          System.out.println("Failed to add a car");
        }
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
    }

  }


  private void handleCustomerLogIn() {
    System.out.println();
    List<Customer> customers = customerDao.findAll();
    if (customers == null) {
      System.out.println("Error getting data from DB");
      return;
    }
    if (customers.size() == 0) {
      System.out.println("The customer list is empty!");
      return;
    }
    System.out.println("Choose a customer:");
    for (int i = 0; i < customers.size(); i++) {
      System.out.printf("%d. %s\n", i + 1, customers.get(i).getName());
    }
    System.out.println("0. Back");

    int action;
    while (true) {
      action = InputManager.getIntInput();
      if (action > customers.size() || action < 0) {
        System.out.println("Please enter a number between 0 and " + customers.size() + ".");
        continue;
      }
      if (action == 0) {
        return;
      }
      break;
    }
    System.out.println();
    handleRentMenu(action);
  }

  private void createCustomer() {
    System.out.println();
    System.out.println("Enter the customer name:");
    String name;
    while (true) {
      name = InputManager.getInput();
      assert name != null;
      if (name.isBlank()) {
        System.out.println("Please enter the customer's name");
        continue;
      }
      try {
        Customer customer = new Customer(name);
        boolean added = customerDao.add(customer);
        if (added) {
          System.out.println("The customer was added!");
          System.out.println();
          return;
        } else {
          System.out.println("Failed adding customer: " + name);
        }
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  private void handleRentMenu(int customerId) {
    int action;
    while (true) {
      System.out.println(RENT_MENU);
      action = InputManager.getIntInput();
      switch (action) {
        case 1: rentCar(customerId);
          break;
        case 2: returnCar(customerId);
          break;
        case 3: showRentedCars(customerId);
          break;
        case 0: return;
        default:
          System.out.println("Please enter an action's number.");
          continue;
      }
      System.out.println();
    }

  }

  private void rentCar(int customerId) {
    System.out.println();
    Optional<Customer> optionalCustomer = customerDao.findById(customerId);
    if (optionalCustomer.isEmpty()) {
      System.out.println("Failed getting customer info for customer id = " + customerId);
      return;
    }
    Customer customer = optionalCustomer.get();

    if (customer.hasRentedCar()) {
      System.out.println("You've already rented a car!");
      return;
    }

    List<Company> companies = companyDao.findAll();
    if (companies == null) {
      System.out.println("Error getting data from DB");
      return;
    }
    if (companies.size() == 0) {
      System.out.println("The company list is empty!");
      return;
    }
    printChooseCompanyMenu(companies);

    int action;
    while (true) {
      action = InputManager.getIntInput();
      if (action > companies.size() || action < 0) {
        System.out.println("Please enter a number between 0 and " + companies.size() + ".");
        continue;
      }
      if (action == 0) {
        return;
      }
      break;
    }
    System.out.println();

    Car car = handleChooseCarMenu(companies.get(action - 1));
    if (car == null) {
      return;
    }
    customer.setRentedCarId(car.getId());
    boolean updated = customerDao.update(customer);
    if (updated) {
      System.out.printf("You rented '%s'%n", car.getName());
    } else {
      System.out.println("Failed to update customer rent data");
    }
  }

  private Car handleChooseCarMenu(Company company) {
    List<Car> cars = carDao.findAllFree(company.getId());
    if (cars.size() == 0) {
      System.out.println("The car list is empty!");
      return null;
    }
    printChooseCarMenu(cars);

    int action;
    while (true) {
      action = InputManager.getIntInput();
      if (action > cars.size() || action < 0) {
        System.out.println("Please enter a number between 0 and " + cars.size() + ".");
        continue;
      }
      if (action == 0) {
        return null;
      }
      break;
    }
    System.out.println();
    return cars.get(action - 1);
  }

  private static void printChooseCarMenu(List<Car> cars) {
    System.out.println("Choose a car:");
    for (int i = 0; i < cars.size(); i++) {
      System.out.printf("%d. %s\n", i + 1, cars.get(i).getName());
    }
    System.out.println("0. Back");
  }


  private void returnCar(int customerId) {
    System.out.println();
    Optional<Customer> optionalCustomer = customerDao.findById(customerId);
    if (optionalCustomer.isEmpty()) {
      System.out.println("Failed getting customer data for id = " + customerId);
      return;
    }
    Customer customer = optionalCustomer.get();
    if (customer.getRentedCarId() == 0) {
      System.out.println("You didn't rent a car!");
      return;
    }
    System.out.println();
    customer.setRentedCarId(0);
    boolean updated = customerDao.update(customer);
    if (updated) {
      System.out.println("You've returned a rented car!");
    } else {
      System.out.println("Failed updating customer rent car id");
    }
  }

  private void showRentedCars(int customerId) {
    System.out.println();
    Optional<Customer> optionalCustomer = customerDao.findById(customerId);
    if (optionalCustomer.isEmpty()) {
      System.out.println("Failed getting customer info for customer id = " + customerId);
      return;
    }
    Customer customer = optionalCustomer.get();

    Optional<Car> optionalCar = carDao.findById(customer.getRentedCarId());
    if (optionalCar.isEmpty()) {
      System.out.println("You didn't rent a car!");
      return;
    }
    Car car = optionalCar.get();
    String carName = car.getName();
    Optional<Company> optionalCompany = companyDao.findById(car.getCompanyId());
    if (optionalCompany.isEmpty()) {
      System.out.println("Inconsistent company - car pair");
      return;
    }
    Company company = optionalCompany.get();
    String companyName = company.getName();

    System.out.println("Your rented car:");
    System.out.println(carName);
    System.out.println("Company:");
    System.out.println(companyName);
  }

  private void exit() {
    System.out.println("Bye!");
    running = false;
  }

}
