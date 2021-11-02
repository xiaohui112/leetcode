package com.example.JUC.U8;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.StringJoiner;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j(topic = ("c.test9"))
public class Test9_数据库缓存一致性 {
    public static void main(String[] args){

        /**
         * 多次读取，频繁调用数据库，耗时。可以增加缓存，
         * 更新数据库是清空缓存，保证数据一致性
         */

//        GenericDao dao = new GenericDao();
        GenericDaoCached dao = new GenericDaoCached();
        log.debug("============>  查询");
        String sql = "select * from tb_emp where empno = ?";
        int empno = 7369;
        Emp emp = dao.queryOne(Emp.class,sql,empno);
        log.debug("第一次查询，emp = {}",emp.toString());
        emp = dao.queryOne(Emp.class,sql,empno);
        log.debug("第二次查询，emp = {}",emp.toString());
        emp = dao.queryOne(Emp.class,sql,empno);
        log.debug("第三次查询，emp = {}",emp.toString());
        
        log.debug("============> 更新");
        String updateSql = "update tb_emp set sal = ? where empno = ?";
        dao.update(updateSql,1000,empno);

        emp = dao.queryOne(Emp.class,sql,empno);
        log.debug("更新后查询，emp = {}",emp.toString());
    }
}
class GenericDaoCached extends GenericDao{
    private GenericDao dao = new GenericDao();
    private HashMap<Integer,Emp> cacheMap = new HashMap<>();
    private ReentrantReadWriteLock rw = new ReentrantReadWriteLock();

    @Override
    public Emp queryOne(Class<Emp> empClass, String sql, Integer empno) {
        rw.readLock().lock();
        try{
            //先查缓存
            Emp value = cacheMap.get(empno);
            if(value != null){
                return value;
            }
        }finally {
            rw.readLock().unlock();
        }
        rw.writeLock().lock();
        try{
            //双重检查
            Emp value = cacheMap.get(empno);
            if(value == null){
                //没有在查数据库
                value = dao.queryOne(empClass, sql, empno);
                cacheMap.put(empno,value);
            }
            return value;
        }finally {
            rw.writeLock().unlock();
        }
    }

    @Override
    public void update(String updateSql, Integer sal, Integer empno) {
        rw.writeLock().lock();
        try{
            //先更新数据库
            dao.update(updateSql, sal, empno);
            //在清空缓存
            cacheMap.clear();
        }finally {
            rw.writeLock().unlock();
        }
    }
}


@Slf4j(topic = ("c.GenericDao"))
class GenericDao{

    final Emp emp = new Emp(6379,0);


    public Emp queryOne(Class<Emp> empClass, String sql, Integer empno) {
        log.debug("sql = {}, param = {}",sql,empno);
        return emp;
    }


    public void update(String updateSql, Integer sal, Integer empno) {
        log.debug("sql = {},sal:{}, empno:{}",updateSql,sal,empno);
        emp.setSal(sal);
    }
}
class Emp{
    private Integer empno;
    private Integer sal;

    public Emp(Integer empno, Integer sal) {
        this.empno = empno;
        this.sal = sal;
    }

    public void setSal(Integer sal) {
        this.sal = sal;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Emp.class.getSimpleName() + "[", "]")
                .add("empno=" + empno)
                .add("sal=" + sal)
                .toString();
    }
}