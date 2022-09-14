package ru.msu.cmc.webprak.DAO;

import ru.msu.cmc.webprak.models.Team_competitions;
import ru.msu.cmc.webprak.models.Team;
import ru.msu.cmc.webprak.models.Competition;

import java.util.List;

public interface Team_competitionsDAO extends CommonDAO<Team_competitions, Long> {
    List<Team_competitions> getTeamCompetitions(Team team);
    List<Team_competitions> getTeamOpponents(Team team);
    List<Team_competitions> getCompetitionTeams(Competition competition);
    void deleteCompetitionTeams(Competition competition);
    void deleteCompetitionTeam(Competition competition, Team team);
}
