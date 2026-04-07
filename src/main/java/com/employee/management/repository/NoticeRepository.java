package com.employee.management.repository;

import com.employee.management.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findByScope(String scope, Pageable pageable);
    Page<Notice> findByScopeOrDepartmentId(String scope, Long departmentId, Pageable pageable);
    List<Notice> findByScopeOrDepartmentId(String scope, Long departmentId);
}
