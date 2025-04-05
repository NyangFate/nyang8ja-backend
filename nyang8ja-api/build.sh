#!/bin/bash

set -e  # 에러 나면 스크립트 종료

echo "🔨 Building API Server JAR..."
cd nyang8ja-api
./gradlew copyConfig
./gradlew clean build -x test

echo "✅ Api Build Success!"

cd ..docker-compose up --build
