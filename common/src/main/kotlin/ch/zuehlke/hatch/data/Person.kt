package ch.zuehlke.hatch.data

import kotlinx.serialization.Serializable


@Serializable
open class Person() {
    var firstName: String = ""
    var lastName: String = ""
}