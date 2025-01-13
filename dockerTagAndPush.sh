# dockerTagAndPush.sh
#!/bin/bash

services=(
  "server" "gateway" "user" "team-stadium" 
  "game" "payment" "ticket"
)

commit_hash=$(git rev-parse --short HEAD)

for service in "${services[@]}"
do
  if [ "$service" = "team-stadium" ]; then
    imageName="$DOCKER_HUB_NAMESPACE/spoticket-$service"
    docker build -t "$imageName:latest" "./com.spoticket.team-stadium"
  else
    imageName="$DOCKER_HUB_NAMESPACE/spoticket-$service"
    docker build -t "$imageName:latest" "./com.spoticket.$service"
  fi

  docker tag "$imageName:latest" "$imageName:$commit_hash"

  docker push "$imageName:latest"
  docker push "$imageName:$commit_hash"

  echo "$service 이미지 푸시 완료"
done

echo "모든 서비스의 이미지 빌드 및 푸시가 완료되었습니다."
