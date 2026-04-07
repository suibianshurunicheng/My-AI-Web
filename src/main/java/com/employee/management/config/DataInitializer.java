package com.employee.management.config;

import com.employee.management.entity.*;
import com.employee.management.repository.*;
import com.employee.management.util.EmployeeCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Lazy
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private PositionRepository positionRepository;
    
    @Autowired
    private com.employee.management.repository.NoticeRepository noticeRepository;
    
    @Autowired
    private EmployeeCodeGenerator employeeCodeGenerator;
    
    @Autowired
    private SalaryStructureRepository salaryStructureRepository;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private ResumeRepository resumeRepository;
    
    @Autowired
    private AttendanceRecordRepository attendanceRecordRepository;
    
    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;
    
    @Autowired
    private PerformanceCycleRepository performanceCycleRepository;
    
    @Autowired
    private PerformanceResultRepository performanceResultRepository;
    
    @Autowired
    private InterviewRepository interviewRepository;
    
    @Autowired
    private OfferRepository offerRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("开始初始化基础数据...");
        
        try {
            if (departmentRepository.count() == 0) {
                Department dept1 = new Department();
                dept1.setName("技术部");
                dept1.setDescription("负责公司技术开发和维护");
                departmentRepository.save(dept1);
                
                Department dept2 = new Department();
                dept2.setName("人力资源部");
                dept2.setDescription("负责公司人力资源管理");
                departmentRepository.save(dept2);
                
                Department dept3 = new Department();
                dept3.setName("市场部");
                dept3.setDescription("负责公司市场推广和销售");
                departmentRepository.save(dept3);
                
                System.out.println("部门数据初始化完成");
            }
            
            if (positionRepository.count() == 0) {
                Position pos1 = new Position();
                pos1.setName("前端开发");
                pos1.setDescription("负责前端开发工作");
                positionRepository.save(pos1);
                
                Position pos2 = new Position();
                pos2.setName("后端开发");
                pos2.setDescription("负责后端开发工作");
                positionRepository.save(pos2);
                
                Position pos3 = new Position();
                pos3.setName("产品经理");
                pos3.setDescription("负责产品规划和管理");
                positionRepository.save(pos3);
                
                Position pos4 = new Position();
                pos4.setName("UI设计师");
                pos4.setDescription("负责界面设计");
                positionRepository.save(pos4);
                
                System.out.println("岗位数据初始化完成");
            }
            
            if (employeeRepository.count() == 0) {
                // 员工 1
                Employee emp1 = new Employee();
                emp1.setName("张明");
                emp1.setGender("男");
                emp1.setAge(28);
                emp1.setBirthDate(LocalDate.of(1995, 3, 12));
                emp1.setHeight(175.5);
                emp1.setWeight(72.3);
                emp1.setPosition("后端开发工程师");
                emp1.setEmail("zhangming@example.com");
                emp1.setPhone("13812345678");
                emp1.setEmployeeCode(employeeCodeGenerator.generateUniqueEmployeeCode());
                emp1.setEntryDate(LocalDate.of(2020, 1, 15));
                emp1.setStatus("在职");
                emp1.setDepartmentId(1L);
                emp1.setDepartmentName("技术部");
                employeeRepository.save(emp1);
                
                // 员工 2
                Employee emp2 = new Employee();
                emp2.setName("李华");
                emp2.setGender("女");
                emp2.setAge(26);
                emp2.setBirthDate(LocalDate.of(1997, 5, 20));
                emp2.setHeight(165.0);
                emp2.setWeight(52.0);
                emp2.setPosition("前端开发工程师");
                emp2.setEmail("lihua@example.com");
                emp2.setPhone("13987654321");
                emp2.setEmployeeCode(employeeCodeGenerator.generateUniqueEmployeeCode());
                emp2.setEntryDate(LocalDate.of(2021, 3, 1));
                emp2.setStatus("在职");
                emp2.setDepartmentId(1L);
                emp2.setDepartmentName("技术部");
                employeeRepository.save(emp2);
                
                // 员工 3
                Employee emp3 = new Employee();
                emp3.setName("王芳");
                emp3.setGender("女");
                emp3.setAge(30);
                emp3.setBirthDate(LocalDate.of(1993, 8, 8));
                emp3.setHeight(168.0);
                emp3.setWeight(55.0);
                emp3.setPosition("产品经理");
                emp3.setEmail("wangfang@example.com");
                emp3.setPhone("13654789210");
                emp3.setEmployeeCode(employeeCodeGenerator.generateUniqueEmployeeCode());
                emp3.setEntryDate(LocalDate.of(2019, 6, 20));
                emp3.setStatus("在职");
                emp3.setDepartmentId(1L);
                emp3.setDepartmentName("技术部");
                employeeRepository.save(emp3);
                
                // 员工 4
                Employee emp4 = new Employee();
                emp4.setName("刘洋");
                emp4.setGender("男");
                emp4.setAge(32);
                emp4.setBirthDate(LocalDate.of(1991, 2, 15));
                emp4.setHeight(180.0);
                emp4.setWeight(78.0);
                emp4.setPosition("UI设计师");
                emp4.setEmail("liuyang@example.com");
                emp4.setPhone("13512345678");
                emp4.setEmployeeCode(employeeCodeGenerator.generateUniqueEmployeeCode());
                emp4.setEntryDate(LocalDate.of(2018, 8, 1));
                emp4.setStatus("在职");
                emp4.setDepartmentId(1L);
                emp4.setDepartmentName("技术部");
                employeeRepository.save(emp4);
                
                // 员工 5
                Employee emp5 = new Employee();
                emp5.setName("陈静");
                emp5.setGender("女");
                emp5.setAge(27);
                emp5.setBirthDate(LocalDate.of(1996, 10, 5));
                emp5.setHeight(163.0);
                emp5.setWeight(50.0);
                emp5.setPosition("后端开发工程师");
                emp5.setEmail("chenjing@example.com");
                emp5.setPhone("13787654321");
                emp5.setEmployeeCode(employeeCodeGenerator.generateUniqueEmployeeCode());
                emp5.setEntryDate(LocalDate.of(2021, 6, 15));
                emp5.setStatus("在职");
                emp5.setDepartmentId(1L);
                emp5.setDepartmentName("技术部");
                employeeRepository.save(emp5);
                
                // 员工 6
                Employee emp6 = new Employee();
                emp6.setName("赵伟");
                emp6.setGender("男");
                emp6.setAge(35);
                emp6.setBirthDate(LocalDate.of(1988, 7, 22));
                emp6.setHeight(178.0);
                emp6.setWeight(80.0);
                emp6.setPosition("前端开发工程师");
                emp6.setEmail("zhaowei@example.com");
                emp6.setPhone("13956781234");
                emp6.setEmployeeCode(employeeCodeGenerator.generateUniqueEmployeeCode());
                emp6.setEntryDate(LocalDate.of(2017, 3, 1));
                emp6.setStatus("在职");
                emp6.setDepartmentId(1L);
                emp6.setDepartmentName("技术部");
                employeeRepository.save(emp6);
                
                // 员工 7
                Employee emp7 = new Employee();
                emp7.setName("孙丽");
                emp7.setGender("女");
                emp7.setAge(25);
                emp7.setBirthDate(LocalDate.of(1998, 1, 18));
                emp7.setHeight(162.0);
                emp7.setWeight(48.0);
                emp7.setPosition("产品经理");
                emp7.setEmail("sunli@example.com");
                emp7.setPhone("13689012345");
                emp7.setEmployeeCode(employeeCodeGenerator.generateUniqueEmployeeCode());
                emp7.setEntryDate(LocalDate.of(2022, 1, 10));
                emp7.setStatus("在职");
                emp7.setDepartmentId(1L);
                emp7.setDepartmentName("技术部");
                employeeRepository.save(emp7);
                
                // 员工 8
                Employee emp8 = new Employee();
                emp8.setName("周杰");
                emp8.setGender("男");
                emp8.setAge(29);
                emp8.setBirthDate(LocalDate.of(1994, 12, 3));
                emp8.setHeight(176.0);
                emp8.setWeight(74.0);
                emp8.setPosition("UI设计师");
                emp8.setEmail("zhoujie@example.com");
                emp8.setPhone("13578901234");
                emp8.setEmployeeCode(employeeCodeGenerator.generateUniqueEmployeeCode());
                emp8.setEntryDate(LocalDate.of(2020, 5, 15));
                emp8.setStatus("在职");
                emp8.setDepartmentId(1L);
                emp8.setDepartmentName("技术部");
                employeeRepository.save(emp8);
                
                // 员工 9
                Employee emp9 = new Employee();
                emp9.setName("吴敏");
                emp9.setGender("女");
                emp9.setAge(31);
                emp9.setBirthDate(LocalDate.of(1992, 4, 25));
                emp9.setHeight(166.0);
                emp9.setWeight(53.0);
                emp9.setPosition("后端开发工程师");
                emp9.setEmail("wumin@example.com");
                emp9.setPhone("13890123456");
                emp9.setEmployeeCode(employeeCodeGenerator.generateUniqueEmployeeCode());
                emp9.setEntryDate(LocalDate.of(2018, 10, 1));
                emp9.setStatus("在职");
                emp9.setDepartmentId(1L);
                emp9.setDepartmentName("技术部");
                employeeRepository.save(emp9);
                
                // 员工 10
                Employee emp10 = new Employee();
                emp10.setName("郑强");
                emp10.setGender("男");
                emp10.setAge(33);
                emp10.setBirthDate(LocalDate.of(1990, 9, 10));
                emp10.setHeight(182.0);
                emp10.setWeight(82.0);
                emp10.setPosition("前端开发工程师");
                emp10.setEmail("zhengqiang@example.com");
                emp10.setPhone("13701234567");
                emp10.setEmployeeCode(employeeCodeGenerator.generateUniqueEmployeeCode());
                emp10.setEntryDate(LocalDate.of(2016, 7, 1));
                emp10.setStatus("在职");
                emp10.setDepartmentId(1L);
                emp10.setDepartmentName("技术部");
                employeeRepository.save(emp10);
                
                // 员工 11
                Employee emp11 = new Employee();
                emp11.setName("谢婷");
                emp11.setGender("女");
                emp11.setAge(24);
                emp11.setBirthDate(LocalDate.of(1999, 6, 30));
                emp11.setHeight(160.0);
                emp11.setWeight(46.0);
                emp11.setPosition("产品经理");
                emp11.setEmail("xieting@example.com");
                emp11.setPhone("13612345678");
                emp11.setEmployeeCode(employeeCodeGenerator.generateUniqueEmployeeCode());
                emp11.setEntryDate(LocalDate.of(2023, 1, 1));
                emp11.setStatus("在职");
                emp11.setDepartmentId(1L);
                emp11.setDepartmentName("技术部");
                employeeRepository.save(emp11);
                
                System.out.println("员工数据初始化完成（11人）");
            }
            
            if (noticeRepository.count() == 0) {
                com.employee.management.entity.Notice notice1 = new com.employee.management.entity.Notice();
                notice1.setTitle("关于2026年五一放假安排的通知");
                notice1.setContent("各部门注意，2026年五一放假时间为4月29日至5月3日，共5天。请各部门提前安排好工作。\n\n祝大家节日快乐！");
                notice1.setScope("ALL");
                notice1.setImportant(true);
                notice1.setSendEmail(true);
                notice1.setSendInternalMessage(true);
                notice1.setPublisherId(1L);
                notice1.setPublisherName("管理员");
                notice1.setPublishTime(LocalDateTime.now());
                notice1.setViewCount(0);
                noticeRepository.save(notice1);
                
                com.employee.management.entity.Notice notice2 = new com.employee.management.entity.Notice();
                notice2.setTitle("技术部季度会议通知");
                notice2.setContent("技术部全体成员请注意，本季度会议将于4月15日下午2点在3号会议室召开，请准时参加。");
                notice2.setScope("DEPARTMENT");
                notice2.setDepartmentId(1L);
                notice2.setDepartmentName("技术部");
                notice2.setImportant(false);
                notice2.setSendEmail(false);
                notice2.setSendInternalMessage(true);
                notice2.setPublisherId(1L);
                notice2.setPublisherName("管理员");
                notice2.setPublishTime(LocalDateTime.now());
                notice2.setViewCount(0);
                noticeRepository.save(notice2);
                
                System.out.println("通知公告数据初始化完成");
            }
            
            if (salaryStructureRepository.count() == 0) {
                SalaryStructure ss1 = new SalaryStructure();
                ss1.setName("基本工资");
                ss1.setType("FIXED");
                ss1.setAmount(new BigDecimal("8000.00"));
                ss1.setFormula("FIXED");
                ss1.setIsActive(true);
                salaryStructureRepository.save(ss1);
                
                SalaryStructure ss2 = new SalaryStructure();
                ss2.setName("绩效奖金");
                ss2.setType("PERFORMANCE");
                ss2.setAmount(new BigDecimal("2000.00"));
                ss2.setFormula("PERFORMANCE");
                ss2.setIsActive(true);
                salaryStructureRepository.save(ss2);
                
                SalaryStructure ss3 = new SalaryStructure();
                ss3.setName("交通补贴");
                ss3.setType("ALLOWANCE");
                ss3.setAmount(new BigDecimal("500.00"));
                ss3.setFormula("FIXED");
                ss3.setIsActive(true);
                salaryStructureRepository.save(ss3);
                
                System.out.println("薪资结构数据初始化完成");
            }
            
            if (jobRepository.count() == 0) {
                Job job1 = new Job();
                job1.setTitle("高级后端开发工程师");
                job1.setDepartmentId(1L);
                job1.setDepartmentName("技术部");
                job1.setPositionId(2L);
                job1.setPositionName("后端开发");
                job1.setRecruitCount(2);
                job1.setRequirement("3年以上Java开发经验，熟悉Spring Boot，有高并发系统经验优先");
                job1.setSalaryRange("20k-35k");
                job1.setStatus("OPEN");
                jobRepository.save(job1);
                
                Job job2 = new Job();
                job2.setTitle("UI设计师");
                job2.setDepartmentId(1L);
                job2.setDepartmentName("技术部");
                job2.setPositionId(4L);
                job2.setPositionName("UI设计师");
                job2.setRecruitCount(1);
                job2.setRequirement("2年以上UI设计经验，熟练使用Figma、Sketch等设计工具");
                job2.setSalaryRange("15k-25k");
                job2.setStatus("OPEN");
                jobRepository.save(job2);
                
                System.out.println("招聘职位数据初始化完成");
            }
            
            if (resumeRepository.count() == 0) {
                Resume resume1 = new Resume();
                resume1.setName("赵强");
                resume1.setPhone("13712345678");
                resume1.setEmail("zhaoqiang@example.com");
                resume1.setJobId(1L);
                resume1.setJobTitle("高级后端开发工程师");
                resume1.setStatus("PENDING");
                resumeRepository.save(resume1);
                
                Resume resume2 = new Resume();
                resume2.setName("孙丽");
                resume2.setPhone("13698765432");
                resume2.setEmail("sunli@example.com");
                resume2.setJobId(2L);
                resume2.setJobTitle("UI设计师");
                resume2.setStatus("PENDING");
                resumeRepository.save(resume2);
                
                System.out.println("简历数据初始化完成");
            }
            
            // 考勤数据
            if (attendanceRecordRepository.count() == 0) {
                for (int empId = 1; empId <= 11; empId++) {
                    String empName = "";
                    switch (empId) {
                        case 1: empName = "张明"; break;
                        case 2: empName = "李华"; break;
                        case 3: empName = "王芳"; break;
                        case 4: empName = "刘洋"; break;
                        case 5: empName = "陈静"; break;
                        case 6: empName = "赵伟"; break;
                        case 7: empName = "孙丽"; break;
                        case 8: empName = "周杰"; break;
                        case 9: empName = "吴敏"; break;
                        case 10: empName = "郑强"; break;
                        case 11: empName = "谢婷"; break;
                    }
                    
                    AttendanceRecord ar = new AttendanceRecord();
                    ar.setEmployeeId((long) empId);
                    ar.setEmployeeName(empName);
                    ar.setAttendanceDate(LocalDate.of(2026, 4, 1));
                    ar.setCheckInTime(LocalDate.of(2026, 4, 1).atTime(8, 50));
                    ar.setCheckOutTime(LocalDate.of(2026, 4, 1).atTime(18, 10));
                    ar.setStatus("NORMAL");
                    attendanceRecordRepository.save(ar);
                }
                System.out.println("考勤数据初始化完成");
            }
            
            // 请假数据
            if (leaveApplicationRepository.count() == 0) {
                LeaveApplication la1 = new LeaveApplication();
                la1.setEmployeeId(1L);
                la1.setEmployeeName("张明");
                la1.setLeaveType("ANNUAL");
                la1.setStartDate(LocalDate.of(2026, 4, 10));
                la1.setEndDate(LocalDate.of(2026, 4, 12));
                la1.setDays(new BigDecimal("3.0"));
                la1.setReason("年假回家探亲");
                la1.setStatus("APPROVED");
                la1.setApproverId(3L);
                la1.setApproverName("王芳");
                leaveApplicationRepository.save(la1);
                
                LeaveApplication la2 = new LeaveApplication();
                la2.setEmployeeId(2L);
                la2.setEmployeeName("李华");
                la2.setLeaveType("SICK");
                la2.setStartDate(LocalDate.of(2026, 4, 5));
                la2.setEndDate(LocalDate.of(2026, 4, 5));
                la2.setDays(new BigDecimal("1.0"));
                la2.setReason("感冒发烧");
                la2.setStatus("PENDING");
                leaveApplicationRepository.save(la2);
                
                System.out.println("请假数据初始化完成");
            }
            
            // 绩效数据
            if (performanceCycleRepository.count() == 0) {
                PerformanceCycle pc = new PerformanceCycle();
                pc.setName("2026年Q1绩效周期");
                pc.setCycleType("QUARTERLY");
                pc.setStartDate(LocalDate.of(2026, 1, 1).atStartOfDay());
                pc.setEndDate(LocalDate.of(2026, 3, 31).atStartOfDay());
                pc.setSelfEvalStart(LocalDate.of(2026, 4, 1).atStartOfDay());
                pc.setSelfEvalEnd(LocalDate.of(2026, 4, 7).atStartOfDay());
                pc.setSuperEvalStart(LocalDate.of(2026, 4, 8).atStartOfDay());
                pc.setSuperEvalEnd(LocalDate.of(2026, 4, 14).atStartOfDay());
                pc.setStatus("ACTIVE");
                performanceCycleRepository.save(pc);
                
                System.out.println("绩效周期数据初始化完成");
            }
            
            if (performanceResultRepository.count() == 0) {
                for (int empId = 1; empId <= 11; empId++) {
                    String empName = "";
                    String empCode = "";
                    switch (empId) {
                        case 1: empName = "张明"; empCode = "EMP001"; break;
                        case 2: empName = "李华"; empCode = "EMP002"; break;
                        case 3: empName = "王芳"; empCode = "EMP003"; break;
                        case 4: empName = "刘洋"; empCode = "EMP004"; break;
                        case 5: empName = "陈静"; empCode = "EMP005"; break;
                        case 6: empName = "赵伟"; empCode = "EMP006"; break;
                        case 7: empName = "孙丽"; empCode = "EMP007"; break;
                        case 8: empName = "周杰"; empCode = "EMP008"; break;
                        case 9: empName = "吴敏"; empCode = "EMP009"; break;
                        case 10: empName = "郑强"; empCode = "EMP010"; break;
                        case 11: empName = "谢婷"; empCode = "EMP011"; break;
                    }
                    
                    PerformanceResult pr = new PerformanceResult();
                    pr.setCycleId(1L);
                    pr.setCycleName("2026年Q1绩效周期");
                    pr.setEmployeeId((long) empId);
                    pr.setEmployeeName(empName);
                    pr.setEmployeeCode(empCode);
                    pr.setTotalScore(new BigDecimal(85 + empId));
                    pr.setGrade(empId <= 3 ? "A" : empId <= 7 ? "B" : "C");
                    pr.setStatus("COMPLETED");
                    performanceResultRepository.save(pr);
                }
                System.out.println("绩效结果数据初始化完成");
            }
            
            // 面试安排和录用流程数据
            if (interviewRepository.count() == 0) {
                Interview interview1 = new Interview();
                interview1.setResumeId(1L);
                interview1.setCandidateName("赵强");
                interview1.setJobTitle("高级后端开发工程师");
                interview1.setInterviewTime(LocalDate.of(2026, 4, 8).atTime(10, 0));
                interview1.setInterviewers("张明, 李华");
                interview1.setLocation("1号会议室");
                interview1.setStatus("SCHEDULED");
                interviewRepository.save(interview1);
                
                Interview interview2 = new Interview();
                interview2.setResumeId(2L);
                interview2.setCandidateName("孙丽");
                interview2.setJobTitle("UI设计师");
                interview2.setInterviewTime(LocalDate.of(2026, 4, 9).atTime(14, 0));
                interview2.setInterviewers("王芳, 刘洋");
                interview2.setLocation("2号会议室");
                interview2.setStatus("SCHEDULED");
                interviewRepository.save(interview2);
                
                System.out.println("面试安排数据初始化完成");
            }
            
            if (offerRepository.count() == 0) {
                Offer offer1 = new Offer();
                offer1.setResumeId(1L);
                offer1.setCandidateName("赵强");
                offer1.setJobId(1L);
                offer1.setJobTitle("高级后端开发工程师");
                offer1.setSalary("28k");
                offer1.setJoinDate(LocalDate.of(2026, 5, 1));
                offer1.setStatus("PENDING");
                offerRepository.save(offer1);
                
                System.out.println("录用流程数据初始化完成");
            }
            
            System.out.println("所有基础数据初始化完成！");
        } catch (Exception e) {
            System.out.println("数据初始化失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
