package ch.zuehlke.hatch

import ch.zuehlke.hatch.data.Tweet
import kotlinx.html.classes
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.EventSource
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.MessageEvent
import org.w3c.dom.events.Event
import react.*
import react.dom.div
import react.dom.li
import react.dom.ul
import styled.css
import styled.styledButton
import styled.styledInput

interface AppState : RState {
    var tweets: List<ReceivedTweet>
    var twitterTermToWatchInputValue: String
    var twitterTermsToWatch: List<WatchedTwitterTerm>
}

class App : RComponent<RProps, AppState>() {

    override fun AppState.init() {
        this.tweets = emptyList()
        this.twitterTermsToWatch = emptyList()
    }

    private fun handleNewTweet(watchedTwitterTerm: WatchedTwitterTerm): (Event) -> Unit {
        return { event ->
            if (event is MessageEvent) {
                val newTweets = this.state.tweets.toMutableList()
                val newTweet = JSON.parse<Tweet>("${event.data}")
                newTweets.add(ReceivedTweet(newTweet, watchedTwitterTerm))
                setState {
                    tweets = newTweets.toList()
                }
            }
        }
    }

    override fun RBuilder.render() {
        div("container-fluid") {
            div("row") {
                div("col") {
                    watchEntryInput()
                }
            }

            div("row") {
                div("col") {
                    ul {
                        for (twitterTermsToWatch in state.twitterTermsToWatch) {
                            li {
                                +"${twitterTermsToWatch.term} (${state.tweets.count { it.watchedTwitterTerm == twitterTermsToWatch }})"
                            }
                        }
                    }
                }
            }

            div("row") {
                div("col") {
                    twitterStream(state.tweets)
                }
            }
        }
    }

    private fun RBuilder.watchEntryInput() {
        styledInput {
            css {
                margin = "0.5rem"
            }
            attrs.value = state.twitterTermToWatchInputValue
            attrs.onChangeFunction = {
                val target = it.target as HTMLInputElement
                setState {
                    twitterTermToWatchInputValue = target.value
                }
            }
        }
        styledButton {
            css {
                margin = "0.5rem"
            }
            +"Watch"
            attrs.onClickFunction = {
                if (state.twitterTermToWatchInputValue.isNotBlank()) {
                    val newTwitterTermsToWatch = state.twitterTermsToWatch.toMutableList()

                    val watchedTwitterTerm = WatchedTwitterTerm(state.twitterTermToWatchInputValue)
                    val eventSource = EventSource("/liveTweets?withTerm=${state.twitterTermToWatchInputValue}")
                    eventSource.onmessage = handleNewTweet(watchedTwitterTerm)
                    newTwitterTermsToWatch.add(watchedTwitterTerm)
                    setState {
                        twitterTermsToWatch = newTwitterTermsToWatch.toList()
                        twitterTermToWatchInputValue = ""
                    }


                }
            }
            attrs.classes = setOf("btn", "btn-primary")
        }
    }
}

fun RBuilder.app() = child(App::class) {}