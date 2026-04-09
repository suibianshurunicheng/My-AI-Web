package com.employee.management.dto;

import lombok.Data;

/**
 * 游标分页请求参数
 */
@Data
public class CursorPageRequest {
    
    /**
     * 游标（上一页最后一条记录的 ID）
     */
    private Long cursor;
    
    /**
     * 每页大小
     */
    private Integer size = 10;
    
    /**
     * 排序字段
     */
    private String sortField = "id";
    
    /**
     * 排序方向：asc, desc
     */
    private String sortDirection = "desc";
}
