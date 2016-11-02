# Undercore Essential Scala course

## day 1 (1st November 2016)

to turn warnings into errors. make the code not compile if not proper.

```scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings"),```


sortWith
    takes two parameters and returns a Boolean
        this boolean is TRUE if the two parameters are in correct order already
        or FALSE, if the are not in the correct order

