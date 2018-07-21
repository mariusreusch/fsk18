package ch.zuehlke.hatch

import kotlinx.css.LinearDimension
import react.RBuilder
import react.dom.div
import react.dom.h5
import react.dom.h6
import react.dom.p
import styled.css
import styled.styledDiv
import styled.styledSpan

fun RBuilder.twitterStream(receivedTweets: List<ReceivedTweet>) {
    for (receivedTweet in receivedTweets.reversed().take(50)) {
        styledDiv {
            css {
                classes = mutableListOf("card")
                marginBottom = LinearDimension("0.5rem")
            }
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
                    for (watchedTwitterTerm in receivedTweet.watchedTwitterTerm) {
                        styledSpan {
                            css {
                                classes = mutableListOf("badge", "badge-info")
                            }
                            +watchedTwitterTerm.term
                        }
                    }
                }
            }

        }
    }
}