package com.riverstar.model.base;

import lombok.Data;

import java.util.List;

/**
 * Author: Hardy
 * Date:   2018/7/30 21:25
 * Description:
 **/
@Data
public class RestPage<T> {
    // 当前页码
    private int pageNo;

    private int pageSize;

    private long totalCount;

    private int totalPage;

    private List<T> list;

    public RestPage(int pageNo, int pageSize, long totalCount) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.totalPage = (int) ((totalCount / pageSize) + (totalCount % pageSize > 0 ? 1 : 0));
    }

    public RestPage(int pageNo, int pageSize, long totalCount, int totalPage) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.totalPage = totalPage;
    }

    public RestPage<T> list(List<T> list) {
        this.list = list;
        return this;
    }
}
