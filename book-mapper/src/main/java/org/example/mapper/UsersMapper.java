package org.example.mapper;

import org.example.my.mapper.MyMapper;
import org.example.pojo.Users;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersMapper extends MyMapper<Users> {}
