package ch.zuehlke.hatch

import ch.zuehlke.hatch.data.Tweet
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
    var currentlyWatching: Boolean
    var eventSource: EventSource
}

class App : RComponent<RProps, AppState>() {

    override fun AppState.init() {
        println("init")
        this.tweets = emptyList()
        this.twitterTermsToWatch = emptyList()
        this.currentlyWatching = false
    }

    override fun RBuilder.render() {
        div("container-fluid") {
            div("row") {
                div("col") {
                    watchEntryInput()
                }
                div("col") {
                    buttonGroup()

                }
            }

            div("row") {
                div("col") {
                    ul {
                        for (twitterTermsToWatch in state.twitterTermsToWatch) {
                            li {
                                +"${twitterTermsToWatch.term} (${state.tweets.count { it.watchedTwitterTerm.contains(twitterTermsToWatch) }})"
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

    private fun handleNewTweet(): (Event) -> Unit {
        return { event ->
            if (event is MessageEvent) {
                val newTweets = this.state.tweets.toMutableList()
                val newTweet = JSON.parse<Tweet>("${event.data}")
                val watchedTermsInNewTweet = this.state.twitterTermsToWatch.filter { newTweet.text.contains(it.term, true) }

                newTweets.add(ReceivedTweet(newTweet, watchedTermsInNewTweet))
                setState {
                    tweets = newTweets.toList()
                }
            }
        }
    }

    private fun RBuilder.watchEntryInput() {
        styledInput {
            css {
                margin = "0.5rem"
                classes = mutableListOf("form-control")
            }

            attrs.placeholder = "Enter one term to watch"
            attrs.value = state.twitterTermToWatchInputValue
            attrs.onChangeFunction = {
                val target = it.target as HTMLInputElement
                setState {
                    twitterTermToWatchInputValue = target.value
                }
            }
        }

    }

    private fun RBuilder.buttonGroup() {
        styledButton {
            +"Add Term To Watch"
            css {
                classes = mutableListOf("btn", "btn-primary")
                margin = "0.5rem"
            }
            attrs.disabled = state.currentlyWatching || state.twitterTermToWatchInputValue.isEmpty()
            attrs.onClickFunction = {
                if (state.twitterTermToWatchInputValue.isNotBlank()) {
                    val newTwitterTermsToWatch = state.twitterTermsToWatch.toMutableList()
                    val watchedTwitterTerm = WatchedTwitterTerm(state.twitterTermToWatchInputValue)
                    newTwitterTermsToWatch.add(watchedTwitterTerm)
                    setState {
                        twitterTermsToWatch = newTwitterTermsToWatch.toList()
                        twitterTermToWatchInputValue = ""
                    }


                }
            }
        }

        styledButton {
            +"Start Watching"
            css {
                margin = "0.5rem"
                classes = mutableListOf("btn", "btn-primary")
            }
            attrs.disabled = state.twitterTermsToWatch.isEmpty() || state.currentlyWatching
            attrs.onClickFunction = {
                if (state.twitterTermsToWatch.isNotEmpty()) {
                    val newTwitterTermsToWatch = state.twitterTermsToWatch.toMutableList()
                    val requestParams = newTwitterTermsToWatch.map { it.term }.joinToString("&withTerm=")
                    val url = "/liveTweets?withTerm=$requestParams"
                    setState {
                        eventSource = EventSource(url)
                        eventSource.onmessage = handleNewTweet()
                        currentlyWatching = true
                    }


                }
            }
        }

        styledButton {
            +"Reset"
            css {
                margin = "0.5rem"
                classes = mutableListOf("btn", "btn-primary")
            }
            attrs.disabled = !state.currentlyWatching
            attrs.onClickFunction = {
                state.eventSource.close()
                setState {
                    currentlyWatching = false
                    currentlyWatching = false
                    twitterTermToWatchInputValue = ""
                    twitterTermsToWatch = emptyList()
                    tweets = emptyList()
                }

            }
        }
    }
}

fun RBuilder.app() = child(App::class) {}
