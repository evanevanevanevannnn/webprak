package ru.msu.cmc.webprak.DAO;

import ru.msu.cmc.webprak.models.Competition;
import ru.msu.cmc.webprak.models.Seats;

import java.util.List;

public interface SeatsDAO extends CommonDAO<Seats, Long> {

    List<Seats> getCompetitionSeats(Competition competition);

    Long getMinimalPrice(Competition competition);

    Long getSeatsAmount(Competition competition);
}
