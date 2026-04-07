package com.employee.management.service;

import com.employee.management.entity.Employee;
import com.employee.management.entity.Payslip;
import com.employee.management.entity.SalaryStructure;
import com.employee.management.repository.EmployeeRepository;
import com.employee.management.repository.PayslipRepository;
import com.employee.management.repository.SalaryStructureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class SalaryService {

    private static final Logger logger = LoggerFactory.getLogger(SalaryService.class);

    private static final BigDecimal SOCIAL_INSURANCE_RATE = new BigDecimal("0.103");
    private static final BigDecimal HOUSING_FUND_RATE = new BigDecimal("0.08");
    private static final BigDecimal TAX_THRESHOLD = new BigDecimal("5000");
    private static final BigDecimal MINIMUM_WAGE = new BigDecimal("2360");

    @Autowired
    private SalaryStructureRepository salaryStructureRepository;

    @Autowired
    private PayslipRepository payslipRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<SalaryStructure> getAllStructures() {
        return salaryStructureRepository.findAll();
    }

    public SalaryStructure saveStructure(SalaryStructure structure) {
        return salaryStructureRepository.save(structure);
    }

    public void deleteStructure(Long id) {
        salaryStructureRepository.deleteById(id);
    }

    public Page<Payslip> getPayslips(Long employeeId, Integer year, Integer monthNum, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "year", "monthNum"));
        if (employeeId != null && year != null && monthNum != null) {
            return payslipRepository.findByEmployeeIdAndYearAndMonthNum(employeeId, year, monthNum, pageable);
        } else if (year != null && monthNum != null) {
            return payslipRepository.findByYearAndMonthNum(year, monthNum, pageable);
        } else if (employeeId != null) {
            return payslipRepository.findByEmployeeId(employeeId, pageable);
        }
        return payslipRepository.findAll(pageable);
    }

    public Optional<Payslip> getPayslipById(Long id) {
        return payslipRepository.findById(id);
    }

    public List<Payslip> getPayslipsForExport(Integer year, Integer monthNum) {
        if (year != null && monthNum != null) {
            return payslipRepository.findByYearAndMonthNum(year, monthNum);
        }
        return payslipRepository.findAll();
    }

    @Transactional
    public List<Payslip> generatePayslips(Integer year, Integer monthNum, List<Long> employeeIds) {
        logger.info("生成工资条 - 年: {}, 月: {}, 员工数: {}", year, monthNum, employeeIds != null ? employeeIds.size() : "全部");

        List<Employee> employees;
        if (employeeIds != null && !employeeIds.isEmpty()) {
            employees = employeeRepository.findAllById(employeeIds);
        } else {
            employees = employeeRepository.findAll();
        }

        for (Employee employee : employees) {
            generatePayslipForEmployee(employee, year, monthNum);
        }

        if (year != null && monthNum != null) {
            return payslipRepository.findByYearAndMonthNum(year, monthNum);
        }
        return payslipRepository.findAll();
    }

    private void generatePayslipForEmployee(Employee employee, Integer year, Integer monthNum) {
        Optional<Payslip> existing = payslipRepository.findByEmployeeIdAndYearAndMonthNum(employee.getId(), year, monthNum);
        if (existing.isPresent()) {
            logger.info("员工 {} 的工资条已存在，跳过", employee.getName());
            return;
        }

        Payslip payslip = new Payslip();
        payslip.setEmployeeId(employee.getId());
        payslip.setEmployeeName(employee.getName());
        payslip.setEmployeeCode(employee.getEmployeeCode());
        payslip.setYear(year);
        payslip.setMonthNum(monthNum);

        BigDecimal basicSalary = new BigDecimal("8000");
        BigDecimal positionSalary = new BigDecimal("3000");
        BigDecimal performanceBonus = new BigDecimal("2000");
        BigDecimal allowances = new BigDecimal("1000");
        BigDecimal deductions = BigDecimal.ZERO;

        BigDecimal socialBase = basicSalary.max(MINIMUM_WAGE);
        BigDecimal socialInsurance = socialBase.multiply(SOCIAL_INSURANCE_RATE).setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal housingFundBase = basicSalary.max(MINIMUM_WAGE);
        BigDecimal housingFund = housingFundBase.multiply(HOUSING_FUND_RATE).setScale(2, RoundingMode.HALF_UP);

        BigDecimal grossSalary = basicSalary.add(positionSalary).add(performanceBonus).add(allowances).subtract(deductions);
        BigDecimal taxBase = grossSalary.subtract(socialInsurance).subtract(housingFund).subtract(TAX_THRESHOLD);
        BigDecimal individualTax = calculateIndividualTax(taxBase);
        BigDecimal netSalary = grossSalary.subtract(socialInsurance).subtract(housingFund).subtract(individualTax);

        payslip.setBasicSalary(basicSalary);
        payslip.setPositionSalary(positionSalary);
        payslip.setPerformanceBonus(performanceBonus);
        payslip.setAllowances(allowances);
        payslip.setDeductions(deductions);
        payslip.setSocialInsurance(socialInsurance);
        payslip.setHousingFund(housingFund);
        payslip.setIndividualTax(individualTax);
        payslip.setGrossSalary(grossSalary);
        payslip.setNetSalary(netSalary);
        payslip.setStatus("GENERATED");

        payslipRepository.save(payslip);
        logger.info("员工 {} 的工资条已生成", employee.getName());
    }

    private BigDecimal calculateIndividualTax(BigDecimal taxBase) {
        if (taxBase.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal tax;
        if (taxBase.compareTo(new BigDecimal("3000")) <= 0) {
            tax = taxBase.multiply(new BigDecimal("0.03"));
        } else if (taxBase.compareTo(new BigDecimal("12000")) <= 0) {       
            tax = taxBase.multiply(new BigDecimal("0.10")).subtract(new BigDecimal("210"));
        } else if (taxBase.compareTo(new BigDecimal("25000")) <= 0) {   
            tax = taxBase.multiply(new BigDecimal("0.20")).subtract(new BigDecimal("1410"));
        } else if (taxBase.compareTo(new BigDecimal("35000")) <= 0) {
            tax = taxBase.multiply(new BigDecimal("0.25")).subtract(new BigDecimal("2660"));
        } else if (taxBase.compareTo(new BigDecimal("55000")) <= 0) {
            tax = taxBase.multiply(new BigDecimal("0.30")).subtract(new BigDecimal("4410"));
        } else if (taxBase.compareTo(new BigDecimal("80000")) <= 0) {
            tax = taxBase.multiply(new BigDecimal("0.35")).subtract(new BigDecimal("7160"));
        } else {
            tax = taxBase.multiply(new BigDecimal("0.45")).subtract(new BigDecimal("15160"));
        }

        return tax.setScale(2, RoundingMode.HALF_UP);
    }
}

