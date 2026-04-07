package com.employee.management.service;

import com.employee.management.entity.*;
import com.employee.management.repository.*;
import com.employee.management.dto.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class RecruitmentService {
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private ResumeRepository resumeRepository;
    
    @Autowired
    private InterviewRepository interviewRepository;
    
    @Autowired
    private OfferRepository offerRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private PositionRepository positionRepository;
    
    @Autowired
    private EmployeeService employeeService;
    
    // 文件上传路径
    private static final String UPLOAD_DIR = "uploads/resumes";
    
    // 职位管理
    public PageResponse<Job> getJobs(int page, int size, Long departmentId, Long positionId, String status) {
        List<Job> jobs = jobRepository.findAll();
        
        // 分页处理
        int start = (page - 1) * size;
        int end = Math.min(start + size, jobs.size());
        List<Job> pageJobs = (start >= jobs.size()) ? new java.util.ArrayList<>() : jobs.subList(start, end);
        
        return new PageResponse<>(jobs.size(), page, size, pageJobs);
    }
    
    public Job createJob(Job job) {
        // 填充部门和职位名称
        Department department = departmentRepository.findById(job.getDepartmentId()).orElse(null);
        if (department != null) {
            job.setDepartmentName(department.getName());
        }
        
        Position position = positionRepository.findById(job.getPositionId()).orElse(null);
        if (position != null) {
            job.setPositionName(position.getName());
        }
        
        return jobRepository.save(job);
    }
    
    public Job updateJob(Long id, Job job) {
        Job existingJob = jobRepository.findById(id).orElse(null);
        if (existingJob == null) {
            return null;
        }
        
        existingJob.setTitle(job.getTitle());
        existingJob.setRequirement(job.getRequirement());
        existingJob.setSalaryRange(job.getSalaryRange());
        existingJob.setRecruitCount(job.getRecruitCount());
        existingJob.setStatus(job.getStatus());
        
        return jobRepository.save(existingJob);
    }
    
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }
    
    // 简历管理
    public PageResponse<Resume> getResumes(int page, int size, Long jobId, String status) {
        List<Resume> resumes;
        
        if (jobId != null && status != null && !status.isEmpty()) {
            resumes = resumeRepository.findByJobIdAndStatus(jobId, status);
        } else if (jobId != null) {
            resumes = resumeRepository.findByJobId(jobId);
        } else if (status != null && !status.isEmpty()) {
            resumes = resumeRepository.findByStatus(status);
        } else {
            resumes = resumeRepository.findAll();
        }
        
        // 分页处理
        int start = (page - 1) * size;
        int end = Math.min(start + size, resumes.size());
        List<Resume> pageResumes = (start >= resumes.size()) ? new java.util.ArrayList<>() : resumes.subList(start, end);
        
        return new PageResponse<>(resumes.size(), page, size, pageResumes);
    }
    
    public Resume uploadResume(String name, String phone, String email, Long jobId, MultipartFile resumeFile) throws IOException {
        // 确保上传目录存在
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        // 生成唯一文件名
        String originalFilename = resumeFile.getOriginalFilename();
        String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.')) : ".pdf";
        String filename = UUID.randomUUID().toString() + extension;
        Path filePath = Paths.get(UPLOAD_DIR, filename);
        
        // 保存文件
        Files.copy(resumeFile.getInputStream(), filePath);
        
        // 创建简历记录
        Resume resume = new Resume();
        resume.setName(name);
        resume.setPhone(phone);
        resume.setEmail(email);
        resume.setJobId(jobId);
        
        // 填充职位名称
        Job job = jobRepository.findById(jobId).orElse(null);
        if (job != null) {
            resume.setJobTitle(job.getTitle());
        }
        
        resume.setResumeFile("/api/files/resumes/" + filename);
        
        return resumeRepository.save(resume);
    }
    
    public Resume updateResumeStatus(Long id, String status) {
        Resume resume = resumeRepository.findById(id).orElse(null);
        if (resume == null) {
            return null;
        }
        
        resume.setStatus(status);
        return resumeRepository.save(resume);
    }
    
    // 面试管理
    public PageResponse<Interview> getInterviews(int page, int size, Long resumeId, String status) {
        List<Interview> interviews = interviewRepository.findAll();
        
        // 分页处理
        int start = (page - 1) * size;
        int end = Math.min(start + size, interviews.size());
        List<Interview> pageInterviews = (start >= interviews.size()) ? new java.util.ArrayList<>() : interviews.subList(start, end);
        
        return new PageResponse<>(interviews.size(), page, size, pageInterviews);
    }
    
    public Interview scheduleInterview(Long resumeId, String interviewers, String location, java.time.LocalDateTime interviewTime) {
        Resume resume = resumeRepository.findById(resumeId).orElse(null);
        if (resume == null) {
            return null;
        }
        
        Interview interview = new Interview();
        interview.setResumeId(resumeId);
        interview.setCandidateName(resume.getName());
        interview.setJobTitle(resume.getJobTitle());
        interview.setInterviewTime(interviewTime);
        interview.setInterviewers(interviewers);
        interview.setLocation(location);
        
        return interviewRepository.save(interview);
    }
    
    public Interview evaluateInterview(Long id, String evaluation, String result) {
        Interview interview = interviewRepository.findById(id).orElse(null);
        if (interview == null) {
            return null;
        }
        
        interview.setEvaluation(evaluation);
        interview.setResult(result);
        interview.setStatus("COMPLETED");
        
        return interviewRepository.save(interview);
    }
    
    public Interview updateInterview(Long id, Interview interview) {
        Interview existingInterview = interviewRepository.findById(id).orElse(null);
        if (existingInterview == null) {
            return null;
        }
        
        existingInterview.setInterviewTime(interview.getInterviewTime());
        existingInterview.setInterviewers(interview.getInterviewers());
        existingInterview.setLocation(interview.getLocation());
        
        return interviewRepository.save(existingInterview);
    }
    
    // 录用管理
    public PageResponse<Offer> getOffers(int page, int size, String status) {
        List<Offer> offers;
        
        if (status != null && !status.isEmpty()) {
            offers = offerRepository.findByStatus(status);
        } else {
            offers = offerRepository.findAll();
        }
        
        // 分页处理
        int start = (page - 1) * size;
        int end = Math.min(start + size, offers.size());
        List<Offer> pageOffers = (start >= offers.size()) ? new java.util.ArrayList<>() : offers.subList(start, end);
        
        return new PageResponse<>(offers.size(), page, size, pageOffers);
    }
    
    public Offer createOffer(Long resumeId, String salary, java.time.LocalDate joinDate) {
        Resume resume = resumeRepository.findById(resumeId).orElse(null);
        if (resume == null) {
            return null;
        }
        
        Offer offer = new Offer();
        offer.setResumeId(resumeId);
        offer.setCandidateName(resume.getName());
        offer.setJobId(resume.getJobId());
        offer.setJobTitle(resume.getJobTitle());
        offer.setSalary(salary);
        offer.setJoinDate(joinDate);
        
        return offerRepository.save(offer);
    }
    
    public Offer processOffer(Long id, String status) {
        Offer offer = offerRepository.findById(id).orElse(null);
        if (offer == null) {
            return null;
        }
        
        offer.setStatus(status);
        return offerRepository.save(offer);
    }
    
    public Offer updateOffer(Long id, Offer offer) {
        Offer existingOffer = offerRepository.findById(id).orElse(null);
        if (existingOffer == null) {
            return null;
        }
        
        existingOffer.setSalary(offer.getSalary());
        existingOffer.setJoinDate(offer.getJoinDate());
        
        return offerRepository.save(existingOffer);
    }
    
    public Employee createEmployeeFromOffer(Long offerId, String employeeCode, Long departmentId, Long positionId, java.time.LocalDate entryDate) {
        Offer offer = offerRepository.findById(offerId).orElse(null);
        if (offer == null || !"APPROVED".equals(offer.getStatus())) {
            System.out.println("Offer not found or not APPROVED, offerId: " + offerId + ", status: " + (offer != null ? offer.getStatus() : "null"));
            return null;
        }
        
        Resume resume = resumeRepository.findById(offer.getResumeId()).orElse(null);
        if (resume == null) {
            System.out.println("Resume not found for offerId: " + offerId);
            return null;
        }
        
        // 检查邮箱是否已存在
        if (employeeService.findByEmail(resume.getEmail()).isPresent()) {
            System.out.println("Email already exists: " + resume.getEmail());
            return null;
        }
        
        // 创建员工档案
        Employee employee = new Employee();
        // 不设置 employeeCode，让 EmployeeService 自动生成
        employee.setName(resume.getName());
        employee.setPhone(resume.getPhone());
        employee.setEmail(resume.getEmail());
        employee.setDepartmentId(departmentId);
        employee.setPositionId(positionId);
        employee.setEntryDate(entryDate);
        employee.setStatus("ACTIVE");
        
        // 填充部门和职位名称
        Department department = departmentRepository.findById(departmentId).orElse(null);
        if (department != null) {
            employee.setDepartmentName(department.getName());
        }
        
        Position position = positionRepository.findById(positionId).orElse(null);
        if (position != null) {
            employee.setPositionName(position.getName());
            employee.setPosition(position.getName());
        } else {
            employee.setPosition(offer.getJobTitle());
        }
        
        // 设置默认值
        employee.setGender("未知");
        employee.setAge(25);
        employee.setBirthDate(java.time.LocalDate.of(2000, 1, 1));
        employee.setHeight(170.0);
        employee.setWeight(60.0);
        
        try {
            Employee createdEmployee = employeeService.createEmployeeFromOffer(employee);
            if (createdEmployee != null) {
                // 更新 Offer 状态为已处理
                offer.setStatus("PROCESSED");
                offerRepository.save(offer);
                System.out.println("Offer status updated to PROCESSED, offerId: " + offerId);
            }
            return createdEmployee;
        } catch (Exception e) {
            System.out.println("Error creating employee: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}