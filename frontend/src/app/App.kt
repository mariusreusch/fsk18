package app

import react.*
import react.dom.*
import kotlinx.html.js.onClickFunction
import org.w3c.dom.url.*
import org.w3c.dom.EventSource
import org.w3c.fetch.*

import kotlinx.coroutines.experimental.await
import kotlin.browser.*
import kotlin.js.*

class App : RComponent<RProps, RState>() {

    override fun RBuilder.render() {
        h2 {

            +"Welcome to React with Kotlin"
        }
        button {
            +"Do it"
            attrs.onClickFunction = {
                console.log("HELLLO")

                listenToServerSentEventSource("/testflux")
                listenToServerSentEventSource("/randomNumbers")

                val response = window.fetch("/hello", object : RequestInit {
                    override var method: String? = "GET"
                    override var headers: dynamic = json("Accept" to "application/json")
                });

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
