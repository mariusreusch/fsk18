package ch.zuehlke.hatch.fsk18

import ch.zuehlke.hatch.fsk18.infrastructure.TwitterClientProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
@EnableConfigurationProperties(TwitterClientProperties::class)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}