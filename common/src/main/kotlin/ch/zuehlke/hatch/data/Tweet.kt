package ch.zuehlke.hatch.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Tweet(@SerialName("created_at") val createdAt: String, val text: String,
                 @SerialName("user") val tweetCreator: TwitterUser) {
}