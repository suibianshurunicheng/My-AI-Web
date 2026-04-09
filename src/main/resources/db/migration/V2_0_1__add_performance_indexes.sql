-- ============================================
-- 数据库性能优化脚本
-- 版本：v2.0.1
-- 日期：2026-04-09
-- 内容：索引优化、查询优化
-- ============================================

-- 1. 为员工表创建索引
-- ============================================

-- 1.1 为 name 字段创建索引（优化模糊查询）
-- 注意：LIKE '%xx%' 无法使用索引，但 LIKE 'xx%' 可以使用
CREATE INDEX IF NOT EXISTS idx_employee_name ON employees(name);

-- 1.2 为 employee_code 字段创建唯一索引（优化精确查询和保证唯一性）
CREATE UNIQUE INDEX IF NOT EXISTS idx_employee_code ON employees(employee_code);

-- 1.3 为 department_id 创建索引（优化部门筛选查询）
CREATE INDEX IF NOT EXISTS idx_employee_department_id ON employees(department_id);

-- 1.4 为 status 创建索引（优化状态筛选）
CREATE INDEX IF NOT EXISTS idx_employee_status ON employees(status);

-- 1.5 创建组合索引（优化常用查询组合）
CREATE INDEX IF NOT EXISTS idx_employee_dept_status ON employees(department_id, status);
CREATE INDEX IF NOT EXISTS idx_employee_name_gender ON employees(name, gender);

-- 2. 为其他表创建索引
-- ============================================

-- 2.1 登录日志表
CREATE INDEX IF NOT EXISTS idx_login_logs_username ON login_logs(username);
CREATE INDEX IF NOT EXISTS idx_login_logs_created_at ON login_logs(created_at);

-- 2.2 招聘职位表
CREATE INDEX IF NOT EXISTS idx_recruitment_jobs_status ON recruitment_jobs(status);
CREATE INDEX IF NOT EXISTS idx_recruitment_jobs_department ON recruitment_jobs(department_id);

-- 2.3 简历表
CREATE INDEX IF NOT EXISTS idx_resumes_job_id ON resumes(job_id);
CREATE INDEX IF NOT EXISTS idx_resumes_status ON resumes(status);

-- 2.4 面试安排表
CREATE INDEX IF NOT EXISTS idx_interviews_resume_id ON interviews(resume_id);
CREATE INDEX IF NOT EXISTS idx_interviews_status ON interviews(status);

-- 2.5 录用通知表
CREATE INDEX IF NOT EXISTS idx_offers_resume_id ON offers(resume_id);
CREATE INDEX IF NOT EXISTS idx_offers_status ON offers(status);

-- 3. 查看索引创建情况
-- ============================================
-- SELECT 
--     tablename,
--     indexname,
--     indexdef
-- FROM 
--     pg_indexes
-- WHERE 
--     schemaname = 'public'
-- ORDER BY 
--     tablename, indexname;

-- 4. 分析表统计信息（优化查询计划）
-- ============================================
ANALYZE employees;
ANALYZE login_logs;
ANALYZE recruitment_jobs;
ANALYZE resumes;
ANALYZE interviews;
ANALYZE offers;

-- 5. 优化建议（注释形式，不执行）
-- ============================================
/*
-- 5.1 对于大数据量表，考虑使用分区表
-- ALTER TABLE employees PARTITION BY RANGE (created_at);

-- 5.2 对于频繁更新的表，定期执行 VACUUM
-- VACUUM ANALYZE employees;

-- 5.3 监控慢查询
-- 在 application.yml 中配置：
-- spring:
--   jpa:
--     properties:
--       hibernate:
--         generate_statistics: true

-- 5.4 使用查询缓存（如果适用）
-- spring:
--   jpa:
--     properties:
--       hibernate:
--         cache:
--           use_second_level_cache: true
*/
