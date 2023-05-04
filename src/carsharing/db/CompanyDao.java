package carsharing.db;

import carsharing.Company;
import java.util.List;
import java.util.Optional;

public interface CompanyDao {

  List<Company> findAll();

  Optional<Company> findById(int id);

  boolean add(Company company);

  // boolean update(Company company, String[] params);
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
   */

}
