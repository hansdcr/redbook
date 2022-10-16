package org.example.service;

import org.example.pojo.Users;

public interface UserService {
  /**
   * 判断用户是否存在，存在返回用户信息
   *
   * @param mobile
   * @return
   */
  public Users queryMobileIsExist(String mobile);
}
