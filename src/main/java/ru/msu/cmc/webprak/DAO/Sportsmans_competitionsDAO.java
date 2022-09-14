package ru.msu.cmc.webprak.DAO;

import ru.msu.cmc.webprak.models.Sportsman;
import ru.msu.cmc.webprak.models.Sportsmans_competitions;
import ru.msu.cmc.webprak.models.Competition;

import java.util.List;

public interface Sportsmans_competitionsDAO extends CommonDAO<Sportsmans_competitions, Long> {
    List<Sportsmans_competitions> getSportsmanCompetitions(Sportsman person);
    List<Sportsmans_competitions> getSportsmanOpponents(Sportsman person);
    List<Sportsmans_competitions> getCompetitionSportsmans(Competition competition);
    void deleteCompetitionSportsmans(Competition competition);
    void deleteCompetitionSportsman(Competition competition, Sportsman sportsman);
}
