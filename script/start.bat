CHCP 65001
echo "启动 testdubbo....."
java -Dtestdubbo  -Dfile.encoding=utf-8  -Djava.ext.dirs=lib:$JAVA_HOME/jre/lib/ext -jar ./lib/testdubbo-0.0.1-SNAPSHOT.jar