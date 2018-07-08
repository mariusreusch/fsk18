package ch.zuehlke.hatch

import ch.zuehlke.hatch.data.Person
import org.w3c.dom.EventSource
import org.w3c.dom.MessageEvent
import org.w3c.dom.events.Event
import react.*

interface AppState : RState {
    var persons: List<Person>
}

class App : RComponent<RProps, AppState>() {

    override fun AppState.init() {
        EventSource("/persons").onmessage = handleMessage()
        this.persons = emptyList()
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

    override fun RBuilder.render(): ReactElement? {
        return personStream(state.persons)
    }
}

fun RBuilder.app() = child(App::class) {}