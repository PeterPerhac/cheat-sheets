
** read a HOCON file and write it out to a file in JSON-format:

import com.typesafe.config._
val c = ConfigFactory.parseFile(new java.io.File("path to HOCON file"))
java.nio.file.Files.write(java.nio.file.Paths.get("/Users/peterperhac/somefile"), c.root().render( ConfigRenderOptions.concise() ).getBytes(), java.nio.file.StandardOpenOption.CREATE)


** something else


