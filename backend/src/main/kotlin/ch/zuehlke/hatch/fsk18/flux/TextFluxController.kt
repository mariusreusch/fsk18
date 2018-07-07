package ch.zuehlke.hatch.fsk18.flux

import ch.zuehlke.hatch.data.Person
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import org.springframework.web.bind.annotation.GetMapping
import java.time.Duration
import java.time.LocalDateTime
import java.util.stream.Stream
import org.springframework.http.codec.ServerSentEvent


@RestController
class TextFluxController(private val personService: PersonService) {



    @GetMapping(path = ["/testflux"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun list(): Flux<String> {
        val interval = Flux.interval(Duration.ofSeconds(2))

        val now: Any = LocalDateTime.now()

        val fromStream = Flux.fromStream(Stream.generate { "Hello $now" })

        return Flux.zip(interval, fromStream).map { it.t2 }
    }

    @GetMapping("/persons")
    fun streamAllPerson(): Flux<ServerSentEvent<Person>> {
        return this.personService.streamPersons()
                .map { ServerSentEvent.builder<Person>().data(it).build() }
    }

    @GetMapping("/randomNumbers")
    fun randomNumbers(): Flux<ServerSentEvent<Int>> {
        return Flux.interval(Duration.ofSeconds(1))
                .map {
                    val randomNumber = (1..10000).shuffled().last()
                    println(randomNumber)
                    ServerSentEvent.builder<Int>()
                            .id("$randomNumber")
                            .data(randomNumber)
                            .build()
                }
    }
}