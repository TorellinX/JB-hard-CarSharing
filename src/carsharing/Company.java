package carsharing;

public class Company {

  private final int id;
  private String name;

  public Company(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public Company(String name) {
    this(0, name);
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return String.format("%s(%d)", name, id);
  }
}
