package ru.msu.cmc.webprak.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.msu.cmc.webprak.DAO.*;
import ru.msu.cmc.webprak.DAO.impl.*;
import ru.msu.cmc.webprak.models.*;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class CompetitionController {

    @Autowired
    private final TeamDAO teamDAO = new TeamDAOImpl();

    @Autowired
    private final SportsDAO sportsDAO = new SportsDAOImpl();

    @Autowired
    private final SportsmanDAO sportsmanDAO = new SportsmanDAOImpl();

    @Autowired
    private final CompetitionDAO competitionDAO = new CompetitionDAOImpl();

    @Autowired
    private final Team_competitionsDAO team_competitionsDAO = new Team_competitionsDAOImpl();

    @Autowired
    private final Sportsmans_competitionsDAO sportsmans_competitionsDAO = new Sportsmans_competitionsDAOImpl();

    @Autowired
    private final SeatsDAO seatsDAO = new SeatsDAOImpl();

    private Competition currentEditedCompetition = null;
    private Boolean sportChanged = false;

    @GetMapping("/sports")
    public String sportsPage(@RequestParam(name = "addButton") boolean addButton,
                             @RequestParam(name = "backLink") String backLink,
                             Model model) {

        List<Sports> sports = (List<Sports>)sportsDAO.getAll();
        sports.sort(new Comparator<Sports>() {
            @Override
            public int compare(Sports o1, Sports o2) {
                return o1.getSport_name().compareTo(o2.getSport_name());
            }
        });

        model.addAttribute("sports", sports);
        model.addAttribute("addButton", addButton);
        model.addAttribute("backLink", backLink);
        return "sports";
    }

    @GetMapping("/addSport")
    public String addSportPage(Model model) {
        return "addSport";
    }

    @PostMapping("/saveSport")
    public String saveSportPage(@RequestParam(name = "sportName") String sportName,
                                @RequestParam(name = "isTeamSport", required = false) Boolean isTeamSport) {
        Sports newSport = new Sports();
        newSport.setSport_name(sportName);
        newSport.setIs_team_sport(isTeamSport != null);

        sportsDAO.save(newSport);
        return "redirect:/sports?addButton=true&backLink=";
    }

    @GetMapping("/removeSport")
    public String removeSportPage(@RequestParam(name = "sportId") Long sportId) {
        sportsDAO.deleteById(sportId);
        return "redirect:/sports?addButton=true&backLink=";
    }

    private String makeCompetitionsBackLink(String tournament,
                                            String compDate,
                                            String venue,
                                            Long minAmount,
                                            Long minPrice) {
        String backLink = "/competitions?";

        if (tournament != null)
            backLink += "tournament=" + tournament + "%26";
        if (compDate != null)
            backLink += "compDate=" + compDate + "%26";
        if (venue != null)
            backLink += "venue=" + venue + "%26";
        if (minAmount != null)
            backLink += "minAmount=" + minAmount + "%26";
        if (minPrice != null)
            backLink += "minPrice=" + minPrice + "%26";

        return backLink;
    }

    @GetMapping("/competitions")
    public String competitionsPage(@RequestParam(name = "tournament", required = false) String tournament,
                                   @RequestParam(name = "compDate", required = false) String compDate,
                                   @RequestParam(name = "venue", required = false) String venue,
                                   @RequestParam(name = "sportId", required = false) Long sportId,
                                   @RequestParam(name = "minAmount", required = false) Long minAmount,
                                   @RequestParam(name = "minPrice", required = false) Long minPrice,
                                   Model model) {

        if (tournament != null && tournament.length() == 0)
            tournament = null;
        if (compDate != null && compDate.length() == 0)
            compDate = null;
        if (venue != null && venue.length() == 0)
            venue = null;

        CompetitionDAO.Filter filter = CompetitionDAO.Filter.builder()
                .tournament(tournament)
                .compDate(compDate)
                .venue(venue)
                .sportId(sportId)
                .minimalAmount(minAmount)
                .minimalPrice(minPrice)
                .build();

        List<Competition> competitions = competitionDAO.getByFilter(filter);
        competitions.sort(new Comparator<Competition>() {
            @Override
            public int compare(Competition o1, Competition o2) {
                LocalDate date1 = LocalDate.parse(o1.getComp_date(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                LocalDate date2 = LocalDate.parse(o2.getComp_date(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                return date2.compareTo(date1);
            }
        });

        model.addAttribute("tournament", tournament);
        model.addAttribute("compDate", compDate);
        model.addAttribute("venue", venue);
        model.addAttribute("sportId", sportId);
        model.addAttribute("minAmount", minAmount);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("sportService", sportsDAO);
        model.addAttribute("competitions", competitions);
        model.addAttribute("backLink", makeCompetitionsBackLink(tournament, compDate, venue, minAmount, minPrice));

        return "competitions";
    }

    @GetMapping("/deleteOpponent")
    public String deleteOpponentPage(@RequestParam(name = "competitionId") Long competitionId,
                                     @RequestParam(name = "sportsmanId", required = false) Long sportsmanId,
                                     @RequestParam(name = "teamId", required = false) Long teamId,
                                     Model model) {

        Competition competition = competitionDAO.getById(competitionId);
        if (competition == null) {
            model.addAttribute("error_msg", "Невалидный запрос.");
            return "errorPage";
        }

        if (sportsmanId != null) {
            Sportsman sportsman = sportsmanDAO.getById(sportsmanId);
            if (sportsman == null) {
                model.addAttribute("error_msg", "Невалидный запрос.");
                return "errorPage";
            }

            sportsmans_competitionsDAO.deleteCompetitionSportsman(competition, sportsman);
        }

        if (teamId != null) {
            Team team = teamDAO.getById(teamId);
            if (team == null) {
                model.addAttribute("error_msg", "Невалидный запрос.");
                return "errorPage";
            }

            team_competitionsDAO.deleteCompetitionTeam(competition, team);
        }

        return "redirect:/competition?competitionId=" + competitionId;
    }

    @GetMapping("/competition")
    public String competitionPage(@RequestParam(name = "competitionId") Long competitionId,
                                  @RequestParam(name = "sportsmanId", required = false) Long sportsmanId,
                                  @RequestParam(name = "teamId", required = false) Long teamId,
                                  Model model) {

        Competition competition = competitionDAO.getById(competitionId);
        if (competition == null) {
            model.addAttribute("error_msg", "Соревнование с ID = " + competitionId + " отсутствует в базе.");
            return "errorPage";
        }

        if (sportsmanId != null) {
            Sportsman sportsman = sportsmanDAO.getById(sportsmanId);
            if (sportsman == null) {
                model.addAttribute("error_msg", "Невалидный запрос.");
                return "errorPage";
            }

            Sportsmans_competitions sport_comp = new Sportsmans_competitions();
            sport_comp.setCompetition(competition);
            sport_comp.setPerson(sportsman);
            sportsmans_competitionsDAO.save(sport_comp);
        }

        if (teamId != null) {
            Team team = teamDAO.getById(teamId);
            if (team == null) {
                model.addAttribute("error_msg", "Невалидный запрос.");
                return "errorPage";
            }

            Team_competitions team_comp = new Team_competitions();
            team_comp.setCompetition(competition);
            team_comp.setTeam(team);
            team_competitionsDAO.save(team_comp);
        }

        if (competition.getSport().getIs_team_sport()) {
            List<Team_competitions> team_competitions = team_competitionsDAO.getCompetitionTeams(competition);
            model.addAttribute("teamOpponents", team_competitions);
        } else {
            List<Sportsmans_competitions> sportsmans_competitions = sportsmans_competitionsDAO.getCompetitionSportsmans(competition);
            model.addAttribute("sportsmanOpponents", sportsmans_competitions);
        }

        model.addAttribute("competition", competition);
        model.addAttribute("seatsDAO", seatsDAO);

        return "competition";
    }

    @GetMapping("/deleteSeats")
    public String deleteSeatsPage(@RequestParam(name = "competitionId") Long competitionId,
                                  @RequestParam(name = "seatsId") Long seatsId,
                                  Model model) {
        Seats seat = seatsDAO.getById(seatsId);
        if (seat == null) {
            model.addAttribute("error_msg", "Невалидный запрос.");
            return "errorPage";
        }

        seatsDAO.delete(seat);

        return "redirect:/competition?competitionId=" + competitionId;
    }

    @GetMapping("/addSeats")
    public String addSeatsPage(@RequestParam(name = "competitionId") Long competitionId,
                               Model model) {
        model.addAttribute("competitionId", competitionId);
        return "addSeats";
    }

    @PostMapping("/saveSeats")
    public String saveSeatsPage(@RequestParam(name = "competitionId") Long competitionId,
                                @RequestParam(name = "type") String type,
                                @RequestParam(name = "amount") Long amount,
                                @RequestParam(name = "price") Long price,
                                Model model) {

        Competition competition = competitionDAO.getById(competitionId);
        if (competition == null) {
            model.addAttribute("error_msg", "Невалидный запрос.");
            return "errorPage";
        }

        Seats seat = new Seats(null, competition, type, amount, price);

        seatsDAO.save(seat);

        return "redirect:/competition?competitionId=" + competitionId;
    }

    @GetMapping("/editCompetition")
    public String editCompetitionPage(@RequestParam(name = "competitionId", required = false) Long competitionId,
                                      @RequestParam(name = "sportId", required = false) Long sportId,
                                      Model model) {

        if (currentEditedCompetition == null) {
            if (competitionId != null) {
                currentEditedCompetition = competitionDAO.getById(competitionId);
                if (currentEditedCompetition == null) {
                    model.addAttribute("error_msg", "Попытка редактирования несуществующих данных.");
                    return "errorPage";
                }
            } else
                currentEditedCompetition = new Competition();
        }

        if (sportId != null) {
            Sports sport = sportsDAO.getById(sportId);
            if (sport == null) {
                model.addAttribute("error_msg", "Спорт с ID = " + sportId + " не существует.");
                return "errorPage";
            } else {
                currentEditedCompetition.setSport(sport);
                sportChanged = true;
            }
        }

        model.addAttribute("currComp", currentEditedCompetition);

        return "editCompetition";
    }

    @PostMapping("/saveCompetition")
    public String saveCompetitionPage(@RequestParam(name = "tournament", required = false) String tournament,
                                      @RequestParam(name = "competitionDate", required = false) String competitionDate,
                                      @RequestParam(name = "venue", required = false) String venue,
                                      @RequestParam(name = "score", required = false) String score,
                                      Model model) {

        if (currentEditedCompetition == null) {
            model.addAttribute("error_msg", "Попытка сохранить несуществующие данные.");
            return "errorPage";
        }

        currentEditedCompetition.setTournament(tournament);
        currentEditedCompetition.setComp_date(competitionDate);
        currentEditedCompetition.setVenue(venue);
        currentEditedCompetition.setScore(score);

        if (currentEditedCompetition.getId() == null)
            competitionDAO.save(currentEditedCompetition);
        else
            competitionDAO.update(currentEditedCompetition);

        if (sportChanged) {
            if (currentEditedCompetition.getSport().getIs_team_sport())
                team_competitionsDAO.deleteCompetitionTeams(currentEditedCompetition);
            else
                sportsmans_competitionsDAO.deleteCompetitionSportsmans(currentEditedCompetition);
        }

        Long competitionId = currentEditedCompetition.getId();
        currentEditedCompetition = null;
        sportChanged = false;

        return "redirect:/competition?competitionId=" + competitionId;
    }

    @PostMapping("/removeCompetition")
    public String removeCompetitionPage(@RequestParam(name = "competitionId") Long competitionId) {
        competitionDAO.deleteById(competitionId);
        return "redirect:/competitions";
    }

    @GetMapping("/ticketTypes")
    public String ticketTypesPage(@RequestParam(name = "competitionId") Long competitionId,
                                  Model model) {

        Competition competition = competitionDAO.getById(competitionId);
        if (competition == null) {
            model.addAttribute("error_msg", "В базе нет соревнования с ID " + competitionId);
            return "errorPage";
        }

        model.addAttribute("competition", competition);
        model.addAttribute("competitionSeats", seatsDAO.getCompetitionSeats(competition));

        return "ticketTypes";
    }

    @GetMapping("/buyTicket")
    public String buyTicketPage(@RequestParam(name = "competitionId") Long competitionId,
                                @RequestParam(name = "seatTypeId", required = false) Long seatId,
                                Model model) {

        Competition competition = competitionDAO.getById(competitionId);
        if (competition == null) {
            model.addAttribute("error_msg", "В базе нет соревнования с ID = " + competitionId);
            return "errorPage";
        }

        Seats seat = null;
        if (seatId != null) {
            seat = seatsDAO.getById(seatId);
            if (seat == null) {
                model.addAttribute("error_msg", "Места с ID = " + seatId + " отсутствуют.");
                return "errorPage";
            }
        }

        model.addAttribute("competition", competition);
        model.addAttribute("seat", seat);
        return "/buyTicket";
    }

    @GetMapping("/boughtTicket")
    public String boughtTicketPage(@RequestParam(name = "seatId") Long seatId,
                                   @RequestParam(name = "amount") Long amount,
                                   Model model) {

        Seats seat = seatsDAO.getById(seatId);
        if (seat == null) {
            model.addAttribute("error_msg", "Места с ID = " + seatId + " отсутствуют.");
            return "errorPage";
        }

        if (amount > seat.getAmount()) {
            model.addAttribute("error_msg", "Вы пытаетесь купить слишком много билетов данного типа.");
            return "errorPage";
        }

        if (seat.getAmount().equals(amount)) {
            seatsDAO.delete(seat);
        } else {
            seat.setAmount(seat.getAmount() - amount);
            seatsDAO.update(seat);
        }

        model.addAttribute("competition", seat.getCompetition());
        model.addAttribute("totalPrice", seat.getPrice() * amount);

        return "/boughtTicket";
    }
}