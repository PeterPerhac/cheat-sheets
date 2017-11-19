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

### quotes problems
make sure the input method - keyboard - selected is not an "international" one. that one is waiting for second keypress to bind two keys into a single glyph.

### preview problems

Preview stops working
open htop
F6 sort by command
find Preview.app
mark it with Space
F9 and Enter to send it the kill signal (mark as many Preview as are running)

## IntelliJ

IntelliJ / SBT tip: if IntelliJ isn't finding the source code for libraries in your sbt project, try doing `sbt update-classifiers` followed by File -> Synchronize in IntelliJ

"Cannot resolve symbol" compilation error - IntelliJ unable to resolve things like "Option".
Invalidate caches, shut down IntelliJ, delete .idea folder, start IntelliJ and re-import project

## Scala / SBT
deprecation / feature warnings - this is how to find out what they are:

scalacOptions need to be set in the sbt build. to do this without editing build file, just do this from terminal:

```bash
sbt
set scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation", "-feature")
compile
```

When "should is not member of String" messages start appearing in _intellij_, one way to get around it is to edit the scalatest dependency version and re-import the sbt project. This fixes intellij's confusion.


coverage dropped for no apparent reason. string arguments for Logger not evaluated. this is because the log levels have changed in logback.xml and now the logger no longer evaluates the strings, they are => call by name


when more memory is needed for running `scala` command, just use the `-J-Xmx2g` switch.
this configures the `JAVA_OPTS` environment variable

### Tab completion when running tests

Tab completion is provided for test names based on the results of the last test:compile. This means that a new sources aren’t available for tab completion until they are compiled and deleted sources won’t be removed from tab completion until a recompile. A new test source can still be manually written out and run using testOnly.

#Unix/Linux

Not sure what the root password is? Perhaps there's none. use `sudo passwd` to create a root password


#iPhone

Trouble getting into GMail app with message: "It looks like your device isn't up to date with the latest security policies. Please try again late"

Remove the MDM profile (settings > general > device management) and then try and sign into the service (GMail) again it will prompt to reinstall the MDM...




# specs2
  When coverage won't run because of
  InvocationTargetException for main in org.specs2.NotifierRunner: null

  the problem is probably incorrect nesting of should { in { in {

    make sure there is only one should at the top and one in at the bottom of the hierarchy
    in between nesting should be done with >>



