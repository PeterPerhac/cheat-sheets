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


Startup Script `idea` stopped working
  - open intelliJ, and from the main menu select Tools -> Create Command-Line Launcher
  This will overwrite previous idea script with a new one that hopefully works

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


### sbt run vs sbt start with system properties

problem: 
1) sbt run -Dhttp.port=8080
2) sbt runProd -Dhttp.port=8080

1 works but 2 does not

system properties are not taking effect when a new jvm process is started by sbt in case 2.
System properties "-Dxxx" are only effective for the jvm in which SBT is running and the play sbt plugin's run task runs the play application directly in that same jvm, so the system properties are effective. In case 2, the plugin will launch a new jvm and run the application in production mode. System properties need to be set from within the SBT process and that is achieved by:

set javaOptions in ThisBuild ++= Seq("-Dhttp.port=8080")

or by exporting JAVA_OPTS variable into your environment prior to launching SBT. SBT will pick up the JAVA_OPTS value when kicking off new jvm.

### upgrading java
upgrading java is done via brew, but classic brew update / brew upgrade command will not suffice. Java is installed from the java cask. So the command is:

```
brew cask upgrade
```

### dependency tree

it's as simple as issuing dependencyTree command
provided you have added this to your plugins:

```
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.0")
```


### Tab completion when running tests

Tab completion is provided for test names based on the results of the last test:compile. This means that a new sources aren’t available for tab completion until they are compiled and deleted sources won’t be removed from tab completion until a recompile. A new test source can still be manually written out and run using testOnly.

#Unix/Linux

Not sure what the root password is? Perhaps there's none. use `sudo passwd` to create a root password

have the noclobber setting on and you really want to overwrite a file? use `>|`

If the redirection operator is ‘>’, and the noclobber option to the set builtin has been enabled, the redirection will fail if the file whose name results from the expansion of word exists and is a regular file. If the redirection operator is ‘>|’, or the redirection operator is ‘>’ and the noclobber option is not enabled, the redirection is attempted even if the file named by word exists.
3.6.2 redirecting output section of bash manual https://www.gnu.org/software/bash/manual/bashref.html#Redirections


#iPhone

Trouble getting into GMail app with message: "It looks like your device isn't up to date with the latest security policies. Please try again late"

Remove the MDM profile (settings > general > device management) and then try and sign into the service (GMail) again it will prompt to reinstall the MDM...




# specs2
  When coverage won't run because of
  InvocationTargetException for main in org.specs2.NotifierRunner: null

  the problem is probably incorrect nesting of should { in { in {

    make sure there is only one should at the top and one in at the bottom of the hierarchy
    in between nesting should be done with >>



#Terminal window Last Login message

```
touch .hushlogin
```

to create a file in your home directory, that will stop the Last Login message


# CATS (1.0.1)
## no monad instance for Future
def that produces an instance of Future requires an instance of ExecutionContext in implicit scope. So remember to not only import FutureInstances but also to import the implicit ExecutionContext.

## mapN doesn't seem to like to work
not sure if this is a problem on other data types, but when trying to mapN Validated the required Semigroupal instance of Validated isn't in scope, for similar reasons as above, the def that produces the validated semigroup instance requires a semigroup instance for bot types A and B, so if it's Validated[List[String], Foo] then an instance for Foo and an instance for List is required in scope. So try importing the instances for whichever container is on the left of Validated.



#various other

## thefuck command not suggesting correct git push command

git push -f doesn't seem to work with thefuck command too well. good new is, it's not needed. use the fuck command only in cases when pushing a new branch that does not exist remotely yet. Then the force switch is not required, and the fuck command works as expected.

> fuck
git push -f -f [enter/↑/↓/ctrl+c]



GIT DETACHED HEAD
============

colin had diverging branches, so he did a
  git reset --hard origin/branch
this eneded up in a detached head
fixed it like so:
  git reset --hard HEAD^^ (go back to the place your branch diverged, --hard if you don't care to lose your stuff)
  git pull



403 Forbidden from NGINX
=============
I've found that two files with apparently identical details (permissions, ownership the same) can give different result when requested over HTTP / NGINX. Looking into this I found that running the `stat` command on the differing files shows they have different SELinux security context. SELinux is security-enhanced linux and these contexts are managed through security policy management tool /usr/sbin/semanage.

read more on how to make NGINX work with SELinux: https://www.nginx.com/blog/using-nginx-plus-with-selinux/

long story short, to get it to work, I did this:

`semanage permissive -a httpd_t`




#NPM
global install of package with npm can't be run
make sure to sudo it

  `sudo npm install -g foo`



Vim installation - when struggling to a get vim working on a new laptop, have you installed plugins? Use the :PlugUpdate and :PlugInstall commands


when the external monitor is switching off after unplugging from AC power, this command sorts it out:

   `sudo pmset disablesleep 1`


