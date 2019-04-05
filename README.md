


```
# create local docker image
sbt createImage/docker:publishLocal
```


```
# create user
curl localhost:8080/users -XPOST -H "Content-Type: application/json" -d '{"name":  "yuichikoh.nagakura", "age": 22, "countryOfResidence": "Saitama"}'
{"description":"User yuichikoh.nagakura created."}


# show users
curl localhost:8080/users
```
