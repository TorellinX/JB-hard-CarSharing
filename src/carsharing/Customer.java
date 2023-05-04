package carsharing;

public class Customer {
  private final int id;
  private String name;
  private int rentedCarId;

  public Customer(int id, String name, int rentedCarId) {
    this.id = id;
    this.name = name;
    this.rentedCarId = rentedCarId;
  }

  public Customer(String name, int rentedCarId) {
    this(0, name, rentedCarId);
  }

  public Customer(String name) {
    this( name, 0);
  }

  public int getId() {
    return id;
  }


  public String getName() {
    return name;
  }

  public int getRentedCarId() {
    return rentedCarId;
  }

  public void setRentedCarId(int rentedCarId) {
    this.rentedCarId = rentedCarId;
  }

  boolean hasRentedCar() {
    return this.rentedCarId != 0;
  }

  @Override
  public String toString() {
    return String.format("%s(id:%d, car:%d)", name, id, rentedCarId);
  }
}
