## to run nginx serving files from current working directory

```
docker run --name my-nginx -p 8080:80 -v `pwd`:/usr/share/nginx/html:ro -d nginx
```

## attach to an already running container

```
sudo docker exec -i -t container-has-or-name-part /bin/bash
```


