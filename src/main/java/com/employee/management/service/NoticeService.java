package com.employee.management.service;

import com.employee.management.dto.PageResponse;
import com.employee.management.entity.Employee;
import com.employee.management.entity.Notice;
import com.employee.management.entity.NoticeReadRecord;
import com.employee.management.repository.EmployeeRepository;
import com.employee.management.repository.NoticeReadRecordRepository;
import com.employee.management.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private NoticeReadRecordRepository noticeReadRecordRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // 获取员工可见的公告列表
    public PageResponse<Map<String, Object>> getEmployeeNotices(Long employeeId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "publishTime"));
        
        Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
        Long departmentId = employeeOpt.map(Employee::getDepartmentId).orElse(null);
        
        Page<Notice> notices;
        if (departmentId != null) {
            notices = noticeRepository.findByScopeOrDepartmentId("ALL", departmentId, pageable);
        } else {
            notices = noticeRepository.findByScope("ALL", pageable);
        }
        
        List<Map<String, Object>> list = new ArrayList<>();
        for (Notice notice : notices) {
            Map<String, Object> noticeMap = new HashMap<>();
            noticeMap.put("id", notice.getId());
            noticeMap.put("title", notice.getTitle());
            noticeMap.put("content", notice.getContent());
            noticeMap.put("scope", notice.getScope());
            noticeMap.put("departmentId", notice.getDepartmentId());
            noticeMap.put("departmentName", notice.getDepartmentName());
            noticeMap.put("isImportant", notice.getImportant());
            noticeMap.put("sendEmail", notice.getSendEmail());
            noticeMap.put("sendInternalMessage", notice.getSendInternalMessage());
            noticeMap.put("publisherId", notice.getPublisherId());
            noticeMap.put("publisherName", notice.getPublisherName());
            noticeMap.put("publishTime", notice.getPublishTime());
            noticeMap.put("viewCount", notice.getViewCount());
            
            Optional<NoticeReadRecord> readRecordOpt = noticeReadRecordRepository.findByNoticeIdAndEmployeeId(notice.getId(), employeeId);
            noticeMap.put("isRead", readRecordOpt.map(NoticeReadRecord::getRead).orElse(false));
            
            list.add(noticeMap);
        }
        
        return new PageResponse<Map<String, Object>>((int) notices.getTotalElements(), notices.getNumber() + 1, notices.getSize(), list);
    }

    // 获取管理员的公告列表
    public PageResponse<Notice> getAdminNotices(String scope, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "publishTime"));
        Page<Notice> notices;
        
        if (scope != null && !scope.isEmpty()) {
            notices = noticeRepository.findByScope(scope, pageable);
        } else {
            notices = noticeRepository.findAll(pageable);
        }
        
        return new PageResponse<Notice>((int) notices.getTotalElements(), notices.getNumber() + 1, notices.getSize(), notices.getContent());
    }

    // 获取公告详情
    public Notice getNoticeById(Long id) {
        return noticeRepository.findById(id).orElse(null);
    }

    // 发布公告
    public Notice publishNotice(Notice notice) {
        if (notice.getViewCount() == null) {
            notice.setViewCount(0);
        }
        return noticeRepository.save(notice);
    }

    // 更新公告
    public Notice updateNotice(Long id, Notice notice) {
        Notice existingNotice = noticeRepository.findById(id).orElse(null);
        if (existingNotice == null) {
            return null;
        }
        
        if (notice.getTitle() != null) {
            existingNotice.setTitle(notice.getTitle());
        }
        if (notice.getContent() != null) {
            existingNotice.setContent(notice.getContent());
        }
        if (notice.getScope() != null) {
            existingNotice.setScope(notice.getScope());
        }
        if (notice.getDepartmentId() != null) {
            existingNotice.setDepartmentId(notice.getDepartmentId());
        }
        if (notice.getDepartmentName() != null) {
            existingNotice.setDepartmentName(notice.getDepartmentName());
        }
        if (notice.getImportant() != null) {
            existingNotice.setImportant(notice.getImportant());
        }
        
        return noticeRepository.save(existingNotice);
    }

    // 删除公告
    public boolean deleteNotice(Long id) {
        if (!noticeRepository.existsById(id)) {
            return false;
        }
        noticeRepository.deleteById(id);
        return true;
    }

    // 标记公告已读
    public NoticeReadRecord markAsRead(Long noticeId, Long employeeId) {
        Notice notice = noticeRepository.findById(noticeId).orElse(null);
        if (notice == null) {
            return null;
        }
        
        Optional<NoticeReadRecord> existingRecord = noticeReadRecordRepository.findByNoticeIdAndEmployeeId(noticeId, employeeId);
        NoticeReadRecord record;
        
        if (existingRecord.isPresent()) {
            record = existingRecord.get();
            if (!record.getRead()) {
                record.setRead(true);
                record.setReadTime(LocalDateTime.now());
                notice.setViewCount(notice.getViewCount() + 1);
                noticeRepository.save(notice);
            }
        } else {
            record = new NoticeReadRecord();
            record.setNoticeId(noticeId);
            record.setEmployeeId(employeeId);
            
            Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
            if (employeeOpt.isPresent()) {
                Employee employee = employeeOpt.get();
                record.setEmployeeName(employee.getName());
                record.setDepartmentName(employee.getDepartmentName());
            }
            
            record.setRead(true);
            record.setReadTime(LocalDateTime.now());
            notice.setViewCount(notice.getViewCount() + 1);
            noticeRepository.save(notice);
        }
        
        return noticeReadRecordRepository.save(record);
    }

    // 获取公告阅读统计
    public Map<String, Object> getNoticeStats(Long noticeId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("noticeId", noticeId);
        
        long totalEmployees = employeeRepository.count();
        long readCount = noticeReadRecordRepository.countByNoticeIdAndRead(noticeId, true);
        
        stats.put("totalEmployees", totalEmployees);
        stats.put("readCount", readCount);
        stats.put("unreadCount", totalEmployees - readCount);
        stats.put("readRate", totalEmployees > 0 ? (double) readCount / totalEmployees : 0);
        
        return stats;
    }

    // 获取阅读详情列表
    public PageResponse<NoticeReadRecord> getReadDetails(Long noticeId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<NoticeReadRecord> records = noticeReadRecordRepository.findByNoticeId(noticeId, pageable);
        
        return new PageResponse<NoticeReadRecord>((int) records.getTotalElements(), records.getNumber() + 1, records.getSize(), records.getContent());
    }

    // 初始化阅读记录（当发布新公告时）
    public void initializeReadRecords(Long noticeId) {
        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            NoticeReadRecord record = new NoticeReadRecord();
            record.setNoticeId(noticeId);
            record.setEmployeeId(employee.getId());
            record.setEmployeeName(employee.getName());
            record.setDepartmentName(employee.getDepartmentName());
            record.setRead(false);
            noticeReadRecordRepository.save(record);
        }
    }
}
