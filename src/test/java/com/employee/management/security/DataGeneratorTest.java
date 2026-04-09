package com.employee.management.security;

import com.employee.management.entity.Employee;
import com.employee.management.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 大数据量测试数据生成器
 * 用于生成 10 万级别的测试数据
 */
@SpringBootTest
@ActiveProfiles("test")
public class DataGeneratorTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private static final String[] DEPARTMENTS = {"技术部", "市场部", "销售部", "人力资源部", "财务部", "运营部"};
    private static final String[] POSITIONS = {"工程师", "经理", "主管", "专员", "总监"};
    private static final String[] GENDERS = {"MALE", "FEMALE"};

    /**
     * 生成 10 万条员工数据
     * 注意：此测试会生成大量数据，请谨慎执行
     */
    @Test
    @Transactional
    public void generateLargeData() {
        int batchSize = 1000;
        int totalRecords = 100000;
        
        System.out.println("开始生成 " + totalRecords + " 条员工数据...");
        
        List<Employee> batch = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        
        for (int i = 1; i <= totalRecords; i++) {
            Employee employee = createRandomEmployee(i);
            batch.add(employee);
            
            // 批量插入
            if (batch.size() >= batchSize) {
                employeeRepository.saveAll(batch);
                batch.clear();
                
                if (i % 10000 == 0) {
                    long elapsed = System.currentTimeMillis() - startTime;
                    System.out.println("已插入 " + i + " 条数据，耗时：" + (elapsed / 1000) + "秒");
                }
            }
        }
        
        // 插入剩余数据
        if (!batch.isEmpty()) {
            employeeRepository.saveAll(batch);
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("数据生成完成！");
        System.out.println("总耗时：" + ((endTime - startTime) / 1000) + "秒");
        System.out.println("平均速度：" + (totalRecords * 1000 / (endTime - startTime)) + "条/秒");
    }
    
    /**
     * 生成 1000 条测试数据（快速测试用）
     */
    @Test
    @Transactional
    public void generateSmallData() {
        int totalRecords = 1000;
        
        System.out.println("开始生成 " + totalRecords + " 条员工数据...");
        
        List<Employee> employees = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        
        for (int i = 1; i <= totalRecords; i++) {
            employees.add(createRandomEmployee(i));
        }
        
        employeeRepository.saveAll(employees);
        
        long endTime = System.currentTimeMillis();
        System.out.println("数据生成完成！");
        System.out.println("总耗时：" + ((endTime - startTime) / 1000) + "秒");
    }
    
    private Employee createRandomEmployee(int index) {
        Random random = new Random();
        
        Employee employee = new Employee();
        employee.setName("测试员工_" + index);
        employee.setGender(GENDERS[random.nextInt(GENDERS.length)]);
        employee.setAge(25 + random.nextInt(15));
        employee.setEmail("test" + index + "@example.com");
        employee.setPhone("138" + String.format("%08d", index));
        employee.setDepartmentId((long) (random.nextInt(6) + 1));
        employee.setDepartmentName(DEPARTMENTS[random.nextInt(DEPARTMENTS.length)]);
        employee.setPosition(POSITIONS[random.nextInt(POSITIONS.length)]);
        employee.setEmployeeCode("EMP" + String.format("%08d", index));
        employee.setStatus("ACTIVE");
        employee.setEntryDate(LocalDate.now().minusDays(random.nextInt(365)));
        employee.setBirthDate(LocalDate.now().minusYears(25 + random.nextInt(15)));
        
        return employee;
    }
}
