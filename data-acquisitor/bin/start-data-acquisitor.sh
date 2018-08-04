#!/usr/bin/env bash

if [ -f env.sh ]; then
    . env.sh
fi
mkdir -p ${ROOT}logs/ftp/

nohup java -jar ${ROOT}lib/data-acquisitor.jar \
-brokers 192.168.0.1:9092,192.168.0.2:9092,192.168.0.3:9092 \
-remoteBase /remote/path/ \
-ftpServer 192.168.0.4 \
-ftpUser user \
-ftpPassword pass >> ${ROOT}logs/ftp/data-acquisitor.out 2>&1 &
