package ch.zuehlke.hatch.fsk18.infrastructure

import ch.zuehlke.hatch.data.Tweet
import org.springframework.stereotype.Service

@Service
class TwitterClientMock : TwitterClient {

    override fun observe(termsToObserve: List<String>, onTweetReceived: (Tweet) -> Unit, onObservationCompleted: () -> Unit) {
        println("mock observing")
    }

    override fun stopObserving() {
        println("stop mock observing")
    }

}