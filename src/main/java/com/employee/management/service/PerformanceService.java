package com.employee.management.service;

import com.employee.management.entity.*;
import com.employee.management.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PerformanceService {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceService.class);

    @Autowired
    private KpiIndicatorRepository kpiIndicatorRepository;

    @Autowired
    private PerformanceCycleRepository performanceCycleRepository;

    @Autowired
    private PerformanceScoreRepository performanceScoreRepository;

    @Autowired
    private PerformanceResultRepository performanceResultRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<KpiIndicator> getAllKpiIndicators() {
        return kpiIndicatorRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Optional<KpiIndicator> getKpiIndicatorById(Long id) {
        return kpiIndicatorRepository.findById(id);
    }

    @Transactional
    public KpiIndicator createKpiIndicator(KpiIndicator indicator) {
        return kpiIndicatorRepository.save(indicator);
    }

    @Transactional
    public KpiIndicator updateKpiIndicator(Long id, KpiIndicator indicator) {
        return kpiIndicatorRepository.findById(id).map(existing -> {
            existing.setName(indicator.getName());
            existing.setDescription(indicator.getDescription());
            existing.setPosition(indicator.getPosition());
            existing.setWeight(indicator.getWeight());
            existing.setIsActive(indicator.getIsActive());
            return kpiIndicatorRepository.save(existing);
        }).orElse(null);
    }

    @Transactional
    public void deleteKpiIndicator(Long id) {
        kpiIndicatorRepository.deleteById(id);
    }

    public Page<PerformanceCycle> getPerformanceCycles(int page, int size, String cycleType) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        if (cycleType != null && !cycleType.isEmpty()) {
            return performanceCycleRepository.findByCycleType(cycleType, pageable);
        }
        return performanceCycleRepository.findAll(pageable);
    }

    public Optional<PerformanceCycle> getPerformanceCycleById(Long id) {
        return performanceCycleRepository.findById(id);
    }

    @Transactional
    public PerformanceCycle createPerformanceCycle(PerformanceCycle cycle) {
        return performanceCycleRepository.save(cycle);
    }

    @Transactional
    public PerformanceCycle updatePerformanceCycle(Long id, PerformanceCycle cycle) {
        return performanceCycleRepository.findById(id).map(existing -> {
            existing.setName(cycle.getName());
            existing.setCycleType(cycle.getCycleType());
            existing.setStartDate(cycle.getStartDate());
            existing.setEndDate(cycle.getEndDate());
            existing.setSelfEvalStart(cycle.getSelfEvalStart());
            existing.setSelfEvalEnd(cycle.getSelfEvalEnd());
            existing.setSuperEvalStart(cycle.getSuperEvalStart());
            existing.setSuperEvalEnd(cycle.getSuperEvalEnd());
            existing.setStatus(cycle.getStatus());
            return performanceCycleRepository.save(existing);
        }).orElse(null);
    }

    @Transactional
    public void deletePerformanceCycle(Long id) {
        performanceCycleRepository.deleteById(id);
    }

    public List<PerformanceScore> getScoresByCycle(Long cycleId) {
        return performanceScoreRepository.findByCycleId(cycleId);
    }

    public List<PerformanceScore> getScoresByCycleAndEmployee(Long cycleId, Long employeeId) {
        return performanceScoreRepository.findByCycleIdAndEmployeeId(cycleId, employeeId);
    }

    @Transactional
    public PerformanceScore createOrUpdateScore(PerformanceScore score) {
        return performanceScoreRepository.save(score);
    }

    public Page<PerformanceResult> getResultsByCycle(Long cycleId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        return performanceResultRepository.findByCycleId(cycleId, pageable);
    }

    public Optional<PerformanceResult> getResultById(Long id) {
        return performanceResultRepository.findById(id);
    }

    @Transactional
    public PerformanceResult createOrUpdateResult(PerformanceResult result) {
        return performanceResultRepository.save(result);
    }

    @Transactional
    public void calculateResults(Long cycleId) {
        logger.info("开始计算考核结果: cycleId={}", cycleId);

        List<Employee> employees = employeeRepository.findAll();
        // List<KpiIndicator> indicators = kpiIndicatorRepository.findByIsActiveTrue();

        PerformanceCycle cycle = performanceCycleRepository.findById(cycleId).orElse(null);
        if (cycle == null) {
            logger.error("考核周期不存在: cycleId={}", cycleId);
            return;
        }

        for (Employee employee : employees) {
            List<PerformanceScore> scores = getScoresByCycleAndEmployee(cycleId, employee.getId());

            BigDecimal totalScore = BigDecimal.ZERO;
            BigDecimal totalWeight = BigDecimal.ZERO;

            for (PerformanceScore score : scores) {
                if (score.getFinalScore() != null) {
                    totalScore = totalScore.add(score.getFinalScore().multiply(score.getKpiWeight()));
                    totalWeight = totalWeight.add(score.getKpiWeight());
                }
            }

            if (totalWeight.compareTo(BigDecimal.ZERO) > 0) {
                totalScore = totalScore.divide(totalWeight, 2, BigDecimal.ROUND_HALF_UP);
            }

            String grade = calculateGrade(totalScore);

            PerformanceResult result = new PerformanceResult();
            result.setCycleId(cycleId);
            result.setCycleName(cycle.getName());
            result.setEmployeeId(employee.getId());
            result.setEmployeeName(employee.getName());
            result.setEmployeeCode(employee.getEmployeeCode());
            result.setTotalScore(totalScore);
            result.setGrade(grade);
            result.setStatus("COMPLETED");

            performanceResultRepository.save(result);
            logger.info("员工 {} 考核结果已计算: score={}, grade={}", employee.getName(), totalScore, grade);
        }
    }

    private String calculateGrade(BigDecimal score) {
        if (score.compareTo(new BigDecimal("90")) >= 0) return "A";
        if (score.compareTo(new BigDecimal("80")) >= 0) return "B";
        if (score.compareTo(new BigDecimal("70")) >= 0) return "C";
        if (score.compareTo(new BigDecimal("60")) >= 0) return "D";
        return "E";
    }
}
