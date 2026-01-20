#!/bin/bash
set -e

# full dump from rds import
if [ ! -f /home/ubuntu/.db_imported ]; then
  echo ">>> importing dump into mariadb (one-time)"
  docker exec -i dobby-mariadb mariadb -u root -p"$DB_ROOT_PASSWORD" < /home/ubuntu/dobby_prod_dump.sql
  touch /home/ubuntu/.db_imported
  echo ">>> dump import done"
fi
# --- end DB bootstrap ---

IS_GREEN_EXIST=$(docker ps | grep green)

# green up
if [[ -z "$IS_GREEN_EXIST" ]]; then
  echo "### BLUE -> GREEN ####"
  echo ">>> pull green image"
  docker compose -f /home/ubuntu/docker-compose-prod.yml pull green
  echo ">>> up green container"
  docker compose -f /home/ubuntu/docker-compose-prod.yml up -d green
  while [[ 1 -eq 1 ]]; do
    echo ">>> green health check ..."
    sleep 3
    REQUEST=$(curl -s http://127.0.0.1:8082)
    if [[ -n "$REQUEST" ]]; then
      echo ">>> health check success !"
      break
    fi
  done
  sleep 3
  echo ">>> reload nginx"
  sudo cp /etc/nginx/conf.d/green-url.inc /etc/nginx/conf.d/service-url.inc
  sudo nginx -s reload
  echo ">>> down blue container"
  docker compose -f /home/ubuntu/docker-compose-prod.yml stop blue

# blue up
else
  echo "### GREEN -> BLUE ###"
  echo ">>> pull blue image"
  docker compose -f /home/ubuntu/docker-compose-prod.yml pull blue
  echo ">>> up blue container"
  docker compose -f /home/ubuntu/docker-compose-prod.yml up -d blue
  while [[ 1 -eq 1 ]]; do
    echo ">>> blue health check ..."
    sleep 3
    REQUEST=$(curl -s http://127.0.0.1:8081)
    if [[ -n "$REQUEST" ]]; then
      echo ">>> health check success !"
      break
    fi
  done
  sleep 3
  echo ">>> reload nginx"
  sudo cp /etc/nginx/conf.d/blue-url.inc /etc/nginx/conf.d/service-url.inc
  sudo nginx -s reload
  echo ">>> down green container"
  docker compose -f /home/ubuntu/docker-compose-prod.yml stop green
fi
