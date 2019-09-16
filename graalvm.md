go to https://github.com/oracle/graal/releases

grab the graalvm-ce-darwin-amd64–XXXXX tar file

untar and `sudo mv` the grralvm directory to /Library/Java/JavaVirtualMachines/

use

```
/usr/libexec/java_home -V
```


to check it's installed correctly

set it to default

```
/usr/libexec/java_home -v 1.8
```

(we're assuming that you're exporting your JAVA_HOME like so:)
`export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)`



Must install this additional utility:
```
gu install native-image
```

with it you can take an executable jar (build it with assembly plugin from sbt or similar) and turn it into a native executable like so:

```
native-image --no-server theJar.jar executableName
```

Peter Perhac 2019-08-22
