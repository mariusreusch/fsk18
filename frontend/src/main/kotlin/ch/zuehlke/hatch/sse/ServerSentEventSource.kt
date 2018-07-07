package ch.zuehlke.hatch.sse

import org.w3c.dom.EventSource

class ServerSentEventSource(private val url: String,
                            private val eventListeners: List<EventListener> = listOf(EventListener("message") { console.log(it) })) {

    fun startListening() {
        val source = EventSource(url);
        source.onopen = {
            console.log("connection to $url opened");
            console.log("readystate of $url is ${source.readyState}");
        };
        source.onmessage = { console.log("message: $it from $url") }
        source.onerror = { console.log("onerror from $url with reason: $it") }
        for (eventListener in eventListeners) {
            source.addEventListener(eventListener.eventName, eventListener.callback, false)

        }
    }
}