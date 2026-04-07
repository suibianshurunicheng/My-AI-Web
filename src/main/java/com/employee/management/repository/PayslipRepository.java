package com.employee.management.repository;

import com.employee.management.entity.Payslip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayslipRepository extends JpaRepository<Payslip, Long> {
    Page<Payslip> findByYearAndMonthNum(Integer year, Integer monthNum, Pageable pageable);
    Page<Payslip> findByEmployeeId(Long employeeId, Pageable pageable);
    Page<Payslip> findByEmployeeIdAndYearAndMonthNum(Long employeeId, Integer year, Integer monthNum, Pageable pageable);
    Optional<Payslip> findByEmployeeIdAndYearAndMonthNum(Long employeeId, Integer year, Integer monthNum);
    List<Payslip> findByYearAndMonthNum(Integer year, Integer monthNum);
    List<Payslip> findByEmployeeId(Long employeeId);
}

