package ch.zuehlke.hatch

import react.RBuilder
import react.dom.*

fun RBuilder.twitterStream(receivedTweets: List<ReceivedTweet>) {
    div {
        for (receivedTweet in receivedTweets) {
            div("card") {
                //attrs.style = "width: 18rem;"
                div("card-body") {
                    h5("card-title") {
                        +receivedTweet.tweet.tweetCreator.userName
                    }
                    h6("card-subtitle mb-2 text-muted") {
                        +receivedTweet.tweet.createdAt
                    }
                    p("card-text") {
                        +receivedTweet.tweet.text
                    }
                    p {
                        span("badge badge-info") { +receivedTweet.watchedTwitterTerm.term }
                    }
                }

            }
        }
    }
}