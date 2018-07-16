package ch.zuehlke.hatch

import ch.zuehlke.hatch.data.Person
import ch.zuehlke.hatch.data.Tweet
import org.w3c.dom.EventSource
import org.w3c.dom.MessageEvent
import org.w3c.dom.events.Event
import react.*

interface AppState : RState {
    var persons: List<Person>
    var tweets: List<Tweet>
}

class App : RComponent<RProps, AppState>() {

    override fun AppState.init() {
        EventSource("/persons").onmessage = handleMessage()
        EventSource("/liveTweets?withTerm=Java").onmessage = handleNewTweet()
        this.persons = emptyList()
        this.tweets = emptyList()
    }

    private fun handleMessage(): (Event) -> Unit {
        return { event ->
            if (event is MessageEvent) {
                val newPersons = this.state.persons.toMutableList()
                newPersons.add(JSON.parse("${event.data}"))

                setState {
                    persons = newPersons.toList()
                }
            }
        }
    }

    private fun handleNewTweet(): (Event) -> Unit {
        return { event ->
            if (event is MessageEvent) {
                val newTweets = this.state.tweets.toMutableList()
                val newTweet = JSON.parse<Tweet>("${event.data}")
                newTweets.add(newTweet)
                println("Hello new tweet $newTweet")
                setState {
                    tweets = newTweets.toList()
                }
            }
        }
    }

    override fun RBuilder.render(): ReactElement? {
        return twitterStream(state.tweets)
    }
}

fun RBuilder.app() = child(App::class) {}