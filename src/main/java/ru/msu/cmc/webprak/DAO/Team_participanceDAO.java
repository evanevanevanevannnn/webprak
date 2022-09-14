package ru.msu.cmc.webprak.DAO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.msu.cmc.webprak.models.Team_participance;
import ru.msu.cmc.webprak.models.Team;
import ru.msu.cmc.webprak.models.Sportsman;

import java.util.List;
import java.util.Date;

public interface Team_participanceDAO extends CommonDAO<Team_participance, Long> {

    List<Team_participance> getPersonTeams(Sportsman person);
}
