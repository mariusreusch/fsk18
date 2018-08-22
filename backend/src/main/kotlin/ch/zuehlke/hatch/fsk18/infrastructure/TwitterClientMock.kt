package ch.zuehlke.hatch.fsk18.infrastructure

import ch.zuehlke.hatch.data.Tweet
import ch.zuehlke.hatch.data.TwitterUser
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit.SECONDS

@Service
class TwitterClientMock : TwitterClient {

    override fun observe(termsToObserve: List<String>, onTweetReceived: (Tweet) -> Unit, onObservationCompleted: () -> Unit) {
        var i = 0

        while (i < 100) {
            SECONDS.sleep((1..6).shuffled().first().toLong())
            val tweetText = "This is random tweet with the random number ${generateRandomNumber()} containing the term ${termsToObserve.shuffled().first()}"
            val tweetCreator = TwitterUser("@RandomUser${generateRandomNumber()}")
            onTweetReceived(Tweet(LocalDateTime.now().toString(), tweetText, tweetCreator))
            i++
        }
    }

    override fun stopObserving() {
        println("stop mock observing")
    }

    private fun generateRandomNumber() = (1..100_000).shuffled().first()

}