package ch.zuehlke.hatch

import ch.zuehlke.hatch.data.Person
import react.RBuilder
import react.ReactElement
import react.dom.div
import react.dom.p

fun RBuilder.personStream(persons: List<Person>): ReactElement? {
    return div { +"My new Title"
        for (person in persons) {
            p { "Hello ${person.firstName} ${person.lastName}" }
        }
    }
}