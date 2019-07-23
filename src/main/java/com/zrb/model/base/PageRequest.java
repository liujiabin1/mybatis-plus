package com.zrb.model.base;

import lombok.Setter;

/**
 * Author: Hardy
 * Date:   2018/7/31 10:53
 * Description:
 **/
@Setter
public class PageRequest {

    // 当前页码
    protected int pageNo;

    // 页面条数
    protected int pageSize;

    public int getPageNo() {
        return pageNo <= 0 ? 1 : pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageSize(int defSize) {
        return pageSize <= 0 ? defSize : pageSize;
    }
}
