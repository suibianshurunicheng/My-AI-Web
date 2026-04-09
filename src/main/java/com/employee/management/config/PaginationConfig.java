package com.employee.management.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 分页配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.pagination")
public class PaginationConfig {
    
    /**
     * 最大页码数
     */
    private Integer maxPageSize = 100;
    
    /**
     * 默认页大小
     */
    private Integer defaultPageSize = 10;
    
    /**
     * 最大返回记录数
     */
    private Integer maxPageResult = 1000;
}
