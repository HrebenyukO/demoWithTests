package com.example.demowithtests.repository;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.dto.EmployeeDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Indexed;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query(value = "select e from Employee e where e.country =?1")
    @EntityGraph(attributePaths = {"addresses"})
    List<Employee> findEmployeesByCountry(String country);

    @Query(value = "select e from Employee e where e.name =?1")
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = "addresses")
    List<Employee> findByNameContaining(String name);

    @Query(value = "SELECT u.* FROM users u JOIN addresses a ON u.id = a.employee_id " +
            "WHERE u.gender = :gender AND a.country = :country", nativeQuery = true)
    List<Employee> findByGender(String gender, String country);

    @Query(value = "SELECT * FROM users WHERE SUBSTRING(country, 1, 1) = LOWER(SUBSTRING(country, 1, 1))",
            nativeQuery = true)
    List<Employee> findAllByCountryStartsWithLowerCase();

    @Query(value = "SELECT * FROM users WHERE country NOT IN :countries", nativeQuery = true)
    List<Employee> findAllByCountryNotIn(@Param("countries") List<String> countries);

    @Query("update Employee set name = ?1 where id = ?2")
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    void updateEmployeeByName(String name, Integer id);


    Employee findByName(String name);


    @Query("SELECT e.id, e.name FROM Employee e WHERE e.id = ?1")
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    void findEmployeeById(Integer id);

    Employee findEmployeeByEmailNotNull();

    @NotNull
    Page<Employee> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"addresses", "document"})
    Page<Employee> findByName(String name, Pageable pageable);

    Page<Employee> findByCountryContaining(String country, Pageable pageable);

    @Query(value = "SELECT * FROM users WHERE country = 'Ukraine'", nativeQuery = true)
    Optional<List<Employee>> findAllUkrainian();

    @EntityGraph(attributePaths = {"addresses", "document"})
    @Query("SELECT e FROM Employee e where e.id < :value")
    List<Employee> findFirstEmployee(@Param("value") Integer value);

    @Query("DELETE  FROM Employee e WHERE e.id = ?1")
    @Modifying(clearAutomatically = true, flushAutomatically = true)
   @Transactional
    void deleteById(@Param("id") Integer id);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            INSERT INTO users(name, email, country, gender) VALUES (name, email, country, gender);
            """, nativeQuery = true)
    Integer saveEmployee(String name, String email, String country, String gender);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            DELETE FROM users WHERE id = ?1
            """, nativeQuery = true)
    void deleteEmployeeById(Integer id);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            DELETE FROM addresses WHERE employee_id = :employeeId
            """, nativeQuery = true)
    void deleteAddressById(@Param("employeeId") Integer id);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
UPDATE users SET name = ?1, email = ?2, country = ?3 WHERE id = ?4
            """, nativeQuery = true)
    Integer updateEmployee(String name, String email, String country, Integer id);
}
