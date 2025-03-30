package com.nyang8ja.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = ["com.nyang8ja.api"])
class NyangApiApplication

fun main(args: Array<String>) {
	runApplication<NyangApiApplication>(*args)
}
