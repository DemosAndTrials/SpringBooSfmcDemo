package sfdc.mc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Calendar;

/**
 * Demo Controller
 */
@Controller
public class DemoController {

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @RequestMapping("/test")
    public String test(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        model.addAttribute("year", year);

        return "bootstrap";
    }

    @RequestMapping(value = "/lightning")
    public String lightning() {
        System.out.println("*******************************");

        return "lightning";
    }

    @RequestMapping(value = "/ixn/activities/generic-activity/index.html")
    public String fix() {

        return "ca";
    }

}