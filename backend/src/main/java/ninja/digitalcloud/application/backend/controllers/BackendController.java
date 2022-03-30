package ninja.digitalcloud.application.backend.controllers;

import ninja.digitalcloud.application.backend.model.Greetings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Greetings from the backend.
 */
@RestController
@CrossOrigin()
public class BackendController {

    private final static Logger log = LoggerFactory.getLogger(BackendController.class);

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/hello-world")
    @ResponseBody
    public Greetings sayHello(@RequestParam(name="name", required=false, defaultValue="Stranger") String name) {
        final Greetings greetings = new Greetings(counter.incrementAndGet(), String.format(template, name));
        log.info ("/hello-world returned: " + greetings);
        return greetings;
    }

}
