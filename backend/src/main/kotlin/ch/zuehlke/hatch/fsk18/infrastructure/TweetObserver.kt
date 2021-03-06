package ch.zuehlke.hatch.fsk18.infrastructure

import ch.zuehlke.hatch.data.Tweet
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import reactor.core.publisher.FluxSink

@Service
class TweetObserver(private val twitterClient: TwitterClient) {

    @Async
    fun observeTerm(term: List<String>, sink: FluxSink<Tweet>) {
        sink.onCancel {
            twitterClient.stopObserving()
        }
        twitterClient.observe(term, { sink.next(it) }, { sink.complete() })
    }
}
