package com.employee.management.controller;

import com.employee.management.entity.*;
import com.employee.management.service.RecruitmentService;
import com.employee.management.dto.ApiResponse;
import com.employee.management.dto.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/recruitment")
public class RecruitmentController {
    
    @Autowired
    private RecruitmentService recruitmentService;
    
    // 职位管理
    @GetMapping("/jobs")
    public ApiResponse<PageResponse<Job>> getJobs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long positionId,
            @RequestParam(required = false) String status) {
        PageResponse<Job> jobs = recruitmentService.getJobs(page, size, departmentId, positionId, status);
        return new ApiResponse<>(200, "success", jobs);
    }
    
    @PostMapping("/jobs")
    public ApiResponse<Job> createJob(@RequestBody Job job) {
        Job createdJob = recruitmentService.createJob(job);
        return new ApiResponse<>(200, "success", createdJob);
    }
    
    @PutMapping("/jobs/{id}")
    public ApiResponse<Job> updateJob(@PathVariable Long id, @RequestBody Job job) {
        Job updatedJob = recruitmentService.updateJob(id, job);
        if (updatedJob == null) {
            return new ApiResponse<>(404, "Job not found", null);
        }
        return new ApiResponse<>(200, "success", updatedJob);
    }
    
    @DeleteMapping("/jobs/{id}")
    public ApiResponse<Void> deleteJob(@PathVariable Long id) {
        recruitmentService.deleteJob(id);
        return new ApiResponse<>(200, "success", null);
    }
    
    // 简历管理
    @GetMapping("/resumes")
    public ApiResponse<PageResponse<Resume>> getResumes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long jobId,
            @RequestParam(required = false) String status) {
        PageResponse<Resume> resumes = recruitmentService.getResumes(page, size, jobId, status);
        return new ApiResponse<>(200, "success", resumes);
    }
    
    @PostMapping("/resumes/upload")
    public ApiResponse<Resume> uploadResume(
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam String email,
            @RequestParam Long jobId,
            @RequestParam("resumeFile") MultipartFile resumeFile) {
        try {
            Resume resume = recruitmentService.uploadResume(name, phone, email, jobId, resumeFile);
            return new ApiResponse<>(200, "success", resume);
        } catch (IOException e) {
            return new ApiResponse<>(500, "File upload failed", null);
        }
    }
    
    @PutMapping("/resumes/{id}/status")
    public ApiResponse<Resume> updateResumeStatus(@PathVariable Long id, @RequestBody StatusRequest statusRequest) {
        Resume resume = recruitmentService.updateResumeStatus(id, statusRequest.getStatus());
        if (resume == null) {
            return new ApiResponse<>(404, "Resume not found", null);
        }
        return new ApiResponse<>(200, "success", resume);
    }
    
    // 面试管理
    @GetMapping("/interviews")
    public ApiResponse<PageResponse<Interview>> getInterviews(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long resumeId,
            @RequestParam(required = false) String status) {
        PageResponse<Interview> interviews = recruitmentService.getInterviews(page, size, resumeId, status);
        return new ApiResponse<>(200, "success", interviews);
    }
    
    @PostMapping("/interviews")
    public ApiResponse<Interview> scheduleInterview(@RequestBody InterviewRequest interviewRequest) {
        Interview interview = recruitmentService.scheduleInterview(
                interviewRequest.getResumeId(),
                interviewRequest.getInterviewers(),
                interviewRequest.getLocation(),
                interviewRequest.getInterviewTime());
        if (interview == null) {
            return new ApiResponse<>(404, "Resume not found", null);
        }
        return new ApiResponse<>(200, "success", interview);
    }
    
    @PutMapping("/interviews/{id}/evaluate")
    public ApiResponse<Interview> evaluateInterview(@PathVariable Long id, @RequestBody EvaluationRequest evaluationRequest) {
        Interview interview = recruitmentService.evaluateInterview(id, evaluationRequest.getEvaluation(), evaluationRequest.getResult());
        if (interview == null) {
            return new ApiResponse<>(404, "Interview not found", null);
        }
        return new ApiResponse<>(200, "success", interview);
    }
    
    @PutMapping("/interviews/{id}")
    public ApiResponse<Interview> updateInterview(@PathVariable Long id, @RequestBody Interview interview) {
        Interview updatedInterview = recruitmentService.updateInterview(id, interview);
        if (updatedInterview == null) {
            return new ApiResponse<>(404, "Interview not found", null);
        }
        return new ApiResponse<>(200, "success", updatedInterview);
    }
    
    // 录用管理
    @GetMapping("/offers")
    public ApiResponse<PageResponse<Offer>> getOffers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {
        PageResponse<Offer> offers = recruitmentService.getOffers(page, size, status);
        return new ApiResponse<>(200, "success", offers);
    }
    
    @PostMapping("/offers")
    public ApiResponse<Offer> createOffer(@RequestBody OfferRequest offerRequest) {
        Offer offer = recruitmentService.createOffer(
                offerRequest.getResumeId(),
                offerRequest.getSalary(),
                offerRequest.getJoinDate());
        if (offer == null) {
            return new ApiResponse<>(404, "Resume not found", null);
        }
        return new ApiResponse<>(200, "success", offer);
    }
    
    @PutMapping("/offers/{id}/process")
    public ApiResponse<Offer> processOffer(@PathVariable Long id, @RequestBody StatusRequest statusRequest) {
        Offer offer = recruitmentService.processOffer(id, statusRequest.getStatus());
        if (offer == null) {
            return new ApiResponse<>(404, "Offer not found", null);
        }
        return new ApiResponse<>(200, "success", offer);
    }
    
    @PutMapping("/offers/{id}")
    public ApiResponse<Offer> updateOffer(@PathVariable Long id, @RequestBody Offer offer) {
        Offer updatedOffer = recruitmentService.updateOffer(id, offer);
        if (updatedOffer == null) {
            return new ApiResponse<>(404, "Offer not found", null);
        }
        return new ApiResponse<>(200, "success", updatedOffer);
    }
    
    @PutMapping("/offers/{id}/approve")
    public ApiResponse<Offer> approveOffer(@PathVariable Long id) {
        Offer offer = recruitmentService.processOffer(id, "APPROVED");
        if (offer == null) {
            return new ApiResponse<>(404, "Offer not found", null);
        }
        return new ApiResponse<>(200, "success", offer);
    }
    
    @PostMapping("/offers/{id}/create-employee")
    public ApiResponse<Employee> createEmployeeFromOffer(@PathVariable Long id, @RequestBody EmployeeRequest employeeRequest) {
        Employee employee = recruitmentService.createEmployeeFromOffer(
                id,
                employeeRequest.getEmployeeCode(),
                employeeRequest.getDepartmentId(),
                employeeRequest.getPositionId(),
                employeeRequest.getEntryDate());
        if (employee == null) {
            return new ApiResponse<>(400, "Failed to create employee", null);
        }
        return new ApiResponse<>(200, "success", employee);
    }
    
    // 辅助类
    public static class StatusRequest {
        private String status;
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
    }
    
    public static class InterviewRequest {
        private Long resumeId;
        private LocalDateTime interviewTime;
        private String interviewers;
        private String location;
        
        public Long getResumeId() {
            return resumeId;
        }
        
        public void setResumeId(Long resumeId) {
            this.resumeId = resumeId;
        }
        
        public LocalDateTime getInterviewTime() {
            return interviewTime;
        }
        
        public void setInterviewTime(LocalDateTime interviewTime) {
            this.interviewTime = interviewTime;
        }
        
        public String getInterviewers() {
            return interviewers;
        }
        
        public void setInterviewers(String interviewers) {
            this.interviewers = interviewers;
        }
        
        public String getLocation() {
            return location;
        }
        
        public void setLocation(String location) {
            this.location = location;
        }
    }
    
    public static class EvaluationRequest {
        private String evaluation;
        private String result;
        
        public String getEvaluation() {
            return evaluation;
        }
        
        public void setEvaluation(String evaluation) {
            this.evaluation = evaluation;
        }
        
        public String getResult() {
            return result;
        }
        
        public void setResult(String result) {
            this.result = result;
        }
    }
    
    public static class OfferRequest {
        private Long resumeId;
        private String salary;
        private LocalDate joinDate;
        
        public Long getResumeId() {
            return resumeId;
        }
        
        public void setResumeId(Long resumeId) {
            this.resumeId = resumeId;
        }
        
        public String getSalary() {
            return salary;
        }
        
        public void setSalary(String salary) {
            this.salary = salary;
        }
        
        public LocalDate getJoinDate() {
            return joinDate;
        }
        
        public void setJoinDate(LocalDate joinDate) {
            this.joinDate = joinDate;
        }
    }
    
    public static class EmployeeRequest {
        private String employeeCode;
        private Long departmentId;
        private Long positionId;
        private LocalDate entryDate;
        
        public String getEmployeeCode() {
            return employeeCode;
        }
        
        public void setEmployeeCode(String employeeCode) {
            this.employeeCode = employeeCode;
        }
        
        public Long getDepartmentId() {
            return departmentId;
        }
        
        public void setDepartmentId(Long departmentId) {
            this.departmentId = departmentId;
        }
        
        public Long getPositionId() {
            return positionId;
        }
        
        public void setPositionId(Long positionId) {
            this.positionId = positionId;
        }
        
        public LocalDate getEntryDate() {
            return entryDate;
        }
        
        public void setEntryDate(LocalDate entryDate) {
            this.entryDate = entryDate;
        }
    }
}