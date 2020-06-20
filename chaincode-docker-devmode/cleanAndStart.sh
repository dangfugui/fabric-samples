#!/bin/bash

###删除所有活跃的容器###
docker rm -f $(docker ps -aq)
###清理网络缓存###
docker network prune
###开启网络###
docker-compose -f docker-compose-simple.yaml up
