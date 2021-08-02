package lt.Renaldas.tasks.controllers;

import lt.Renaldas.tasks.entities.GetStats;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TaskController {

    @GetMapping("/gameinjectionoverlay")
    public String greeting(@RequestParam(name = "playertag", required = true) String playertag, @RequestParam(name = "platform", required = true) String platform, Model model) {


        GetStats getStats = new GetStats(playertag, platform);
        GetStats.Player player = getStats.getPlayerStats();

        model.addAttribute("LKD", player.lifetimeKD);
        model.addAttribute("WKD", player.winKD);
        model.addAttribute("KILLS", player.brAll);
        model.addAttribute("WINS", player.wins);

        return "overlay";
    }

}
