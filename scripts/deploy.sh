#!/bin/bash

IS_GREEN_EXIST=$(docker ps | grep green)
DEFAULT_CONF="/etc/nginx/nginx.conf"

# blue가 실행 중이면 green을 up합니다.
if [ -z $IS_GREEN_EXIST ]; then
  echo "### BLUE => GREEN ####"
  echo ">>> green image를 pull합니다."
  docker-compose pull green
  echo ">>> green container를 up합니다."
  docker-compose up -d green
  sleep 3
  echo ">>> nginx를 다시 실행 합니다."
  sudo cp /etc/nginx/nginx.green.conf /etc/nginx/nginx.conf
  sudo nginx -s reload
  echo ">>> blue container를 down합니다."
  docker-compose stop blue

# green이 실행 중이면 blue를 up합니다.
else
  echo "### GREEN => BLUE ###"
  echo ">>> blue image를 pull합니다."
  docker-compose pull blue
  echo ">>> blue container up합니다."
  docker-compose up -d blue
  sleep 3
  echo ">>> nginx를 다시 실행 합니다."
  sudo cp /etc/nginx/nginx.blue.conf /etc/nginx/nginx.conf
  sudo nginx -s reload
  echo ">>> green container를 down합니다."
  docker-compose stop green
  fi