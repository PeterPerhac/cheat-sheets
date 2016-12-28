#Troubleshooting
Don't ever google the same problem twice. Once you've fixed it, make a note here.

##Mac related
### Stop ssh client prompting for password
```bash
vim ~/.ssh/config
```
add these lines:
```
Host *
   UseKeychain yes
```


