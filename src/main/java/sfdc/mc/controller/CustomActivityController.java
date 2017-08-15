package sfdc.mc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sfdc.mc.util.ConfigJsonConstants;

import javax.json.Json;
import javax.json.JsonObject;

@Controller
@RequestMapping("/ca")
public class CustomActivityController {

    public static Logger logger = LoggerFactory.getLogger(CustomActivityController.class);

    @RequestMapping(value = "")
    public String index(@RequestParam(value="numSteps", defaultValue="0") Integer numSteps, Model model) {

        //if (logger.isDebugEnabled())
        System.out.println("*** Number of steps: " + numSteps);
        model.addAttribute("numSteps", numSteps);
        return "ca";
    }



    @RequestMapping(value = "/execute", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity execute(@RequestBody String json) {
        if (logger.isDebugEnabled())
            logger.debug("json: " + json);
        return new ResponseEntity("OK", HttpStatus.OK);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, headers = "Accept=application/json")
    public String save() {
        if (logger.isDebugEnabled())
            logger.debug("save:");
        return "ca";
    }

    @RequestMapping(value = "/publish", method = RequestMethod.POST, headers = "Accept=application/json")
    public String publish() {
        if (logger.isDebugEnabled())
            logger.debug("publish:");
        return "ca";
    }

    @RequestMapping(value = "/validate", method = RequestMethod.POST, headers = "Accept=application/json")
    public String validate() {
        if (logger.isDebugEnabled())
            logger.debug("validate:");
        return "ca";
    }

    @RequestMapping(value = "/stop", method = RequestMethod.POST, headers = "Accept=application/json")
    public String stop() {
        if (logger.isDebugEnabled())
            logger.debug("stop:");
        return "ca";
    }

    @RequestMapping(value = "/config.json")
    public ResponseEntity getConfig() {

        String caName = System.getenv(ConfigJsonConstants.CA_NAME) != null ? System.getenv(ConfigJsonConstants.CA_NAME) : "Demo Custom Activity";
        String caEditUrl = System.getenv(ConfigJsonConstants.CA_EDIT_URL) != null ? System.getenv(ConfigJsonConstants.CA_EDIT_URL) : "edit.html";
        String caImage15 = System.getenv(ConfigJsonConstants.CA_IMG_15) != null ? System.getenv(ConfigJsonConstants.CA_IMG_15) : "https://s25.postimg.org/hxtt8fj2n/angry-bird-icon-15.png";
        String caImage40 = System.getenv(ConfigJsonConstants.CA_IMG_40) != null ? System.getenv(ConfigJsonConstants.CA_IMG_40) : "https://s25.postimg.org/u9wplx6xb/angry-bird-icon-40.png";
        String caNumSteps = System.getenv(ConfigJsonConstants.CA_NUM_STEPS) != null ? System.getenv(ConfigJsonConstants.CA_NUM_STEPS) : "1";
        String caEditHeight = System.getenv(ConfigJsonConstants.CA_EDIT_HEIGHT) != null ? System.getenv(ConfigJsonConstants.CA_EDIT_HEIGHT) : "600";
        String caEditWidth = System.getenv(ConfigJsonConstants.CA_EDIT_WIDTH) != null ? System.getenv(ConfigJsonConstants.CA_EDIT_WIDTH) : "800";
        String caEndPointName = System.getenv(ConfigJsonConstants.ENDPOINT_NAME) != null ? System.getenv(ConfigJsonConstants.ENDPOINT_NAME) : "ENDPOINT_NAME";
        String caEndPointUrl = System.getenv(ConfigJsonConstants.CA_ENDPOINT_URL) != null ? System.getenv(ConfigJsonConstants.CA_ENDPOINT_URL) : "index.html";
        String caHerokuAppName = System.getenv(ConfigJsonConstants.HEROKU_APP_NAME) != null ? System.getenv(ConfigJsonConstants.HEROKU_APP_NAME) : "HEROKU_APP_NAME";
        String caKey = System.getenv(ConfigJsonConstants.CA_KEY) != null ? System.getenv(ConfigJsonConstants.CA_KEY) : "9ccde4db-7cc2-4aa9-9227-5bb10673ac6d";


        JsonObject value = Json.createObjectBuilder()
                .add("workflowApiVersion", "1.1")
                .add("metaData", Json.createObjectBuilder()
                        .add("icon", caImage40)
                        .add("iconSmall", caImage15)
                        .add("category", "message")
                        .add("isConfigured", true))
                .add("type", "REST")
                .add("lang", Json.createObjectBuilder()
                        .add("en-US", Json.createObjectBuilder()
                                .add("name", caName)
                                .add("description", "{{ACTIVITY_DESCRIPTION}}")))
                .add("arguments", Json.createObjectBuilder()
                        .add("execute", Json.createObjectBuilder()
                                .add("inArguments", Json.createArrayBuilder())
                                .add("url", caEndPointUrl)
                                .add("verb", "POST")
                                .add("body", "")
                                .add("header", "")
                                .add("format", "json")
                                .add("useJwt", false)
                                .add("timeout",10000)))
                .add("configurationArguments", Json.createObjectBuilder()
                        .add("applicationExtensionKey", caKey)
                        .add("save", Json.createObjectBuilder()
                                .add("url", "https://sfmc-api-demo.herokuapp.com/ca/save"))
                        .add("publish", Json.createObjectBuilder()
                                .add("url", "https://sfmc-api-demo.herokuapp.com/ca/publish"))
                        .add("validate", Json.createObjectBuilder()
                                .add("url", "https://sfmc-api-demo.herokuapp.com/ca/validate")))
                .add("wizardSteps", caNumSteps)
                .add("edit", Json.createObjectBuilder()
                        .add("url", caEditUrl)
                        .add("height", caEditHeight)
                        .add("width", caEditWidth))
                .build();
        String result = value.toString();
        System.out.println("*** config.json: " + result);
        return new ResponseEntity(result, HttpStatus.OK);
    }

}
