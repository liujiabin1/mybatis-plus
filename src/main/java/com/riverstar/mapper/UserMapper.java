package com.riverstar.mapper;

import com.riverstar.component.database.DataSourceRouter;
import com.riverstar.component.database.DataSourceType;
import com.riverstar.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Author: Hardy
 * Date:   2019/4/19
 * Description:
 **/
@Mapper
@Repository
@DataSourceRouter(DataSourceType.SLAVE)
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where age>=#{age}")
    User findByAgeGte(int age);
}
