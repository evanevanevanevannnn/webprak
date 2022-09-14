package ru.msu.cmc.webprak.DAO.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

import ru.msu.cmc.webprak.DAO.*;
import ru.msu.cmc.webprak.models.Competition;
import ru.msu.cmc.webprak.models.Seats;
import ru.msu.cmc.webprak.models.Sportsman;
import ru.msu.cmc.webprak.models.Team;

import java.util.List;

@Repository
public class CompetitionDAOImpl extends CommonDAOImpl<Competition, Long> implements CompetitionDAO {

    public CompetitionDAOImpl() {
        super(Competition.class);
    }

    @Autowired
    private final Team_competitionsDAO team_competitionsDAO = new Team_competitionsDAOImpl();

    @Autowired
    private final Sportsmans_competitionsDAO sportsmans_competitionsDAO = new Sportsmans_competitionsDAOImpl();

    @Autowired
    private final SeatsDAO seatsDAO = new SeatsDAOImpl();

    public List<Competition> getByFilter(Filter filter) {
        List<Competition> res = new ArrayList<>();
        for (Competition competition : getAll()) {

            if ((filter.getTournament() == null || competition.getTournament().equals(filter.getTournament())) &&
                    (filter.getCompDate() == null || competition.getComp_date().equals(filter.getCompDate())) &&
                    (filter.getVenue() == null || competition.getVenue().equals(filter.getVenue())) &&
                    (filter.getSportId() == null || competition.getSport().getId().equals(filter.getSportId())) &&
                    (filter.getMinimalAmount() == null || filter.getMinimalAmount() <= seatsDAO.getSeatsAmount(competition)) &&
                    (filter.getMinimalPrice() == null || filter.getMinimalPrice() >= seatsDAO.getMinimalPrice(competition))) {
                res.add(competition);
            }
        }
        return res;
    }
}
