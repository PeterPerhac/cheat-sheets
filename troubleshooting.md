#Troubleshooting
Don't ever google the same problem twice. Once you've fixed it, make a note here.

## Mac-related
### Stop ssh client prompting for password
```bash
vim ~/.ssh/config
```
add these lines:
```
Host *
   UseKeychain yes
```

## IntelliJ

IntelliJ / SBT tip: if IntelliJ isn't finding the source code for libraries in your sbt project, try doing `sbt update-classifiers` followed by File -> Synchronize in IntelliJ


## Scala / SBT
deprecation / feature warnings - this is how to find out what they are:

scalacOptions need to be set in the sbt build. to do this without editing build file, just do this from terminal:

```bash
sbt
set scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation")
compile
```


