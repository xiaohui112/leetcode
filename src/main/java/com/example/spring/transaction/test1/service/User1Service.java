package com.example.spring.transaction.test1.service;

import com.example.spring.transaction.test1.domain.User1;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class User1Service {

    @Transactional(propagation = Propagation.REQUIRED)
    public void addRequired(User1 user){

    }
}
