package ch.zuehlke.hatch

import kotlinx.html.js.onClickFunction
import org.w3c.dom.EventSource
import org.w3c.fetch.RequestInit
import react.*
import react.dom.button
import kotlin.browser.window
import kotlin.js.json

class App : RComponent<RProps, RState>() {

    override fun RBuilder.render(): ReactElement? {
        return button {
            +"Do it"
            attrs.onClickFunction = {

                listenToServerSentEventSource("/testflux")
                listenToServerSentEventSource("/randomNumbers")

                val response = window.fetch("/hello", object : RequestInit {
                    override var method: String? = "GET"
                    override var headers: dynamic = json("Accept" to "application/json")
                })

                response.then { resp -> console.log(resp) }


            }
        }
    }

    private fun listenToServerSentEventSource(url: String) {
        var source = EventSource(url);
        source.onopen = {
            console.log("connection to $url opened");
            console.log("readystate of $url is ${source.readyState}");
        };
        //source.addEventListener("random", {e -> console.log(e)}, false);
        source.addEventListener("message", {e -> console.log(e)}, false);
        source.onmessage = { message -> console.log("message: $message from $url") };
        source.onerror = { console.log("onerror from $url") };
    }
}

fun RBuilder.app() = child(App::class) {}
