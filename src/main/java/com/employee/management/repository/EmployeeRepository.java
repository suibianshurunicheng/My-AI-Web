package com.employee.management.repository;

import com.employee.management.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    
    Optional<Employee> findByEmployeeCode(String employeeCode);
    
    Optional<Employee> findByEmail(String email);
    
    @Query("SELECT e FROM Employee e WHERE e.name LIKE %:name%")
    List<Employee> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT e FROM Employee e WHERE e.employeeCode LIKE %:code%")
    List<Employee> findByEmployeeCodeContaining(@Param("code") String code);
    
    @Query("SELECT e FROM Employee e WHERE e.name LIKE %:name% AND e.employeeCode LIKE %:code%")
    List<Employee> findByNameAndEmployeeCodeContaining(@Param("name") String name, 
                                                       @Param("code") String code);
    
    boolean existsByEmployeeCode(String employeeCode);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT COUNT(e) > 0 FROM Employee e WHERE e.email = :email AND e.id != :id")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("id") Long id);
}