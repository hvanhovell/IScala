package org.refptr.iscala

import java.io.PrintWriter

import scala.tools.nsc.{ Settings => ISettings }
import scala.tools.nsc.interpreter.CommandLine


object ScalaInterpreterFactory extends InterpreterFactory {

    def apply(config: Options#Config): IScalaInterpreter = {
        ScalaInterpreterFactory(config.completeClasspath, config.args, config.embed)
    }

    def apply(additionalClasspath: String, args: List[String] = Nil, embed: Boolean = false): IScalaInterpreter = {
        // Setup Settings via CommandLine
        val settings = new CommandLine(args, println).settings

        // Setup the classpath
        val totalClassPath = settings.classpath
        totalClassPath.value = ClassPath.join(totalClassPath.value, additionalClasspath)

        // Embed (???) the interpreter
        if (embed) {
            settings.embeddedDefaults[IScalaInterpreter]
        }

        // Create the backend creation function.
        val backendInit = (settings:ISettings, printer:PrintWriter) => {
            new ScalaIMainBackend(settings, printer)
        }

        // Create the interpreter
        new IScalaInterpreter(settings, backendInit)
    }

    override def toString = "<SparkInterpreterFactory>"
}