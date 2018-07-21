package ch.zuehlke.hatch.fsk18.infrastructure

import ch.zuehlke.hatch.data.Tweet
import com.twitter.hbc.ClientBuilder
import com.twitter.hbc.core.Constants
import com.twitter.hbc.core.HttpHosts
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint
import com.twitter.hbc.core.processor.StringDelimitedProcessor
import com.twitter.hbc.httpclient.BasicClient
import com.twitter.hbc.httpclient.auth.OAuth1
import kotlinx.serialization.json.JSON
import org.springframework.stereotype.Service
import java.util.concurrent.LinkedBlockingQueue

@Service
class TwitterClient(twitterClientProperties: TwitterClientProperties) {

    private val msgQueue: LinkedBlockingQueue<String> = LinkedBlockingQueue<String>()
    private var hosebirdClient: BasicClient? = null
    private val hosebirdEndpoint = StatusesFilterEndpoint()
    private val hosebirdAuth: OAuth1 = OAuth1(twitterClientProperties.consumerKey, twitterClientProperties.consumerSecret,
            twitterClientProperties.token, twitterClientProperties.tokenSecret)

    fun observe(termsToObserve: List<String>, onTweetReceived: (Tweet) -> Unit, onObservationCompleted: () -> Unit) {
        connect()

        startObserving(termsToObserve, onTweetReceived)

        stopObserving()

        onObservationCompleted()
    }

    private fun startObserving(termsToObserve: List<String>, onTweetReceived: (Tweet) -> Unit) {

        hosebirdEndpoint.trackTerms(termsToObserve)

        while (hosebirdClient?.isDone == false) {
            val tweetText = msgQueue.take()
            if (tweetText.contains("created_at", true)) {
                val tweet = JSON.nonstrict.parse<Tweet>(tweetText)
                val tweetTextContainsAtLeastOneTrackedTerm = termsToObserve.any { tweet.text.contains(it, true) }
                if (tweetTextContainsAtLeastOneTrackedTerm) {
                    onTweetReceived(tweet)
                }
            }
        }
    }

    private fun connect() {
        try {
            hosebirdClient = ClientBuilder()
                    .name("Hosebird-Client-forFSK18")
                    .hosts(HttpHosts(Constants.STREAM_HOST))
                    .authentication(hosebirdAuth)
                    .endpoint(hosebirdEndpoint)
                    .processor(StringDelimitedProcessor(msgQueue))
                    .build()
            hosebirdClient?.connect()
        } catch (e: IllegalStateException) {
            println("Connection already established.")
        }
    }

    fun stopObserving() {
        this.hosebirdClient?.stop()
        this.hosebirdClient?.exitEvent
    }
}
