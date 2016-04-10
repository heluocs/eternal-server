package xyz.goome.eternal.dbserver;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by matrix on 16/4/9.
 */
public class DBMain {

    public static void main(String[] args) {
//        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-*.xml");
        String[] config = {"classpath:spring-context.xml", "classpath:spring-dao.xml"};
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(config);
    }

}
