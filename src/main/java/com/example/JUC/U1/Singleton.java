package com.example.JUC.U1;

import java.io.Serializable;

/**
 * 懒汉式 单例模式，调用的时候在创建实例
 */
public final class Singleton {

    private Singleton(){}

    private static Singleton INSTANCE = null;

    public static Singleton getInstance(){
        //首次调用时创建实例，每次调用都加锁，性能低
        synchronized (Singleton.class){
            if(INSTANCE == null){
                INSTANCE = new Singleton();
            }
            return INSTANCE;
        }
    }
}
final class Single{
    private Single(){}
    private static volatile Single single = null;
    public static Single getInstance(){
        if(single==null){
            synchronized(Single.class){
                if(single==null){
                    single = new Single();
                }
            }
        }
        return single;
    }
}
final class Singleton_doublechecked{
    private Singleton_doublechecked(){}
    //volatile 保证变量的 可见性，有序性
    private static volatile Singleton_doublechecked INSTANCE = null;

    public static Singleton_doublechecked getInstance(){
        if(INSTANCE == null){//优化性能，不用每次调用方法，都去竞争锁。但是多个线程中间
            synchronized (Singleton_doublechecked.class){
                if(INSTANCE == null){
                    //字节码指令 可能存在重排序，导致先给INSTANCE赋值，再调用Singleton_doublechecked类的构造方法，这样线程之间无法保证INSTANCE的有序性。见图片 Singleton.png
                    //INSTANCE 加上volatile关键字，禁止了指令重排序。保证了并发线程的有序性。
                    INSTANCE = new Singleton_doublechecked();
                }
            }
        }
        return INSTANCE;

    }
}


/**
 * 饿汉式单例
 */

//问题1：为什么使用 final？ 答：防止子类重写方法，破坏单例
//问题2：如果实现了序列化接口，还要做什么个来防止反序列化破坏单例  答：readResovle
final class Singleton_ehan implements Serializable{
    //问题3：为什么设置为私有？是否能防止反射创建新的实例？ 答:私有构造方法，防止其他类创建单例实例。不能够防止反射机制创建实例。
    private Singleton_ehan(){}
    //问题4：这样初始化是否能够保证单例对象创建时的线程安全？
    // 答：能!静态成员变量的初始化操作是在类加载阶段完成的,在类加载阶段由JVM保证代码的线程安全性!
    private static final Singleton_ehan INSTANCE = new Singleton_ehan();
    //问题5：为什么提供静态方法而不是直接将INSTANCE设置为public，说出你知道的理由
    //使用方法可以提供更好的封装性,可以在内部实现一些懒惰的初始化操作!
    //使用方法可以对创建单例对象时有更多的控制!
    //使用方法可以提供泛型的支持!
    public static Singleton_ehan getInstance(){
        return INSTANCE;
    }

    //在反序列化的过程中,一旦发现readResolve()方法返回了一个对象,那么就使用方法中返回的这个对象
    //而不会把真正反序列化那个字节码生成的对象当成反序列化的结果!
    //保证使用的反序列化之后使用的是同一个对象,而不会生成新的对象;
    public Object readResovle(){
        return INSTANCE;
    }
}

/**
 * 静态内部类
 */
final class Singleton_neibulei{
    private Singleton_neibulei(){}
    //问题1：属于懒汉式还是饿汉式？  答：懒汉式，静态内部类懒汉式加载
    private static class LazyHolder{
        static final Singleton_neibulei INSTANCE = new Singleton_neibulei();
    }
    //问题2：在创建时是否有并发问题？答：没有，内部类加载时创建静态成员变量，保证线程安全
    public static Singleton_neibulei getInstance(){
        return LazyHolder.INSTANCE;
    }
}



/**
 * 枚举单例
 */
//问题1：枚举单例是如何限制实例个数的?
//枚举类中的对象在定义时有几个,那么将来就有几个,枚举中定义的对象相当于枚举类的静态成员变量

//问题2：枚举单例在创建时是否有并发问题?
//并没有,枚举类中定义的对象是静态成员变量,线程安全性是在类加载阶段由jvm保证的,天生的线程安全!

//问题3：枚举单例能否被反射破坏单例?
//不能.

//问题4：枚举单例能否被反序列化破坏单例?
//枚举类默认都实现了序列化口
//由于在实现序列化接口的过程中已经考虑到了被反序列化破坏单例的问题,而且已经提前做了处理
//因此枚举单例可以避免被反序列化破坏单例

//问题5：枚举单例属于懒汉式还是饿汉式?
//饿汉式,静态成员变量

//问题6：枚举单例如果希望加入一些在单例创建时的初始化逻辑,该如何做?
//可以在枚举类中添加一个构造函数,然后将初始化逻辑添加到构造函数中即可!
enum Singleton_enum{
    INSTANCE;
}
