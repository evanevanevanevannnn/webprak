package ru.msu.cmc.webprak.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.msu.cmc.webprak.DAO.SportsmanDAO;
import ru.msu.cmc.webprak.DAO.Sportsmans_competitionsDAO;
import ru.msu.cmc.webprak.DAO.TeamDAO;
import ru.msu.cmc.webprak.DAO.Team_participanceDAO;
import ru.msu.cmc.webprak.DAO.impl.SportsmanDAOImpl;
import ru.msu.cmc.webprak.DAO.impl.Sportsmans_competitionsDAOImpl;
import ru.msu.cmc.webprak.DAO.impl.TeamDAOImpl;
import ru.msu.cmc.webprak.DAO.impl.Team_participanceDAOImpl;
import ru.msu.cmc.webprak.models.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class SportsmanController {

    @Autowired
    private final SportsmanDAO sportsmanDAO = new SportsmanDAOImpl();

    @Autowired
    private final TeamDAO teamDAO = new TeamDAOImpl();

    @Autowired
    private final Team_participanceDAO team_participanceDAO = new Team_participanceDAOImpl();

    @Autowired
    private final Sportsmans_competitionsDAO sportsmans_competitionsDAO = new Sportsmans_competitionsDAOImpl();

    @GetMapping("/sportsman")
    public String sportsmanPage(@RequestParam(name = "sportsmanId") Long sportsmanId, Model model) {
        Sportsman person = sportsmanDAO.getById(sportsmanId);

        if (person == null) {
            model.addAttribute("error_msg", "В базе нет человека с ID = " + sportsmanId);
            return "errorPage";
        }

        List<Sportsmans_competitions> sportsmansOpponents = sportsmans_competitionsDAO.getSportsmanOpponents(person);

        model.addAttribute("person", person);
        model.addAttribute("personService", sportsmanDAO);
        model.addAttribute("teamParticipanceService", team_participanceDAO);
        model.addAttribute("sportsmansOpponents", sportsmansOpponents);

        return "sportsman";
    }

    @GetMapping("/sportsmans")
    public String sportsmansPage(@RequestParam(name = "addButton") boolean addButton,
                                 @RequestParam(name = "backLink") String backLink,
                                 Model model) {
        List<Sportsman> people = (List<Sportsman>)sportsmanDAO.getAll();
        people.sort(new Comparator<Sportsman>() {
            @Override
            public int compare(Sportsman o1, Sportsman o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        model.addAttribute("people", people);
        model.addAttribute("addButton", addButton);
        model.addAttribute("backLink", backLink);
        return "sportsmans";
    }

    private Sportsman currentEditedSportsman = null;

    @GetMapping("/editSportsman")
    public String editSportsmanPage(@RequestParam(name = "sportsmanId", required = false) Long sportsmanId,
                                    @RequestParam(name = "teamId", required = false) Long teamId,
                                    Model model) {

        if (currentEditedSportsman == null) {
            if (sportsmanId != null) {
                currentEditedSportsman = sportsmanDAO.getById(sportsmanId);
                if (currentEditedSportsman == null) {
                    model.addAttribute("error_msg", String.format("Спортсмен с ID = %d отсутствует в базе.", sportsmanId));
                    return "errorPage";
                }
            } else
                currentEditedSportsman = new Sportsman();
        }

        if (teamId != null) {
            Team currTeam = teamDAO.getById(teamId);
            if (currTeam == null) {
                model.addAttribute("error_msg", String.format("Команда с ID = %d отсутствует в базе.", teamId));
                return "errorPage";
            } else
                currentEditedSportsman.setCurrent_team(currTeam);
        } else
            currentEditedSportsman.setCurrent_team(null);

        model.addAttribute("currEdit", currentEditedSportsman);
        model.addAttribute("currTeam", currentEditedSportsman.getCurrent_team());
        model.addAttribute("currName", currentEditedSportsman.getName());
        model.addAttribute("currBirthDate", currentEditedSportsman.getBirth_date());

        return "editSportsman";
    }

    @PostMapping("/saveSportsman")
    public String saveSportsmanPage(@RequestParam(name = "name") String name,
                                    @RequestParam(name = "birthDate", required = false) String birthDate,
                                    Model model) {

        if (currentEditedSportsman == null) {
            model.addAttribute("error_msg", "Попытка сохранить в базе данных несуществующие данные");
            return "errorPage";
        }

        currentEditedSportsman.setName(name);
        currentEditedSportsman.setBirth_date(birthDate);

        if (currentEditedSportsman.getId() == null)
            sportsmanDAO.save(currentEditedSportsman);
        else
            sportsmanDAO.update(currentEditedSportsman);

        Long sportsmanId = currentEditedSportsman.getId();
        currentEditedSportsman = null;

        return "redirect:/sportsman?sportsmanId=" + sportsmanId;
    }

    @PostMapping("/removeSportsman")
    public String removeSportsmanPage(@RequestParam(name = "sportsmanId") Long sportsmanId) {
        sportsmanDAO.deleteById(sportsmanId);
        return "redirect:/sportsmans?addButton=true&backLink=/sportsman?";
    }
}