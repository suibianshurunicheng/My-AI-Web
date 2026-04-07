package com.employee.management.repository;

import com.employee.management.entity.NoticeReadRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeReadRecordRepository extends JpaRepository<NoticeReadRecord, Long> {
    Optional<NoticeReadRecord> findByNoticeIdAndEmployeeId(Long noticeId, Long employeeId);
    Page<NoticeReadRecord> findByNoticeId(Long noticeId, Pageable pageable);
    List<NoticeReadRecord> findByNoticeId(Long noticeId);
    long countByNoticeIdAndRead(Long noticeId, Boolean read);
    List<NoticeReadRecord> findByEmployeeId(Long employeeId);
}
