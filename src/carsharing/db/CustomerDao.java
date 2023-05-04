package carsharing.db;

import carsharing.Customer;
import java.util.List;
import java.util.Optional;

public interface CustomerDao {
  List<Customer> findAll();
  Optional<Customer> findById(int id);
  boolean add(Customer customer);
  boolean update(Customer customer);
  // void deleteById(int id);

  /*
  Optional<T> get(long id);
  List<T> getAll();
  void save(T t);
  void update(T t, String[] params);
  void delete(T t);

  getCustomers()				List<Customer> getAll()
  addCustomer(String name)		void save(Customer)
   */

}
