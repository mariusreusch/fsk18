package ch.zuehlke.hatch.fsk18.infrastructure

import ch.zuehlke.hatch.data.Tweet
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import reactor.core.publisher.FluxSink

@Service
class TwitterClientService(private val twitterClientProperties: TwitterClientProperties) {

    @Async
    fun observeTerm(term: String, sink: FluxSink<Tweet>) {
        val twitterClient = TwitterClient(twitterClientProperties)

        sink.onCancel {
            twitterClient.stopObserving()
        }

        twitterClient.observe(term, { sink.next(it) }, { sink.complete() })
    }
}