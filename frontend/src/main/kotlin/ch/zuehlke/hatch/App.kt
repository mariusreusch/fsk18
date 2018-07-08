package ch.zuehlke.hatch

import ch.zuehlke.hatch.data.Person
import ch.zuehlke.hatch.sse.EventListener
import ch.zuehlke.hatch.sse.ServerSentEventSource
import org.w3c.dom.MessageEvent
import org.w3c.dom.events.Event
import react.*

interface AppState : RState {
    var persons: MutableList<Person>
}

class App : RComponent<RProps, AppState>() {

    override fun AppState.init() {
        val onMessage = handleMessage()
        ServerSentEventSource("/persons", listOf(EventListener("message", onMessage))).startListening()
        this.persons = mutableListOf()
    }

    private fun handleMessage(): (Event) -> Unit {
        return { event: Event ->
            if (event is MessageEvent) {
                val newPersons = this.state.persons.toMutableList()
                newPersons.add(JSON.parse("${event.data}"))
                setState {
                    persons = newPersons
                }
            }
        }
    }

    override fun RBuilder.render(): ReactElement? {
        return personStream(state.persons)
    }
}

fun RBuilder.app() = child(App::class) {}