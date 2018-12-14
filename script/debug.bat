CHCP 65001
echo "debug启动 testdubbo....."
java -Dtestdubbo  -Dfile.encoding=utf-8  -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n -cp "testdubbo-0.0.1-SNAPSHOT.jar;apiLib/*;lib/*" com.yejg.testdubbo.Application
