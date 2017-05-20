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

Preview stops working
open htop
F6 sort by command
find Preview.app
mark it with Space
F9 and Enter to send it the kill signal (mark as many Preview as are running)

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

When "should is not member of String" messages start appearing in _intellij_, one way to get around it is to edit the scalatest dependency version and re-import the sbt project. This fixes intellij's confusion.


coverage dropped for no apparent reason. string arguments for Logger not evaluated. this is because the log levels have changed in logback.xml and now the logger no longer evaluates the strings, they are => call by name


when more memory is needed for running `scala` command, just use the `-J-Xmx2g` switch.
this configures the `JAVA_OPTS` environment variable

