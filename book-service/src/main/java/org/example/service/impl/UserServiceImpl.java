package org.example.service.impl;

import org.apache.catalina.User;
import org.example.mapper.UsersMapper;
import org.example.pojo.Users;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

public class UserServiceImpl implements UserService {
  @Autowired private UsersMapper usersMapper;

  @Override
  public Users queryMobileIsExist(String mobile) {
    Example userExample = new Example(Users.class);
    Example.Criteria criteria = userExample.createCriteria(); // 创建一个查询条件
    criteria.andEqualTo("mobile", mobile); // 数据库中的"mobile"需要和用户传进来的mobile相等
    Users users = usersMapper.selectOneByExample(userExample); // 开始发出查询请求
    return users;
  }

  @Override
  public Users createUser(String mobile) {
    return null;
  }
}
