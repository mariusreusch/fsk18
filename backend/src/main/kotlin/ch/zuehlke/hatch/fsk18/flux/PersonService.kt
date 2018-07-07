package ch.zuehlke.hatch.fsk18.flux

import ch.zuehlke.hatch.data.Person
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Duration

@Service
class PersonService {

    private val firstNames = listOf("Franz", "Gerd", "Paul", "Sepp", "Karl-Heinz", "Georg")
    private val lastNames = listOf("Beckenbauer", "Müller", "Meier", "Maier", "", "Georg")

    fun streamPersons(): Flux<Person> {
        val randomInterval = Flux.interval(Duration.ofSeconds(2))
        return randomInterval.map {
            Person(firstNames.shuffled().first(), lastNames.shuffled().first())
        }
    }
}