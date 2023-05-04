package carsharing;

public class Car {

  private final int id;
  private String name;
  private int companyId;
  boolean free;


  public Car(int id, String name, int companyId, boolean free) {
    this.id = id;
    this.name = name;
    this.companyId = companyId;
    this.free = free;
  }

  public Car(int id, String name, int companyId) {
    this(id, name, companyId, true);
  }

  public Car(String name, int companyId) {
    this(0, name, companyId);
  }


  public int getId() {
    return id;
  }


  public String getName() {
    return name;
  }

  public int getCompanyId() {
    return companyId;
  }

  public boolean isFree() {
    return free;
  }

  @Override
  public String toString() {
    return String.format("%s(id: %d, company id: %d, free: %b)", name, id, companyId, free);
  }
}
