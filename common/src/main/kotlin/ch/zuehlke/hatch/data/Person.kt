package ch.zuehlke.hatch.data

import kotlinx.serialization.Serializable


@Serializable
data class Person(val firstName: String, var lastName: String) {
}