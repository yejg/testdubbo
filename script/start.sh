#!/bin/sh
echo "Starting testdubbo....."
nohup java  -Dtestdubbo  -Xbootclasspath/a:lib -XX:HeapDumpPath=/tmp/testdubbo_vm_dump.hprof -Dfile.encoding=utf-8  -jar ./lib/testdubbo-0.0.1-SNAPSHOT.jar >/dev/null 2>&1 &
