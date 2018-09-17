import GraphStructure.Graph;
import GraphStructure.GraphParserPBF;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class Main {

    private static final String template = "Hello, %s!";
    private static GraphParserPBF graphParserPBF;

    @RequestMapping("/greeting")
    @ResponseBody
    public String greeting(@RequestParam(value = "name", defaultValue = "Fuckface") String name) {
        return String.format(template, name);
    }

    // start by parsing the graph; then launch springboot
    public static void main(String[] args) {

         // graphParserPBF = new GraphParserPBF();
        // graphParserPBF.parseFromPbf();

        Graph graph = new Graph();
        graph.loadMapData();
        //graph.graphFromBinaries();
        //graph.calculateOffsets();
        //graph.getOffsets();
        //graph-.getEdges();
        //graph.getNodes();

        SpringApplication.run(Main.class, args);
    }
}