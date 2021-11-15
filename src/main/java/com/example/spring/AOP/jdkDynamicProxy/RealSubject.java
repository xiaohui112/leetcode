package com.example.spring.AOP.jdkDynamicProxy;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 使用JDK动态代理，委托类
 */
public class RealSubject implements Subject{
    @Override
    public void request() {
        System.out.println("卖房");
    }
}

class ProxyFactory{
    private Object target;//维护一个目标对象

    public ProxyFactory(Object target) {
        this.target = target;
    }

    //为目标对象生成代理对象
    public Object getProxyInstance(){
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("计算开始时间");
                // 执行目标对象方法
                method.invoke(target, args);
                System.out.println("计算结束时间");
                return null;
            }
        });
    }



    public static void main(String[] args) {
        RealSubject realSubject = new RealSubject();
        System.out.println(realSubject.getClass());
        Subject subject = (Subject) new ProxyFactory(realSubject).getProxyInstance();
        System.out.println(subject.getClass());
        subject.request();
    }
}