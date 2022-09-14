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

import ru.msu.cmc.webprak.DAO.Team_participanceDAO;
import ru.msu.cmc.webprak.models.Team_participance;
import ru.msu.cmc.webprak.models.Sportsman;
import ru.msu.cmc.webprak.models.Team;

import java.util.List;
import java.util.Objects;

@Repository
public class Team_participanceDAOImpl extends CommonDAOImpl<Team_participance, Long> implements Team_participanceDAO {

    public Team_participanceDAOImpl() {
        super(Team_participance.class);
    }

    public List<Team_participance> getPersonTeams(Sportsman person) {
        List<Team_participance> res = new ArrayList<>();
        for (Team_participance team_participance : getAll()) {
            if (Objects.equals(team_participance.getPerson().getId(), person.getId())) {
                res.add(team_participance);
            }
        }
        return res;
    }
}
