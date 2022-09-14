package ru.msu.cmc.webprak.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.msu.cmc.webprak.DAO.SportsmanDAO;
import ru.msu.cmc.webprak.DAO.TeamDAO;
import ru.msu.cmc.webprak.DAO.Team_participanceDAO;
import ru.msu.cmc.webprak.DAO.impl.SportsmanDAOImpl;
import ru.msu.cmc.webprak.DAO.impl.TeamDAOImpl;
import ru.msu.cmc.webprak.DAO.impl.Team_participanceDAOImpl;
import ru.msu.cmc.webprak.models.Team_participance;

@Controller
public class ParticipanceController {

    @Autowired
    private final TeamDAO teamDAO = new TeamDAOImpl();

    @Autowired
    private final Team_participanceDAO team_participanceDAO = new Team_participanceDAOImpl();

    @Autowired
    private final SportsmanDAO sportsmanDAO = new SportsmanDAOImpl();

    @GetMapping("/addParticipance")
    public String addParticipancePage(@RequestParam(name = "sportsmanId") Long sportsmanId,
                                      @RequestParam(name = "teamId", required = false) Long teamId,
                                      Model model) {

        model.addAttribute("teamId", teamId);
        model.addAttribute("sportsmanId", sportsmanId);
        model.addAttribute("backLink", "/addParticipance?sportsmanId=" + sportsmanId + "%26");
        model.addAttribute("teamService", teamDAO);
        return "addParticipance";
    }

    @PostMapping("/saveParticipance")
    public String saveParticipancePage(@RequestParam(name = "sportsmanId") Long sportsmanId,
                                       @RequestParam(name = "partStart") String partStart,
                                       @RequestParam(name = "partFinish") String partFinish,
                                       @RequestParam(name = "teamId") Long teamId) {
        Team_participance part = new Team_participance(sportsmanDAO.getById(sportsmanId),
                                                        teamDAO.getById(teamId),
                                                        partStart,
                                                        partFinish);

        team_participanceDAO.save(part);
        return "redirect:/sportsman?sportsmanId=" + sportsmanId;
    }

    @GetMapping("/removeParticipance")
    public String removeParticipance(@RequestParam(name = "sportsmanId") Long sportsmanId,
                                     @RequestParam(name = "partId") Long partId) {
        team_participanceDAO.deleteById(partId);
        return "redirect:/sportsman?sportsmanId=" + sportsmanId;
    }
}
