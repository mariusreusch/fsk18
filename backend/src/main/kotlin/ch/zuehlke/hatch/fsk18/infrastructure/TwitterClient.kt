package ch.zuehlke.hatch.fsk18.infrastructure

import ch.zuehlke.hatch.data.Tweet

interface TwitterClient {

    fun observe(termsToObserve: List<String>, onTweetReceived: (Tweet) -> Unit, onObservationCompleted: () -> Unit)

    fun stopObserving()
}