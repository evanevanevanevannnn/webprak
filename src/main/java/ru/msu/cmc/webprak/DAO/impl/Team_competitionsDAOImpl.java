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

import ru.msu.cmc.webprak.DAO.Team_competitionsDAO;
import ru.msu.cmc.webprak.models.Competition;
import ru.msu.cmc.webprak.models.Team_competitions;
import ru.msu.cmc.webprak.models.Team;

import java.util.List;
import java.util.Objects;

@Repository
public class Team_competitionsDAOImpl extends CommonDAOImpl<Team_competitions, Long> implements Team_competitionsDAO {

    public Team_competitionsDAOImpl() {
        super(Team_competitions.class);
    }

    public List<Team_competitions> getTeamCompetitions(Team team) {
        List<Team_competitions> res = new ArrayList<>();
        for (Team_competitions team_competition : getAll()) {
            if (Objects.equals(team_competition.getTeam().getId(), team.getId())) {
                res.add(team_competition);
            }
        }
        return res;
    }

    public List<Team_competitions> getTeamOpponents(Team team) {
        List<Team_competitions> teamOpponents = new ArrayList<>();
        List<Team_competitions> teamCompetitions = getTeamCompetitions(team);
        for (Team_competitions competition : teamCompetitions) {
            List<Team_competitions> competitionTeams = getCompetitionTeams(competition.getCompetition());
            for (Team_competitions opponent : competitionTeams)
                if (!opponent.getTeam().getId().equals(team.getId()))
                    teamOpponents.add(opponent);
        }

        return teamOpponents;
    }

    public List<Team_competitions> getCompetitionTeams(Competition competition) {
        List<Team_competitions> res = new ArrayList<>();
        for (Team_competitions team_competition : getAll()) {
            if (Objects.equals(team_competition.getCompetition().getId(), competition.getId())) {
                res.add(team_competition);
            }
        }
        return res;
    }

    public void deleteCompetitionTeams(Competition competition) {
        List<Team_competitions> teams = getCompetitionTeams(competition);
        for (Team_competitions team : teams)
            delete(team);
    }

    public void deleteCompetitionTeam(Competition competition, Team team) {
        List<Team_competitions> teams = getCompetitionTeams(competition);
        for (Team_competitions team_comp : teams)
            if (team_comp.getTeam().getId().equals(team.getId()))
                delete(team_comp);
    }
}
