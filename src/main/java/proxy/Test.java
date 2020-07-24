package proxy;

import java.lang.reflect.Proxy;

public class Test {
    public static void main(String[] args) {
       //创建代理对象
        StudentDao studentDao=new StudentDaoImp();
        StudentDao proxy = (StudentDao) Proxy.newProxyInstance(studentDao.getClass().getClassLoader(), studentDao.getClass().getInterfaces(), new MyProxy(studentDao));
        proxy.add("test");
    }
}
