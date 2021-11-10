package com.riverstar.entity;

import com.riverstar.tool.DateTool;
import lombok.Data;

/**
 * Author: Hardy
 * Date:   2019/4/18
 * Description:
 **/
@Data
public abstract class BaseEntity {
    protected Integer id;

    protected Integer time;

    protected Integer upTime;

    BaseEntity() {
        this.time = DateTool.currentTime();
        this.upTime = time;
    }

    public void saveUpTime(){
        this.upTime = DateTool.currentTime();
    }
}
