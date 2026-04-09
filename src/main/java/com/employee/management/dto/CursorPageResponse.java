package com.employee.management.dto;

import lombok.Data;
import java.util.List;

/**
 * 游标分页响应对象
 */
@Data
public class CursorPageResponse<T> {
    
    /**
     * 数据列表
     */
    private List<T> data;
    
    /**
     * 下一个游标（下一页的起点）
     */
    private Long nextCursor;
    
    /**
     * 是否有更多数据
     */
    private Boolean hasNext = false;
    
    /**
     * 当前页大小
     */
    private Integer size;
    
    public static <T> CursorPageResponse<T> of(List<T> data, Long nextCursor, Integer size) {
        CursorPageResponse<T> response = new CursorPageResponse<>();
        response.setData(data);
        response.setNextCursor(nextCursor);
        response.setHasNext(data.size() > 0 && data.size() >= size);
        response.setSize(size);
        return response;
    }
}
