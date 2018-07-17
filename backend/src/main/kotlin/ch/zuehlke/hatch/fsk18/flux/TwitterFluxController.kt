package ch.zuehlke.hatch.fsk18.flux

import ch.zuehlke.hatch.data.Tweet
import ch.zuehlke.hatch.fsk18.infrastructure.TwitterClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class TwitterFluxController(private val twitterClient: TwitterClient) {

    @GetMapping(path = ["/liveTweets"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun stockTransactionEvents(@RequestParam withTerm: String): Flux<Tweet> {
        return Flux.create { sink -> twitterClient.observeTerm(sink, withTerm) }
    }
}