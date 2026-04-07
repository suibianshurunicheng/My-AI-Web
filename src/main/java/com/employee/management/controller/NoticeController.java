package com.employee.management.controller;

import com.employee.management.dto.ApiResponse;
import com.employee.management.dto.PageResponse;
import com.employee.management.entity.Notice;
import com.employee.management.entity.NoticeReadRecord;
import com.employee.management.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    // 测试接口
    @GetMapping("/test")
    public ApiResponse<String> test() {
        System.out.println("测试接口被调用了！");
        return new ApiResponse<>(200, "success", "测试成功！");
    }

    // 获取员工可见的公告列表
    @GetMapping("/list")
    public ApiResponse<PageResponse<Map<String, Object>>> getEmployeeNotices(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        System.out.println("收到请求: employeeId=" + employeeId + ", page=" + page + ", size=" + size);
        if (employeeId == null) {
            employeeId = 1L; // 默认admin员工
        }
        System.out.println("使用 employeeId=" + employeeId);
        PageResponse<Map<String, Object>> response = noticeService.getEmployeeNotices(employeeId, page, size);
        System.out.println("返回数据: " + response);
        return new ApiResponse<>(200, "success", response);
    }

    // 获取管理员的公告列表
    @GetMapping("/admin/list")
    public ApiResponse<PageResponse<Notice>> getAdminNotices(
            @RequestParam(required = false) String scope,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<Notice> response = noticeService.getAdminNotices(scope, page, size);
        return new ApiResponse<>(200, "success", response);
    }

    // 获取公告详情
    @GetMapping("/{id}")
    public ApiResponse<Notice> getNoticeById(@PathVariable Long id) {
        Notice notice = noticeService.getNoticeById(id);
        if (notice == null) {
            return new ApiResponse<>(404, "Notice not found", null);
        }
        return new ApiResponse<>(200, "success", notice);
    }

    // 发布公告
    @PostMapping
    public ApiResponse<Notice> publishNotice(@RequestBody NoticeRequest noticeRequest) {
        Notice notice = new Notice();
        notice.setTitle(noticeRequest.getTitle());
        notice.setContent(noticeRequest.getContent());
        notice.setScope(noticeRequest.getScope());
        notice.setDepartmentId(noticeRequest.getDepartmentId());
        notice.setDepartmentName(noticeRequest.getDepartmentName());
        notice.setImportant(noticeRequest.getIsImportant());
        notice.setSendEmail(noticeRequest.getSendEmail());
        notice.setSendInternalMessage(noticeRequest.getSendInternalMessage());
        notice.setPublisherId(noticeRequest.getPublisherId());
        notice.setPublisherName(noticeRequest.getPublisherName());
        
        Notice savedNotice = noticeService.publishNotice(notice);
        // 初始化阅读记录
        noticeService.initializeReadRecords(savedNotice.getId());
        return new ApiResponse<>(200, "success", savedNotice);
    }

    // 更新公告
    @PutMapping("/{id}")
    public ApiResponse<Notice> updateNotice(@PathVariable Long id, @RequestBody NoticeRequest noticeRequest) {
        Notice notice = new Notice();
        notice.setTitle(noticeRequest.getTitle());
        notice.setContent(noticeRequest.getContent());
        notice.setScope(noticeRequest.getScope());
        notice.setDepartmentId(noticeRequest.getDepartmentId());
        notice.setDepartmentName(noticeRequest.getDepartmentName());
        notice.setImportant(noticeRequest.getIsImportant());
        notice.setSendEmail(noticeRequest.getSendEmail());
        notice.setSendInternalMessage(noticeRequest.getSendInternalMessage());
        
        Notice updatedNotice = noticeService.updateNotice(id, notice);
        if (updatedNotice == null) {
            return new ApiResponse<>(404, "Notice not found", null);
        }
        return new ApiResponse<>(200, "success", updatedNotice);
    }

    // 删除公告
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteNotice(@PathVariable Long id) {
        boolean deleted = noticeService.deleteNotice(id);
        if (!deleted) {
            return new ApiResponse<>(404, "Notice not found", null);
        }
        return new ApiResponse<>(200, "success", null);
    }

    // 标记公告已读
    @PostMapping("/{id}/read")
    public ApiResponse<NoticeReadRecord> markAsRead(
            @PathVariable Long id,
            @RequestBody MarkReadRequest request) {
        NoticeReadRecord record = noticeService.markAsRead(id, request.getEmployeeId());
        if (record == null) {
            return new ApiResponse<>(404, "Notice not found", null);
        }
        return new ApiResponse<>(200, "success", record);
    }

    // 获取公告阅读统计
    @GetMapping("/{id}/stats")
    public ApiResponse<Map<String, Object>> getNoticeStats(@PathVariable Long id) {
        Map<String, Object> stats = noticeService.getNoticeStats(id);
        return new ApiResponse<>(200, "success", stats);
    }

    // 获取阅读详情列表
    @GetMapping("/{id}/read-details")
    public ApiResponse<PageResponse<NoticeReadRecord>> getReadDetails(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<NoticeReadRecord> response = noticeService.getReadDetails(id, page, size);
        return new ApiResponse<>(200, "success", response);
    }

    // 辅助类
    public static class NoticeRequest {
        private String title;
        private String content;
        private String scope;
        private Long departmentId;
        private String departmentName;
        private Boolean isImportant;
        private Boolean sendEmail;
        private Boolean sendInternalMessage;
        private Long publisherId;
        private String publisherName;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public Long getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(Long departmentId) {
            this.departmentId = departmentId;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        public Boolean getIsImportant() {
            return isImportant;
        }

        public void setIsImportant(Boolean isImportant) {
            this.isImportant = isImportant;
        }

        public Boolean getSendEmail() {
            return sendEmail;
        }

        public void setSendEmail(Boolean sendEmail) {
            this.sendEmail = sendEmail;
        }

        public Boolean getSendInternalMessage() {
            return sendInternalMessage;
        }

        public void setSendInternalMessage(Boolean sendInternalMessage) {
            this.sendInternalMessage = sendInternalMessage;
        }

        public Long getPublisherId() {
            return publisherId;
        }

        public void setPublisherId(Long publisherId) {
            this.publisherId = publisherId;
        }

        public String getPublisherName() {
            return publisherName;
        }

        public void setPublisherName(String publisherName) {
            this.publisherName = publisherName;
        }
    }

    public static class MarkReadRequest {
        private Long employeeId;

        public Long getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(Long employeeId) {
            this.employeeId = employeeId;
        }
    }
}
