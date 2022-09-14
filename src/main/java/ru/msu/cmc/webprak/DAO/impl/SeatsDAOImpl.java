package ru.msu.cmc.webprak.DAO.impl;

import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprak.DAO.SeatsDAO;
import ru.msu.cmc.webprak.models.Competition;
import ru.msu.cmc.webprak.models.Seats;
import ru.msu.cmc.webprak.models.Sports;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SeatsDAOImpl extends CommonDAOImpl<Seats, Long> implements SeatsDAO {

    public SeatsDAOImpl() {
        super(Seats.class);
    }

    public List<Seats> getCompetitionSeats(Competition competition) {
        List<Seats> competitionSeats = new ArrayList<>();
        for (Seats seat : getAll())
            if (seat.getCompetition().getId().equals(competition.getId()))
                competitionSeats.add(seat);

        return competitionSeats;
    }

    public Long getMinimalPrice(Competition competition) {
        Long minPrice = -1L;
        for (Seats seat : getCompetitionSeats(competition))
            if (minPrice == -1 || minPrice > seat.getPrice())
                minPrice = seat.getPrice();

        return minPrice;
    }

    public Long getSeatsAmount(Competition competition) {
        Long sumAmount = 0L;
        for (Seats seat : getCompetitionSeats(competition))
            sumAmount += seat.getAmount();
        return sumAmount;
    }
}
