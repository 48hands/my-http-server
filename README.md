# my-http-server

## APIs

### Create User

```
$ curl localhost:8080/users -XPOST -H "Content-Type: application/json" -d '{"name":  "yuichikoh.nagakura", "age": 22, "countryOfResidence": "Saitama"}'
{"description":"User yuichikoh.nagakura created."}
```

### Get Users

```
$ curl localhost:8080/users
```

## Dockernize

### Create Docker Image at local

```
$ sbt docker:publishLocal
$ docker images
REPOSITORY                       TAG                 IMAGE ID            CREATED              SIZE
yuichi.nagakura/my-http-server   1.0                 f6b1bcd15400        About a minute ago   665MB
```

or

```
$ sbt clean docker:stage
$ docker build -t my-webserver ./target/docker/stage/
$ docker images
REPOSITORY                       TAG                 IMAGE ID            CREATED             SIZE
my-webserver                     latest              f6b1bcd15400        8 minutes ago       665MB
```


### Push Docker Image to DockerHub

```
$ docker login
$ docker push yuichi.nagakura/my-http-server:1.0
```

### Push Docker Image to AWS ECR

```
$ $(aws ecr get-login --no-include-email --password-stdin --region ap-northeast-1)

$ sbt clean docker:stage
$ docker build -t my-webserver-ecs ./target/docker/stage/
$ docker tag my-webserver-ecs:latest XXXXXXXXXXXX.dkr.ecr.ap-northeast-1.amazonaws.com/my-webserver-ecs:latest
$ docker push XXXXXXXXXXXX.dkr.ecr.ap-northeast-1.amazonaws.com/my-webserver-ecs:latest
```
