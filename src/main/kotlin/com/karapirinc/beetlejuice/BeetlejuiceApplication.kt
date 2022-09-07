package com.karapirinc.beetlejuice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BeetlejuiceApplication

fun main(args: Array<String>) {
	runApplication<BeetlejuiceApplication>(*args)
}
