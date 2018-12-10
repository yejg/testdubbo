package cn.cairh.testdubbo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringValueResolver;

@SpringBootApplication
public class Application  {

    Logger logger = LoggerFactory.getLogger(getClass());

    private StringValueResolver stringValueResolver;

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(Application.class, args);
    }
}
