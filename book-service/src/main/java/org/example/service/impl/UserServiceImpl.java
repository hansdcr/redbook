package org.example.service.impl;

import org.apache.catalina.User;
import org.example.enums.Sex;
import org.example.enums.YesOrNo;
import org.example.mapper.UsersMapper;
import org.example.pojo.Users;
import org.example.service.UserService;
import org.example.utils.DateUtil;
import org.example.utils.DesensitizationUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
  @Autowired private UsersMapper usersMapper;
  @Autowired private Sid sid;
  // 写死的一个用户默认头像
  private static final String USER_FACE1 =
      "http://122.152.205.72:88/group1/M00/00/05/CpoxxF6ZUySASMbOAABBAXhjY0Y649.png";

  @Override
  public Users queryMobileIsExist(String mobile) {
    Example userExample = new Example(Users.class);
    Example.Criteria criteria = userExample.createCriteria(); // 创建一个查询条件
    criteria.andEqualTo("mobile", mobile); // 数据库中的"mobile"需要和用户传进来的mobile相等
    Users users = usersMapper.selectOneByExample(userExample); // 开始发出查询请求
    return users;
  }

  @Transactional
  @Override
  public Users createUser(String mobile) {
    // 获得全局唯一的ID
    String userId = sid.nextShort();
    Users user = new Users();
    user.setId(userId);
    user.setMobile(mobile);
    user.setNickname("用户：" + DesensitizationUtil.commonDisplay(mobile));
    user.setImoocNum("用户：" + DesensitizationUtil.commonDisplay(mobile));
    user.setFace(USER_FACE1);

    user.setBirthday(DateUtil.stringToDate("1900-01-01"));
    user.setSex(Sex.secret.type);

    user.setCountry("中国");
    user.setProvince("");
    user.setCity("");
    user.setDistrict("");
    user.setDescription("这家伙很懒，什么都没留下~");
    user.setCanImoocNumBeUpdated(YesOrNo.YES.type);

    user.setCreatedTime(new Date());
    user.setUpdatedTime(new Date());

    usersMapper.insert(user);
    return user;
  }
}
