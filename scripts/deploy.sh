#!/usr/bin/env bash



REPOSITORY=/home/ubuntu/finalproject12Be
cd $REPOSITORY

APP_NAME=finalproject12Be
JAR_NAME=finalproject12Be-0.0.1-SNAPSHOT.jar
JAR_PATH=$REPOSITORY/build/libs/finalproject12Be-0.0.1-SNAPSHOT.jar

echo "> 현재 구동중인 애플리케이션 pid 확인"
CURRENT_PID=$(pgrep -fl $APP_NAME | grep java | awk '{print $1}')

if [ -z "$CURRENT_PID" ];
then
  echo "> 종료할 애플리케이션이 없습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 애플리케이션 배포"

JAR_PATH=$REPOSITORY/build/libs/finalproject12Be-0.0.1-SNAPSHOT.jar

echo "> JAR NAME: $JAR_PATH"

echo "> $JAR_PATH 에 실행권한 추가"

chmod +x $JAR_PATH

echo "> $JAR_PATH 실행"

nohup java -jar $JAR_PATH >> $REPOSITORY/nohup.out 2>&1 &