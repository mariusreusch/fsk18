package ch.zuehlke.hatch.fsk18.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SayHelloRestController {

    @GetMapping("/hello")
    fun sayHello(): String {
        return "Hello";
    }
}