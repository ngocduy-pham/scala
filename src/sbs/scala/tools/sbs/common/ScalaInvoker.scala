/*
 * ScalaInvoker
 * 
 * Version
 * 
 * Created October 2nd, 2011
 * 
 * Created by ND P
 */

package scala.tools.sbs
package common

import java.net.URL

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.sys.process.Process
import scala.sys.process.ProcessIO
import scala.tools.nsc.util.ClassPath
import scala.tools.sbs.benchmark.Benchmark
import scala.tools.sbs.io.Log

/** An implement of {@link JVMInvoker}.
 */
class ScalaInvoker(log: Log, config: Config) extends JVMInvoker {

  /** `java` or `./jre/bin/java`, etc...
   */
  private val java = Seq(config.javacmd, "-server")

  /** `-cp <scala-library.jar, scala-compiler.jar> -Dscala=<scala-home> scala.tools.nsc.MainGenericRunner`
   */
  private def asScala(classpathURLs: List[URL]) =
    Seq(
      "-cp",
      ClassPath.fromURLs(classpathURLs ++ List(config.scalaLibraryJar.toURL, config.scalaCompilerJar.toURL): _*)) ++
      Seq(config.javaProp, "scala.tools.nsc.MainGenericRunner")

  /** `-cp <classpath from config; classpath from benchmark>`
   */
  private def asScalaClasspath(classpathURLs: List[URL]) =
    Seq("-cp", ClassPath.fromURLs(classpathURLs: _*))

  /** `-cp <classpath from config; classpath from benchmark> Runner`
   */
  private def asHarness(harness: ObjectHarness, benchmark: Benchmark, classpathURLs: List[URL]) =
    asScalaClasspath(classpathURLs) ++ Seq(harness.getClass.getName replace ("$", ""))

  /** `-cp <classpath from config; classpath from benchmark> Benchmark`
   */
  private def asBenchmark(benchmark: Benchmark, classpathURLs: List[URL]) =
    asScalaClasspath(classpathURLs) ++ Seq(benchmark.name)

  /** `-cp <scala-library.jar, scala-compiler.jar> -Dscala.home=<scala-home> scala.tools.nsc.MainGenericRunner
   *  -cp <classpath from config; classpath from benchmark> Benchmark benchmark.arguments`
   */
  def asJavaArgument(benchmark: Benchmark, classpathURLs: List[URL]) =
    asScala(classpathURLs) ++ asBenchmark(benchmark, classpathURLs) ++ benchmark.arguments

  /** `-cp <scala-library.jar, scala-compiler.jar> -Dscala.home=<scala-home> scala.tools.nsc.MainGenericRunner
   *  -cp <classpath from config; classpath from benchmark> Runner benchmark.toXML config.args`
   *  Result must be a string on one line and starts with `<`.
   */
  def asJavaArgument(harness: ObjectHarness, benchmark: Benchmark, classpathURLs: List[URL]) =
    asScala(classpathURLs) ++
      asHarness(harness, benchmark, classpathURLs) ++
      Seq(scala.xml.Utility.trim(benchmark.toXML).toString) ++
      config.args

  def command(harness: ObjectHarness, benchmark: Benchmark, classpathURLs: List[URL]) =
    java ++ asJavaArgument(harness, benchmark, classpathURLs)

  def command(benchmark: Benchmark, classpathURLs: List[URL]) =
    java ++ asJavaArgument(benchmark, classpathURLs)

  def invoke[R](command: Seq[String], convert: String => R, timeout: Int): (ArrayBuffer[R], ArrayBuffer[String]) = {
    val result = ArrayBuffer[R]()
    val error = ArrayBuffer[String]()
    val processBuilder = Process(command)

    log.debug("Invoked command: " + (command mkString " "))

    val processIO = new ProcessIO(
      _ => (),
      stdout => Source.fromInputStream(stdout).getLines foreach (
        line => try result append convert(line) catch { case _ => log(line) }),
      stderr => Source.fromInputStream(stderr).getLines foreach (error append _))

    var success = -1
    val processStarter = new Thread {

      override def run = {
        var process: Process = null
        try {
          process = processBuilder.run(processIO)
          success = process.exitValue
        }
        catch {
          case e: InterruptedException =>
            log.error("Timeout")
            process.destroy
        }
      }

    }
    val timer = new Thread {

      override def run = try {
        Thread sleep (timeout + 3000)
        processStarter.interrupt()
      }
      catch {
        case e: InterruptedException =>
          log.debug("Not timeout")
      }

    }

    processStarter.start()
    if (timeout > 0) timer.start()
    processStarter.join
    timer.interrupt()

    log.debug("Sub-process exit value: " + success)

    (result, error)
  }

}
