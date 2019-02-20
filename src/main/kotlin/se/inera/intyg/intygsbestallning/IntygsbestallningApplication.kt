package se.inera.intyg.intygsbestallning

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class IntygsbestallningApplication

fun main(args: Array<String>) {
	runApplication<IntygsbestallningApplication>(*args)
}
