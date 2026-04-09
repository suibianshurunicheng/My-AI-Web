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
    
    // ============================================
    // 游标分页查询方法（优化深度分页性能）
    // ============================================
    
    /**
     * 游标分页查询 - 基础查询（根据 ID）
     * 性能优于 OFFSET 分页，特别是在大数据量场景
     */
    @Query("SELECT e FROM Employee e WHERE e.id > :cursor ORDER BY e.id ASC")
    List<Employee> findByIdGreaterThan(@Param("cursor") Long cursor, org.springframework.data.domain.Pageable pageable);
    
    /**
     * 游标分页查询 - 降序排列
     */
    @Query("SELECT e FROM Employee e WHERE e.id < :cursor ORDER BY e.id DESC")
    List<Employee> findByIdLessThan(@Param("cursor") Long cursor, org.springframework.data.domain.Pageable pageable);
    
    /**
     * 游标分页查询 - 带部门筛选
     */
    @Query("SELECT e FROM Employee e WHERE e.departmentId = :departmentId AND e.id > :cursor ORDER BY e.id ASC")
    List<Employee> findByDepartmentIdAndIdGreaterThan(@Param("departmentId") Long departmentId, 
                                                       @Param("cursor") Long cursor,
                                                       org.springframework.data.domain.Pageable pageable);
    
    /**
     * 延迟关联查询 - 优化深度分页
     * 先查询 ID 列表，再关联查询详细信息
     */
    @Query("SELECT e.id FROM Employee e ORDER BY e.id ASC")
    List<Long> findIds(org.springframework.data.domain.Pageable pageable);
    
    /**
     * 根据 ID 列表查询详细信息
     */
    @Query("SELECT e FROM Employee e WHERE e.id IN :ids")
    List<Employee> findByIds(@Param("ids") List<Long> ids);
}