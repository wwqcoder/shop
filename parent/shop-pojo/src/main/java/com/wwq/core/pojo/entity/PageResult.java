package com.wwq.core.pojo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果封装对象
 * @param <T>
 */
public class PageResult<T> implements Serializable {

    //总记录数
    private Long total;

    //当前结果封装结果
    private List<T> rows;

    public PageResult(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
