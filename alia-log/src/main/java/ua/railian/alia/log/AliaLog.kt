package ua.railian.alia.logger

import ua.railian.alia.core.Alia

object AliaLogger

val Alia.LOGGER get() = AliaLogger

val log = Alia.LOGGER.config { +tag("@@@") }

fun main() {
    Alia.LOGGER config { +tag("@@@") } log debug("Hello, world!")
    log log d("How are you?")
    log.d("gfhfg")
    log.debug("")
}

fun debug(message: String): AliaLog = TODO()
fun d(message: String): AliaLog = TODO()
fun AliaLogger.debug(message: String): AliaLog = TODO()
fun AliaLogger.d(message: String): AliaLog = TODO()

class AliaLog

private infix fun AliaLogger.log(debug: AliaLog): AliaLogger = TODO("not implemented")

private infix fun AliaLogger.with(builder: AliaLoggerBuilder.() -> Unit): AliaLogger = TODO()
private infix fun AliaLogger.config(builder: AliaLoggerBuilder.() -> Unit): AliaLogger = TODO()

class AliaLoggerBuilder : Alia.Builder<AliaLogger>(Alia.Core(), { AliaLogger })

infix fun AliaLoggerBuilder.tag(name: String): Alia.Element = TODO()
