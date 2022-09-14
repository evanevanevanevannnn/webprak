package ru.msu.cmc.webprak.DAO;

import ru.msu.cmc.webprak.models.Sportsman;
import ru.msu.cmc.webprak.models.Team;

import java.util.List;

public interface SportsmanDAO extends CommonDAO<Sportsman, Long> {

    List<Sportsman> getTeamPlayers(Team team);
}
