package com.babymonitor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BabyTrackerApiApplication

fun main(args: Array<String>) {
    runApplication<BabyTrackerApiApplication>(*args)
}
