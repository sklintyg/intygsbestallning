package se.inera.intyg.intygsbestallning.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class IntygsbestallningApplication

fun main(args: Array<String>) {
	runApplication<IntygsbestallningApplication>(*args)
}
