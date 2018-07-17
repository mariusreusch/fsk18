package ch.zuehlke.hatch.fsk18.infrastructure

import ch.zuehlke.hatch.data.Tweet
import com.twitter.hbc.ClientBuilder
import com.twitter.hbc.core.Constants
import com.twitter.hbc.core.HttpHosts
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint
import com.twitter.hbc.core.event.Event
import com.twitter.hbc.core.processor.StringDelimitedProcessor
import com.twitter.hbc.httpclient.BasicClient
import com.twitter.hbc.httpclient.auth.OAuth1
import kotlinx.serialization.json.JSON
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import reactor.core.publisher.FluxSink
import java.util.concurrent.LinkedBlockingQueue

@Service
class TwitterClient {

    constructor() {
        println("hello twitter client")
    }

    @Async
    fun observeTerm(sink: FluxSink<Tweet>, term: String) {
        val (msgQueue, hosebirdClient) = createTwitterClient(listOf(term))
        hosebirdClient.connect()
        while (!hosebirdClient.isDone) {
            val tweetText = msgQueue.take()
            if (tweetText.contains("created_at", true)) {
                val tweet = JSON.nonstrict.parse<Tweet>(tweetText)
                if (tweet.text.contains(term, true)) {
                    println("New tweet parsed for term $term")
                    sink.next(tweet)
                }
            }
        }
        hosebirdClient.stop()
        sink.complete()
    }

    private fun createTwitterClient(termsToTrack: List<String>): Pair<LinkedBlockingQueue<String>, BasicClient> {
        val msgQueue = LinkedBlockingQueue<String>(100000)
        val eventQueue = LinkedBlockingQueue<Event>(1000)

        /** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
        val hosebirdHosts = HttpHosts(Constants.STREAM_HOST)
        val hosebirdEndpoint = StatusesFilterEndpoint()

        hosebirdEndpoint.trackTerms(termsToTrack)

        // These secrets should be read from a config file
        val hosebirdAuth = OAuth1("qzO6uN9sQkcfyzRwDLlKybvRF", "UrF8dBWQkIZZlADzybUd2Ji4RfH34cBoLwcsaahJGAB54kwSIM",
                "715527321516683264-kFaZt0qCsBHT77FOCqbw1NNiPjIMVZm", "qcPWlpQMikwb0DuC2e0n9o1fjq1kXdSjM5spqTkjEXHe8")

        val hosebirdClient = ClientBuilder()
                .name("Hosebird-Client-for-${termsToTrack.joinToString("-")}")
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(StringDelimitedProcessor(msgQueue))
                .eventMessageQueue(eventQueue).build()
        return Pair(msgQueue, hosebirdClient)
    }
}

