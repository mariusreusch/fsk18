package ch.zuehlke.hatch.fsk18.infrastructure

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("fsk18.twitter.client")
class TwitterClientProperties {
    lateinit var consumerKey: String
    lateinit var consumerSecret: String
    lateinit var token: String
    lateinit var tokenSecret: String
}
