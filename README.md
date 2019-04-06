# my-http-server

## APIs

### AWS CloudWatchとECS連携用に作ったAPI

```
# Infoログ標準出力させる
curl localhost:8080/logging/info

# warnログ標準出力させる
curl localhost:8080/logging/warn

# errorログ標準出力させる(例外出力含む)
curl localhost:8080/logging/error
```

### そのほかのAPI

```
# ユーザ作る
curl localhost:8080/users -XPOST -H "Content-Type: application/json" -d '{"name":  "yuichikoh.nagakura", "age": 22, "countryOfResidence": "Saitama"}'

# ユーザ一覧を参照する
curl localhost:8080/users 
```

## Docker系の操作

### Create Docker Image at local

```
sbt docker:publishLocal
```

or

```
sbt clean docker:stage
docker build -t my-webserver ./target/docker/stage/
```


### Push Docker Image to DockerHub

```
docker login
docker push yuichi.nagakura/my-http-server:1.0
```

### Push Docker Image to AWS ECR

事前にECRに`my-webserver-ecs`という名前でリポジトリを作っておく必要がある。

```
sbt clean docker:stage
docker build -t my-webserver-ecs:latest ./target/docker/stage/

$(aws ecr get-login --no-include-email --region ap-northeast-1)
docker tag my-webserver-ecs:latest XXXXXXXXXXXX.dkr.ecr.ap-northeast-1.amazonaws.com/my-webserver-ecs:latest
docker push XXXXXXXXXXXX.dkr.ecr.ap-northeast-1.amazonaws.com/my-webserver-ecs:latest
```
