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
    private val hosebirdClient: BasicClient
    private val hosebirdEndpoint = StatusesFilterEndpoint()

    init {
        println("init client")
        println(twitterClientProperties.consumerKey)
        val hosebirdAuth = OAuth1(twitterClientProperties.consumerKey, twitterClientProperties.consumerSecret,
                twitterClientProperties.token, twitterClientProperties.tokenSecret)

        hosebirdClient = ClientBuilder()
                .name("Hosebird-Client-forFSK18")
                .hosts(HttpHosts(Constants.STREAM_HOST))
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(StringDelimitedProcessor(msgQueue))
                .build()

    }

    fun observe(termToObserve: String, onTweetReceived: (Tweet) -> Unit, onObservationCompleted: () -> Unit) {
        startObserving(termToObserve, onTweetReceived)

        stopObserving()

        onObservationCompleted()
    }

    fun stopObserving() {
        this.hosebirdClient.stop()
    }

    private fun startObserving(termToObserve: String, onTweetReceived: (Tweet) -> Unit) {
        connectIfNecessary()

        hosebirdEndpoint.trackTerms(listOf(termToObserve))

        while (!hosebirdClient.isDone) {
            val tweetText = msgQueue.take()
            if (tweetText.contains("created_at", true)) {
                val tweet = JSON.nonstrict.parse<Tweet>(tweetText)
                if (tweet.text.contains(termToObserve, true)) {
                    onTweetReceived(tweet)
                }
            }
        }
    }

    private fun connectIfNecessary() {
        try {
            hosebirdClient.connect()
        } catch (e: IllegalStateException) {
            println("Connection already established.")
        }
    }
}