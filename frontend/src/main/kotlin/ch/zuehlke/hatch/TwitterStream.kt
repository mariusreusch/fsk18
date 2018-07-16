package ch.zuehlke.hatch

import ch.zuehlke.hatch.data.Tweet
import react.RBuilder
import react.ReactElement
import react.dom.div
import react.dom.p

fun RBuilder.twitterStream(tweets: List<Tweet>): ReactElement? {
    return div {
        +"My new Title"
        for (tweet in tweets) {
            p { +"Hello ${tweet.text}" }
        }
    }
}