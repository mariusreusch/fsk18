package ch.zuehlke.hatch.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TwitterUser(@SerialName("screen_name") val userName: String) {
}