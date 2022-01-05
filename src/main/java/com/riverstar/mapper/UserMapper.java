package com.riverstar.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.riverstar.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * Author: Hardy
 * Date:   2019/4/19
 * Description:
 **/
@Mapper
//@Repository
//@DataSourceRouter(DataSourceType.SLAVE)
public interface UserMapper extends BaseMapper<User> {

}
