## testdubbo

dubbo接口测试工具  


### 结构
- apiLib 此目录存放dubbo-api的jar，如果dubbo-api自身还依赖其他jar，也一并放进来
- frontjs 此目录是前端页面的源码
- script 启动的脚本(bat、sh)
- src java代码目录


### 原理
其实原理很简单，主要就2点：  

1.  解析jar，从中找到配置的目录的类和方法
2.  利用dubbo的 [泛化调用](http://dubbo.apache.org/zh-cn/docs/user/demos/generic-service.html) dubboInvokeService.invokeDubbo 调用对应的dubbo接口


### 配置
见src\main\resources\application.properties，需要注意dubbo.interface，这个配置是指定哪些目录的dubbo接口。
> eg: dubbo.interface=com.dubbo.user.api, com.dubbo.businflow.api


### 运行和打包
- 运行源码
   需要将dubbo-api的jar方法apiLib目录，并添加到classpath中，然后run application就ok了

- 打包
执行【mvn clean package】，然后在target/testdubbo目录就有可执行的东西，执行使用start.bat即可

### 关于bat/sh运行
项目里面提供的是java -cp的方式，即
> java -cp "testdubbo-0.0.1-SNAPSHOT.jar;apiLib/*;lib/*" com.yejg.testdubbo.Application


如果想要用java -jar的方式，则需要做如下调整：
1.  修改pom。xml的maven-jar-plugin部分配置，改成
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <configuration>
        <archive>
            <manifest>
                <addClasspath>true</addClasspath>
                <classpathPrefix>lib</classpathPrefix>
                <mainClass>com.yejg.testdubbo.Application</mainClass>
            </manifest>
        </archive>
    </configuration>
</plugin>
```
2.  启动命令改成：
> java -Xbootclasspath/a:apiLib/api1.jar;api2.jar -jar testdubbo-0.0.1-SNAPSHOT.jar

linux场景下，jar之间用 : 分隔


### 感谢
感谢同事(Maple)提供的初版本代码
