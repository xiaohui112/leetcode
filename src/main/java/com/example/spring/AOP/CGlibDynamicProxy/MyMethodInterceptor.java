package com.example.spring.AOP.CGlibDynamicProxy;

import AOP.jdkDynamicProxy.RealSubject;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class MyMethodInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("目标类增强前！！！");
        //注意这里的方法调用，不是用反射哦！！！
        Object object = methodProxy.invokeSuper(o, args);
        System.out.println("目标类增强后！！！");
        return object;
    }
}

 class CGlibProxy{
    public static void main(String[] args){
        //创建Enhancer对象，列斯与JDK动态代理的proxy类，下一步就是设置几个参数
        Enhancer enhancer = new Enhancer();
        //设置目标类的字节码文件
        enhancer.setSuperclass(RealSubject.class);
        //设置回调函数
        enhancer.setCallback(new MyMethodInterceptor());

        //这里create方法就是正是创建代理类
        RealSubject proxyDog = (RealSubject) enhancer.create();
        //调用代理类的request方法
        proxyDog.request();
    }
}