package com.employee.management.controller;

import com.employee.management.annotation.PreventDuplicateSubmit;
import com.employee.management.dto.ApiResponse;
import com.employee.management.dto.PageResponse;
import com.employee.management.entity.*;
import com.employee.management.service.PerformanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/performance")
public class PerformanceController {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceController.class);

    @Autowired
    private PerformanceService performanceService;

    @GetMapping("/kpi-indicators")
    public ApiResponse<List<KpiIndicator>> getAllKpiIndicators() {
        try {
            List<KpiIndicator> indicators = performanceService.getAllKpiIndicators();
            return ApiResponse.success(indicators);
        } catch (Exception e) {
            logger.error("获取 KPI 指标失败", e);
            return ApiResponse.error(500, "获取 KPI 指标失败: " + e.getMessage());
        }
    }

    @GetMapping("/kpi-indicators/{id}")
    public ApiResponse<KpiIndicator> getKpiIndicatorById(@PathVariable Long id) {
        try {
            Optional<KpiIndicator> indicator = performanceService.getKpiIndicatorById(id);
            if (indicator.isPresent()) {
                return ApiResponse.success(indicator.get());
            }
            return ApiResponse.error(404, "KPI 指标不存在");
        } catch (Exception e) {
            logger.error("获取 KPI 指标失败", e);
            return ApiResponse.error(500, "获取 KPI 指标失败: " + e.getMessage());
        }
    }

    @PostMapping("/kpi-indicators")
    @PreventDuplicateSubmit
    public ApiResponse<KpiIndicator> createKpiIndicator(@RequestBody KpiIndicator indicator) {
        try {
            KpiIndicator created = performanceService.createKpiIndicator(indicator);
            return ApiResponse.success(created);
        } catch (Exception e) {
            logger.error("创建 KPI 指标失败", e);
            return ApiResponse.error(500, "创建 KPI 指标失败: " + e.getMessage());
        }
    }

    @PutMapping("/kpi-indicators/{id}")
    @PreventDuplicateSubmit
    public ApiResponse<KpiIndicator> updateKpiIndicator(@PathVariable Long id, @RequestBody KpiIndicator indicator) {
        try {
            KpiIndicator updated = performanceService.updateKpiIndicator(id, indicator);
            if (updated != null) {
                return ApiResponse.success(updated);
            }
            return ApiResponse.error(404, "KPI 指标不存在");
        } catch (Exception e) {
            logger.error("更新 KPI 指标失败", e);
            return ApiResponse.error(500, "更新 KPI 指标失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/kpi-indicators/{id}")
    public ApiResponse<Void> deleteKpiIndicator(@PathVariable Long id) {
        try {
            performanceService.deleteKpiIndicator(id);
            return ApiResponse.success(null);
        } catch (Exception e) {
            logger.error("删除 KPI 指标失败", e);
            return ApiResponse.error(500, "删除 KPI 指标失败: " + e.getMessage());
        }
    }

    @GetMapping("/cycles")
    public ApiResponse<PageResponse<PerformanceCycle>> getPerformanceCycles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String cycleType) {
        try {
            Page<PerformanceCycle> result = performanceService.getPerformanceCycles(page, size, cycleType);
            PageResponse<PerformanceCycle> pageResponse = new PageResponse<>();
            pageResponse.setList(result.getContent());
            pageResponse.setTotal((int) result.getTotalElements());
            pageResponse.setPage(result.getNumber() + 1);
            pageResponse.setSize(result.getSize());
            return ApiResponse.success(pageResponse);
        } catch (Exception e) {
            logger.error("获取考核周期失败", e);
            return ApiResponse.error(500, "获取考核周期失败: " + e.getMessage());
        }
    }

    @GetMapping("/cycles/{id}")
    public ApiResponse<PerformanceCycle> getPerformanceCycleById(@PathVariable Long id) {
        try {
            Optional<PerformanceCycle> cycle = performanceService.getPerformanceCycleById(id);
            if (cycle.isPresent()) {
                return ApiResponse.success(cycle.get());
            }
            return ApiResponse.error(404, "考核周期不存在");
        } catch (Exception e) {
            logger.error("获取考核周期失败", e);
            return ApiResponse.error(500, "获取考核周期失败: " + e.getMessage());
        }
    }

    @PostMapping("/cycles")
    @PreventDuplicateSubmit
    public ApiResponse<PerformanceCycle> createPerformanceCycle(@RequestBody PerformanceCycle cycle) {
        try {
            PerformanceCycle created = performanceService.createPerformanceCycle(cycle);
            return ApiResponse.success(created);
        } catch (Exception e) {
            logger.error("创建考核周期失败", e);
            return ApiResponse.error(500, "创建考核周期失败: " + e.getMessage());
        }
    }

    @PutMapping("/cycles/{id}")
    @PreventDuplicateSubmit
    public ApiResponse<PerformanceCycle> updatePerformanceCycle(@PathVariable Long id, @RequestBody PerformanceCycle cycle) {
        try {
            PerformanceCycle updated = performanceService.updatePerformanceCycle(id, cycle);
            if (updated != null) {
                return ApiResponse.success(updated);
            }
            return ApiResponse.error(404, "考核周期不存在");
        } catch (Exception e) {
            logger.error("更新考核周期失败", e);
            return ApiResponse.error(500, "更新考核周期失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/cycles/{id}")
    public ApiResponse<Void> deletePerformanceCycle(@PathVariable Long id) {
        try {
            performanceService.deletePerformanceCycle(id);
            return ApiResponse.success(null);
        } catch (Exception e) {
            logger.error("删除考核周期失败", e);
            return ApiResponse.error(500, "删除考核周期失败: " + e.getMessage());
        }
    }

    @GetMapping("/scores/cycle/{cycleId}")
    public ApiResponse<List<PerformanceScore>> getScoresByCycle(@PathVariable Long cycleId) {
        try {
            List<PerformanceScore> scores = performanceService.getScoresByCycle(cycleId);
            return ApiResponse.success(scores);
        } catch (Exception e) {
            logger.error("获取评分失败", e);
            return ApiResponse.error(500, "获取评分失败: " + e.getMessage());
        }
    }

    @GetMapping("/scores/cycle/{cycleId}/employee/{employeeId}")
    public ApiResponse<List<PerformanceScore>> getScoresByCycleAndEmployee(
            @PathVariable Long cycleId,
            @PathVariable Long employeeId) {
        try {
            List<PerformanceScore> scores = performanceService.getScoresByCycleAndEmployee(cycleId, employeeId);
            return ApiResponse.success(scores);
        } catch (Exception e) {
            logger.error("获取评分失败", e);
            return ApiResponse.error(500, "获取评分失败: " + e.getMessage());
        }
    }

    @PostMapping("/scores")
    @PreventDuplicateSubmit
    public ApiResponse<PerformanceScore> createOrUpdateScore(@RequestBody PerformanceScore score) {
        try {
            PerformanceScore saved = performanceService.createOrUpdateScore(score);
            return ApiResponse.success(saved);
        } catch (Exception e) {
            logger.error("保存评分失败", e);
            return ApiResponse.error(500, "保存评分失败: " + e.getMessage());
        }
    }

    @GetMapping("/results/cycle/{cycleId}")
    public ApiResponse<PageResponse<PerformanceResult>> getResultsByCycle(
            @PathVariable Long cycleId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<PerformanceResult> result = performanceService.getResultsByCycle(cycleId, page, size);
            PageResponse<PerformanceResult> pageResponse = new PageResponse<>();
            pageResponse.setList(result.getContent());
            pageResponse.setTotal((int) result.getTotalElements());
            pageResponse.setPage(result.getNumber() + 1);
            pageResponse.setSize(result.getSize());
            return ApiResponse.success(pageResponse);
        } catch (Exception e) {
            logger.error("获取考核结果失败", e);
            return ApiResponse.error(500, "获取考核结果失败: " + e.getMessage());
        }
    }

    @GetMapping("/results/{id}")
    public ApiResponse<PerformanceResult> getResultById(@PathVariable Long id) {
        try {
            Optional<PerformanceResult> result = performanceService.getResultById(id);
            if (result.isPresent()) {
                return ApiResponse.success(result.get());
            }
            return ApiResponse.error(404, "考核结果不存在");
        } catch (Exception e) {
            logger.error("获取考核结果失败", e);
            return ApiResponse.error(500, "获取考核结果失败: " + e.getMessage());
        }
    }

    @PostMapping("/results")
    @PreventDuplicateSubmit
    public ApiResponse<PerformanceResult> createOrUpdateResult(@RequestBody PerformanceResult result) {
        try {
            PerformanceResult saved = performanceService.createOrUpdateResult(result);
            return ApiResponse.success(saved);
        } catch (Exception e) {
            logger.error("保存考核结果失败", e);
            return ApiResponse.error(500, "保存考核结果失败: " + e.getMessage());
        }
    }

    @PostMapping("/cycles/{cycleId}/calculate")
    @PreventDuplicateSubmit
    public ApiResponse<Void> calculateResults(@PathVariable Long cycleId) {
        try {
            performanceService.calculateResults(cycleId);
            return ApiResponse.success(null);
        } catch (Exception e) {
            logger.error("计算考核结果失败", e);
            return ApiResponse.error(500, "计算考核结果失败: " + e.getMessage());
        }
    }
}
