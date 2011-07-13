import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info) with AkkaProject {
  val junit = "junit" % "junit" % "4.7" % "test"
  val mockito = "org.mockito" % "mockito-all" % "1.8.5" % "test"
  val specs = "org.scala-tools.testing" % "specs_2.8.1" % "1.6.7" % "test"
  val configgy = "net.lag" % "configgy" % "2.0.0" intransitive()
  val runSimpleServer = task { args => { runTask(Some("akka.silvertip.test.SimpleServer"), runClasspath, args) }.dependsOn(testCompile) }
  val runSimpleClient = task { args => { runTask(Some("akka.silvertip.test.SimpleClient"), runClasspath, args) }.dependsOn(testCompile) }
  lazy val publishTo = Resolver.file("GitHub Pages", new java.io.File("../akka-silvertip-gh-pages/maven/"))
  override def managedStyle = ManagedStyle.Maven
  override def compileOptions = super.compileOptions ++ Seq(Unchecked)
}
