package com.babymonitor.baby

import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/babies")
class BabyController(private val repository: BabyRepository) {

    @GetMapping
    fun all(): Flux<Baby> = repository.findAll()

    @PostMapping
    fun create(@RequestBody baby: Baby): Mono<Baby> = repository.save(baby)
}
