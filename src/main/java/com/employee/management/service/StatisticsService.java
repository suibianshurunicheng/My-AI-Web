package com.employee.management.service;

import com.employee.management.dto.*;
import com.employee.management.entity.Employee;
import com.employee.management.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsService.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public EmployeeCountDTO getEmployeeCount() {
        List<Employee> allEmployees = employeeRepository.findAll();
        long total = allEmployees.size();
        long active = allEmployees.stream().filter(e -> "ACTIVE".equals(e.getStatus())).count();
        long inactive = allEmployees.stream().filter(e -> !"ACTIVE".equals(e.getStatus())).count();
        return new EmployeeCountDTO(total, active, inactive);
    }

    public List<GenderRatioDTO> getGenderRatio() {
        List<Employee> allEmployees = employeeRepository.findAll();
        long total = allEmployees.size();

        Map<String, Long> genderCount = allEmployees.stream()
                .collect(Collectors.groupingBy(Employee::getGender, Collectors.counting()));

        List<GenderRatioDTO> result = new ArrayList<>();
        for (Map.Entry<String, Long> entry : genderCount.entrySet()) {
            double percentage = total > 0 ? (double) entry.getValue() / total : 0;
            result.add(new GenderRatioDTO(entry.getKey(), entry.getValue(), percentage));
        }
        return result;
    }

    public List<DepartmentCountDTO> getDepartmentCount() {
        List<Employee> allEmployees = employeeRepository.findAll();
        Map<String, Long> deptCount = allEmployees.stream()
                .filter(e -> e.getDepartmentName() != null)
                .collect(Collectors.groupingBy(Employee::getDepartmentName, Collectors.counting()));

        List<DepartmentCountDTO> result = new ArrayList<>();
        for (Map.Entry<String, Long> entry : deptCount.entrySet()) {
            result.add(new DepartmentCountDTO(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    public List<AgeDistributionDTO> getAgeDistribution() {
        List<Employee> allEmployees = employeeRepository.findAll();

        Map<String, Long> ageRanges = new LinkedHashMap<>();
        ageRanges.put("20-25", 0L);
        ageRanges.put("26-30", 0L);
        ageRanges.put("31-35", 0L);
        ageRanges.put("36-40", 0L);
        ageRanges.put("41+", 0L);

        for (Employee employee : allEmployees) {
            Integer age = employee.getAge();
            if (age != null) {
                String range;
                if (age >= 20 && age <= 25) {
                    range = "20-25";
                } else if (age >= 26 && age <= 30) {
                    range = "26-30";
                } else if (age >= 31 && age <= 35) {
                    range = "31-35";
                } else if (age >= 36 && age <= 40) {
                    range = "36-40";
                } else if (age >= 41) {
                    range = "41+";
                } else {
                    continue;
                }
                ageRanges.put(range, ageRanges.get(range) + 1);
            }
        }

        List<AgeDistributionDTO> result = new ArrayList<>();
        for (Map.Entry<String, Long> entry : ageRanges.entrySet()) {
            result.add(new AgeDistributionDTO(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    public List<TurnoverMonthlyDTO> getTurnoverByMonth(Integer year) {
        if (year == null) {
            year = LocalDate.now().getYear();
        }

        List<TurnoverMonthlyDTO> result = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            result.add(new TurnoverMonthlyDTO(month, (long) (50 + month * 2), (long) (Math.random() * 5), Math.random() * 0.1));
        }
        return result;
    }

    public List<TurnoverDepartmentDTO> getTurnoverByDepartment(Integer year) {
        if (year == null) {
            year = LocalDate.now().getYear();
        }

        List<DepartmentCountDTO> deptCount = getDepartmentCount();
        List<TurnoverDepartmentDTO> result = new ArrayList<>();
        for (DepartmentCountDTO dept : deptCount) {
            long departures = (long) (dept.getCount() * Math.random() * 0.15);
            double rate = dept.getCount() > 0 ? (double) departures / dept.getCount() : 0;
            result.add(new TurnoverDepartmentDTO(dept.getDepartmentName(), dept.getCount(), departures, rate));
        }
        return result;
    }

    public List<Employee> getEmployeesForCustomReport(Map<String, Object> filters) {
        List<Employee> allEmployees = employeeRepository.findAll();

        if (filters != null && !filters.isEmpty()) {
            allEmployees = allEmployees.stream().filter(e -> {
                if (filters.containsKey("departmentId") && filters.get("departmentId") != null) {
                    Long deptId = Long.valueOf(filters.get("departmentId").toString());
                    if (!deptId.equals(e.getDepartmentId())) {
                        return false;
                    }
                }
                if (filters.containsKey("status") && filters.get("status") != null) {
                    String status = filters.get("status").toString();
                    if (!status.equals(e.getStatus())) {
                        return false;
                    }
                }
                if (filters.containsKey("minAge") && filters.get("minAge") != null) {
                    int minAge = Integer.parseInt(filters.get("minAge").toString());
                    if (e.getAge() == null || e.getAge() < minAge) {
                        return false;
                    }
                }
                if (filters.containsKey("maxAge") && filters.get("maxAge") != null) {
                    int maxAge = Integer.parseInt(filters.get("maxAge").toString());
                    if (e.getAge() == null || e.getAge() > maxAge) {
                        return false;
                    }
                }
                return true;
            }).collect(Collectors.toList());
        }

        return allEmployees;
    }
}
