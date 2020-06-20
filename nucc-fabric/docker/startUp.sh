#!/bin/bash


docker-compose -f docker-compose-order.yaml up -d
docker-compose -f docker-compose-org1-peer0.yaml up -d
docker-compose -f docker-compose-org1-peer1.yaml up -d
docker-compose -f docker-compose-org2-peer0.yaml up -d
docker-compose -f docker-compose-org2-peer1.yaml up -d
docker-compose -f docker-compose-org3-peer0.yaml up -d
docker-compose -f docker-compose-org3-peer1.yaml up -d

docker-compose -f docker-compose-cli.yaml up -d
