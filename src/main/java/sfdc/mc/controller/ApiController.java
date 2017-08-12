package sfdc.mc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * API Controller
 */
@RestController
@RequestMapping("api")
public class ApiController {

    @RequestMapping("/test")
    public ResponseEntity test(@RequestParam(value="name", defaultValue="World") String name) {
        return new ResponseEntity(String.format("Hello, %s!", name), HttpStatus.OK);
    }
}
