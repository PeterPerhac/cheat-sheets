Setup Basic Auth on Oracle Linux box

```bash
sudo yum install httpd-tools
sudo htpasswd -c /etc/nginx/.htpasswd pepek
sudo vim /etc/nginx/nginx.conf
```

add these two lines to the desired "location" section:
```
  auth_basic "Only The Chosen One May Enter This Realm";
  auth_basic_user_file /etc/nginx/.htpasswd;
```

Restart nginx and all is done

```
sudo nginx -s reload
```
