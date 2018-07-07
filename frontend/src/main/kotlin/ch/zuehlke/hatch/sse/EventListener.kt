package ch.zuehlke.hatch.sse

import org.w3c.dom.MessageEvent
import org.w3c.dom.events.Event

data class EventListener(val eventName: String, val callback: (Event) -> Unit) {
}