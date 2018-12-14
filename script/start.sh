#!/bin/sh  -Xbootclasspath/a:lib
echo "Starting testdubbo....."
nohup java  -Dtestdubbo  -XX:HeapDumpPath=/tmp/testdubbo_vm_dump.hprof -Dfile.encoding=utf-8  -cp "testdubbo-0.0.1-SNAPSHOT.jar;apiLib/*;lib/*" com.yejg.testdubbo.Application >/dev/null 2>&1 &
