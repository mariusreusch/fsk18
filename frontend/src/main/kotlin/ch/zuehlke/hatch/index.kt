package ch.zuehlke.hatch

import kotlinext.js.*
import react.dom.*
import kotlin.browser.*

fun main(args: Array<String>) {

    render(document.getElementById("root")) {
        app()
    }
}
