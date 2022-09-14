package ru.msu.cmc.webprak.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.msu.cmc.webprak.DAO.SportsmanDAO;
import ru.msu.cmc.webprak.DAO.TeamDAO;
import ru.msu.cmc.webprak.DAO.Team_competitionsDAO;
import ru.msu.cmc.webprak.DAO.Team_participanceDAO;
import ru.msu.cmc.webprak.DAO.impl.SportsmanDAOImpl;
import ru.msu.cmc.webprak.DAO.impl.TeamDAOImpl;
import ru.msu.cmc.webprak.DAO.impl.Team_competitionsDAOImpl;
import ru.msu.cmc.webprak.DAO.impl.Team_participanceDAOImpl;
import ru.msu.cmc.webprak.models.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Controller
public class TeamController {

    @Autowired
    private final TeamDAO teamDAO = new TeamDAOImpl();

    @Autowired
    private final SportsmanDAO sportsmanDAO = new SportsmanDAOImpl();

    @Autowired
    private final Team_competitionsDAO team_competitionsDAO = new Team_competitionsDAOImpl();

    @GetMapping("/team")
    public String teamPage(@RequestParam(name = "teamId") Long teamId,
                           Model model) {
        Team team = teamDAO.getById(teamId);

        if (team == null) {
            model.addAttribute("error_msg", "В базе нет команды с ID = " + teamId);
            return "errorPage";
        }

        List<Team_competitions> teamOpponents = team_competitionsDAO.getTeamOpponents(team);

        model.addAttribute("team", team);
        model.addAttribute("teamService", teamDAO);
        model.addAttribute("teamPlayers", sportsmanDAO.getTeamPlayers(team));
        model.addAttribute("teamOpponents", teamOpponents);
        return "team";
    }

    @GetMapping("/teams")
    public String teamsPage(@RequestParam(name = "backLink") String backLink,
                            @RequestParam(name = "addButton") boolean addButton,
                            Model model) {

        List<Team> teams = (List<Team>)teamDAO.getAll();
        teams.sort(new Comparator<Team>() {
            @Override
            public int compare(Team o1, Team o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        model.addAttribute("teams", teams);
        model.addAttribute("backLink", backLink);
        model.addAttribute("addButton", addButton);
        return "teams";
    }

    private Team currentEditedTeam = null;

    @GetMapping("/editTeam")
    public String editTeamPage(@RequestParam(name = "teamId", required = false) Long teamId,
                               Model model) {

        if (teamId != null) {
            currentEditedTeam = teamDAO.getById(teamId);
            if (currentEditedTeam == null) {
                model.addAttribute("error_msg", "Невозможно отредактировать страницу несуществующей команды");
                return "errorPage";
            }
        } else
            currentEditedTeam = new Team();

        model.addAttribute("name", currentEditedTeam.getName());
        model.addAttribute("coach", currentEditedTeam.getCoach());

        return "editTeam";
    }

    @PostMapping("/saveTeam")
    public String saveTeamPage(@RequestParam(name = "name") String name,
                               @RequestParam(name = "coach", required = false) String coach,
                               Model model) {

        if (currentEditedTeam == null) {
            model.addAttribute("error_msg", "Попытка сохранить в базе данных несуществующие данные");
            return "errorPage";
        }

        currentEditedTeam.setName(name);
        currentEditedTeam.setCoach(coach);

        if (currentEditedTeam.getId() == null)
            teamDAO.save(currentEditedTeam);
        else
            teamDAO.update(currentEditedTeam);

        Long teamId = currentEditedTeam.getId();
        currentEditedTeam = null;

        return "redirect:/team?teamId=" + teamId;
    }

    @PostMapping("/removeTeam")
    public String removeTeamPage(@RequestParam(name = "teamId") Long teamId) {
        teamDAO.deleteById(teamId);
        return "redirect:/teams?addButton=true&backLink=/team?";
    }
}
