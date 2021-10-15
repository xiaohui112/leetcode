package com.example.JUC;

import lombok.extern.slf4j.Slf4j;

import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

@Slf4j
public class Test40_AtomicReferenceFieldUpdater {
  
    
    public static void main(String[] args){
        Student stu = new Student();
        AtomicReferenceFieldUpdater updater = AtomicReferenceFieldUpdater.newUpdater(Student.class,String.class,"name");

        System.out.println(updater.compareAndSet(stu,null,"张硕"));
        System.out.println(stu);
    }
}

class Student{
    volatile String name;

    @Override
    public String toString() {
        return new StringJoiner(", ", Student.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .toString();
    }
}