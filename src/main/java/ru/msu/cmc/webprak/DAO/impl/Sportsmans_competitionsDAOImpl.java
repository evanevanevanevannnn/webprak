package ru.msu.cmc.webprak.DAO.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import ru.msu.cmc.webprak.DAO.Sportsmans_competitionsDAO;
import ru.msu.cmc.webprak.models.Sportsmans_competitions;
import ru.msu.cmc.webprak.models.Sportsman;
import ru.msu.cmc.webprak.models.Competition;

import java.util.List;
import java.util.Objects;

@Repository
public class Sportsmans_competitionsDAOImpl extends CommonDAOImpl<Sportsmans_competitions, Long> implements Sportsmans_competitionsDAO {

    public Sportsmans_competitionsDAOImpl() {
        super(Sportsmans_competitions.class);
    }

    public List<Sportsmans_competitions> getSportsmanCompetitions(Sportsman person) {
        List<Sportsmans_competitions> res = new ArrayList<>();
        for (Sportsmans_competitions sportsmans_competition : getAll()) {
            if (Objects.equals(sportsmans_competition.getPerson().getId(), person.getId())) {
                res.add(sportsmans_competition);
            }
        }
        return res;
    }

    public List<Sportsmans_competitions> getSportsmanOpponents(Sportsman person) {
        List<Sportsmans_competitions> res = new ArrayList<>();
        List<Sportsmans_competitions> personCompetitions = getSportsmanCompetitions(person);
        for (Sportsmans_competitions competition : personCompetitions) {
            List<Sportsmans_competitions> oppponentSportsmans = getCompetitionSportsmans(competition.getCompetition());
            for (Sportsmans_competitions opponent : oppponentSportsmans)
                if (!opponent.getPerson().getId().equals(person.getId()))
                    res.add(opponent);
        }

        return res;
    }

    public List<Sportsmans_competitions> getCompetitionSportsmans(Competition competition) {
        List<Sportsmans_competitions> res = new ArrayList<>();
        for (Sportsmans_competitions sportsmans_competition : getAll()) {
            if (Objects.equals(sportsmans_competition.getCompetition().getId(), competition.getId())) {
                res.add(sportsmans_competition);
            }
        }
        return res;
    }

    public void deleteCompetitionSportsmans(Competition competition) {
        List <Sportsmans_competitions> sportsmans = getCompetitionSportsmans(competition);
        for (Sportsmans_competitions sportsman : sportsmans)
            delete(sportsman);
    }

    public void deleteCompetitionSportsman(Competition competition, Sportsman sportsman) {
        List <Sportsmans_competitions> sportsmans = getCompetitionSportsmans(competition);
        for (Sportsmans_competitions sport_comp : sportsmans)
            if (sport_comp.getPerson().getId().equals(sportsman.getId()))
                delete(sport_comp);
    }
}
