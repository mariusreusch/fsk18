package ch.zuehlke.hatch

import react.dom.render
import kotlin.browser.document

fun main(args: Array<String>) {

    render(document.getElementById("root")) {

        app()

        //sse()
    }


}

//private fun RBuilder.sse() {
//    div {
//        h1 {
//            +"Hello"
//        }
//
//        button {
//            +"Do it"
//            attrs.onClickFunction = {
//                ServerSentEventSource("/testflux").startListening()
//                ServerSentEventSource("/randomNumbers").startListening()
//                ServerSentEventSource("/persons").startListening()
//            }
//        }
//
//
//    }
//}
