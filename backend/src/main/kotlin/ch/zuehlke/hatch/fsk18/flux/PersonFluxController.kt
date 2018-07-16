package ch.zuehlke.hatch.fsk18.flux

import ch.zuehlke.hatch.data.Person
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration

@RestController
class PersonFluxController(private val personService: PersonService) {

    @GetMapping("/persons")
    fun streamAllPerson(): Flux<ServerSentEvent<Person>> {
        return this.personService.streamPersons()
                .map { ServerSentEvent.builder<Person>().data(it).build() }
    }

    @GetMapping("/randomNumbers")
    fun randomNumbers(@RequestParam divider: Int): Flux<ServerSentEvent<Int>> {
        return Flux.interval(Duration.ofSeconds(1))
                .map {
                    val randomNumber = (1..10000).shuffled().last { it % divider == 0 }

                    ServerSentEvent.builder<Int>()
                            .id("$randomNumber")
                            .data(randomNumber)
                            .build()
                }
    }
}

