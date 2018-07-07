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
        val onMessage = { event: Event ->
            if (event is MessageEvent) {
                console.log(event)
                this.persons.add(JSON.parse<Person>("${event.data}"))
            }
        }

        ServerSentEventSource("/persons", listOf(EventListener("message", onMessage))).startListening()
        this.persons = mutableListOf()

    }

    override fun RBuilder.render(): ReactElement? {
        return personStream(state.persons)
    }


}


fun RBuilder.app() = child(App::class) {}