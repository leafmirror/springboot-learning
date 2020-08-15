package com.tracenull.mapper;

import com.tracenull.common.MyMapper;
import com.tracenull.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

//@Repository
//@Mapper
public interface UserMapper extends MyMapper<User> {
}
