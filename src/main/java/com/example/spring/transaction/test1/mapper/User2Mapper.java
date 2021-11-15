package com.example.spring.transaction.test1.mapper;

import com.example.spring.transaction.test1.domain.User1;

public interface User2Mapper {
    int insert(User1 record);
    User1 selectBuPrimaryKey(Integer id);
}
