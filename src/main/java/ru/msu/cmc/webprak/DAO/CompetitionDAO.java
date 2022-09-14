package ru.msu.cmc.webprak.DAO;

import lombok.Builder;
import lombok.Getter;
import ru.msu.cmc.webprak.models.Competition;
import ru.msu.cmc.webprak.models.Sports;
import ru.msu.cmc.webprak.models.Sportsman;
import ru.msu.cmc.webprak.models.Team;

import java.util.List;
import java.util.Date;

public interface CompetitionDAO extends CommonDAO<Competition, Long> {
    List<Competition> getByFilter(Filter filter);
    @Builder
    @Getter
    class Filter {
        private String tournament;
        private String compDate;
        private String venue;
        private Long sportId;
        private Long minimalAmount;
        private Long minimalPrice;
    }

    static Filter.FilterBuilder getFilterBuilder() {
        return Filter.builder();
    }
}
