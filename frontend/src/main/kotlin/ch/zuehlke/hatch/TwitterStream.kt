package ch.zuehlke.hatch

import react.RBuilder
import react.dom.*
import styled.css
import styled.styledSpan

fun RBuilder.twitterStream(receivedTweets: List<ReceivedTweet>) {
    div {
        for (receivedTweet in receivedTweets.reversed().take(50)) {
            div("card") {
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
                        styledSpan {
                            css {
                                classes = mutableListOf("badge", "badge-info")
                            }
                            +receivedTweet.watchedTwitterTerm.term
                        }
                    }
                }

            }
        }
    }
}