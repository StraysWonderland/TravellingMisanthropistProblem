import GraphStructure.GraphManager;
import Util.OsmParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@EnableAutoConfiguration
public class Main {

    private static final String template = "Hello, %s!";
    private static GraphManager graphManager;

    @RequestMapping("/greeting")
    @ResponseBody
    public String greeting(@RequestParam(value = "name", defaultValue = "Fuckface") String name) {
        return String.format(template, name);
    }

    // start by parsing the graph; then launch springboot
    public static void main(String[] args) throws Exception {
        graphManager = new GraphManager();
        graphManager.parseFromPbf();
        SpringApplication.run(Main.class, args);
    }
}