package carsharing.db;

import carsharing.Car;
import java.util.List;
import java.util.Optional;

public interface CarDao {
  List<Car> findAll(int companyId);
  List<Car> findAllFree(int companyId);
  Optional<Car> findById(int id);
  boolean add(Car car);
  // boolean update(Car customer);
  // boolean deleteById(int id);

  /*
  Optional<T> get(long id);
  List<T> getAll();
  void save(T t);
  void update(T t, String[] params);
  void delete(T t);

  getCompanies()				          <Company> getAll()
  addCompany(String name)		      void save(Company)
  getCompanyName(int companyId))	Optional<Company> get(int id)

  addCar(int companyId, String carName)	void save(Car)
  getCars(int companyId)			->
  getFreeCars(int companyId)		->
  getCar(int customerId)			Optional<Car> get(int id)
  returnCar(int customerId)		void update(Car, String[] params);
  rentCar(int customerId, int carID)	//-//

  getCustomers()				<Customer> getAll()
  addCustomer(String name)		void save(Customer)

   */

}
