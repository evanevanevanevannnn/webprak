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

import ru.msu.cmc.webprak.DAO.SportsmanDAO;
import ru.msu.cmc.webprak.models.Sportsman;
import ru.msu.cmc.webprak.models.Team;

import java.util.List;
import java.util.Objects;

@Repository
public class SportsmanDAOImpl extends CommonDAOImpl<Sportsman, Long> implements SportsmanDAO {

    public SportsmanDAOImpl() {
        super(Sportsman.class);
    }

    @Override
    public List<Sportsman> getTeamPlayers(Team team) {
        List<Sportsman> res = new ArrayList<>();
        for (Sportsman sportsman : getAll()) {
            if (sportsman.getCurrent_team() != null && Objects.equals(sportsman.getCurrent_team().getId(), team.getId())) {
                res.add(sportsman);
            }
        }
        return res;
    }
}
