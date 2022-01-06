package com.riverstar.service.impl;

import com.riverstar.entity.User;
import com.riverstar.mapper.UserMapper;
import com.riverstar.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuailan
 * @since 2022-01-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
