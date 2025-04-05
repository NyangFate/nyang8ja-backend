#!/bin/bash

git submodule update --remote
sh ./nyang8ja-api/build.sh
docker-compose up -d --build