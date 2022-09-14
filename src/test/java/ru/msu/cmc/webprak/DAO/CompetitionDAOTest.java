package ru.msu.cmc.webprak.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.webprak.DAO.impl.*;
import ru.msu.cmc.webprak.models.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class CompetitionDAOTest {

    @Autowired
    private final TeamDAO teamDAO = new TeamDAOImpl();
    @Autowired
    private final SportsmanDAO sportsmanDAO = new SportsmanDAOImpl();
    @Autowired
    private final Sportsmans_competitionsDAO sportsmans_competitionsDAO = new Sportsmans_competitionsDAOImpl();
    @Autowired
    private final Team_competitionsDAO team_competitionsDAO = new Team_competitionsDAOImpl();
    @Autowired
    private final CompetitionDAO competitionDAO = new CompetitionDAOImpl();
    @Autowired
    private final SportsDAO sportsDAO = new SportsDAOImpl();

    @Autowired
    private final SeatsDAO seatsDAO = new SeatsDAOImpl();
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testGetByFilter() {
        CompetitionDAO.Filter firstFilter = CompetitionDAO.Filter.builder()
                .tournament("tournament 1")
                .venue("venue 1")
                .build();

        List<Competition> firstFilterGet = competitionDAO.getByFilter(firstFilter);
        assertEquals(1, firstFilterGet.size());
        assertNull(firstFilterGet.get(0).getComp_date());

        CompetitionDAO.Filter secondFilter = CompetitionDAO.Filter.builder()
                .minimalAmount(100L)
                .build();

        List<Competition> secondFilterGet = competitionDAO.getByFilter(secondFilter);
        assertEquals(3, secondFilterGet.size());

        CompetitionDAO.Filter thirdFilter = CompetitionDAO.Filter.builder()
                .minimalPrice(1000L)
                .build();

        List<Competition> thirdFilterGet = competitionDAO.getByFilter(thirdFilter);
        assertEquals(3, thirdFilterGet.size());
    }

    @Test
    void testPlayerCompetitions() {
//        List<Sportsmans_competitions> firstSportsmansCompetitions = sportsmans_competitionsDAO.getSportsmanCompetitions(sportsmanDAO.getById(1L));
//        assertEquals(firstSportsmansCompetitions.size(), 1);
//
//        List<Sportsmans_competitions> firstCompetitionPlayers = sportsmans_competitionsDAO.getCompetitionSportsmans(competitionDAO.getById(1L));
//        assertEquals(firstCompetitionPlayers.size(), 2);
//
//        List<Team_competitions> secondTeamCompetitions = team_competitionsDAO.getTeamCompetitions(teamDAO.getById(2L));
//        assertEquals(secondTeamCompetitions.size(), 1);
//
//        List<Team_competitions> secondCompetitionPLayers = team_competitionsDAO.getCompetitionTeams(competitionDAO.getById(2L));
//        assertEquals(secondCompetitionPLayers.size(), 2);
    }

    @Test
    void testDeleteById() {
        Long shouldBeDeleted = 2L;

        competitionDAO.deleteById(shouldBeDeleted);

        Competition competition = competitionDAO.getById(shouldBeDeleted);
        assertNull(competition);
    }

    @BeforeEach
    void beforeAll() {
        List<Team> teamList = new ArrayList<>();

        teamList.add(new Team(null, "team 1", "coach 1"));
        teamList.add(new Team(null, "team 2", null));
        teamList.add(new Team(null, "team 3", "coach 3"));
        teamList.add(new Team(null, "team 4", null));
        teamList.add(new Team(null, "team 5", "coach 5"));

        teamDAO.saveCollection(teamList);

        List<Sportsman> sportsmanList = new ArrayList<>();

        sportsmanList.add(new Sportsman(null, teamDAO.getById(1L), "sportsman 1", "01.01.2000"));
        sportsmanList.add(new Sportsman(null, null, "sportsman 2", "02.01.2000"));
        sportsmanList.add(new Sportsman(null, teamDAO.getById(1L), "sportsman 3", "03.01.2000"));
        sportsmanList.add(new Sportsman(null, null, "sportsman 4", "04.01.2000"));
        sportsmanList.add(new Sportsman(null, teamDAO.getById(5L), "sportsman 5", "05.01.2000"));


        sportsmanDAO.saveCollection(sportsmanList);

        List<Sports> sportsList = new ArrayList<>();

        sportsList.add(new Sports(null, "tennis", Boolean.FALSE));
        sportsList.add(new Sports(null, "hockey", Boolean.TRUE));
        sportsList.add(new Sports(null, "football", Boolean.TRUE));
        sportsList.add(new Sports(null, "basketball", Boolean.TRUE));
        sportsList.add(new Sports(null, "golf", Boolean.FALSE));

        sportsDAO.saveCollection(sportsList);

        List<Competition> competitionsList = new ArrayList<>();

        competitionsList.add(new Competition(null, sportsDAO.getById(1L), "tournament 1", null, "venue 1", "6:3"));
        competitionsList.add(new Competition(null, sportsDAO.getById(1L), "tournament 2", "01.01.2011", null, "15:10"));
        competitionsList.add(new Competition(null, sportsDAO.getById(3L), "tournament 3", "01.01.2012", null, "3:1"));
        competitionsList.add(new Competition(null, sportsDAO.getById(4L), "tournament 4", "01.01.2024", "venue 4", null));
        competitionsList.add(new Competition(null, sportsDAO.getById(5L), "tournament 5", "01.01.2025", "venue 5", null));

        competitionDAO.saveCollection(competitionsList);

        List <Seats> seatsList = new ArrayList<>();

        seatsList.add(new Seats(null, competitionDAO.getById(1L), "Cheap", 100L, 1000L));
        seatsList.add(new Seats(null, competitionDAO.getById(1L), "Premium", 50L, 2000L));
        seatsList.add(new Seats(null, competitionDAO.getById(2L), "Cheap", 100L, 1500L));
        seatsList.add(new Seats(null, competitionDAO.getById(2L), "Premium", 50L, 3000L));
        seatsList.add(new Seats(null, competitionDAO.getById(3L), "Cheap", 100L, 2000L));
        seatsList.add(new Seats(null, competitionDAO.getById(3L), "Premium", 50L, 4000L));

        seatsDAO.saveCollection(seatsList);

        List<Team_competitions> teamCompList = new ArrayList<>();

        teamCompList.add(new Team_competitions(null, teamDAO.getById(1L), competitionDAO.getById(2L)));
        teamCompList.add(new Team_competitions(null, teamDAO.getById(2L), competitionDAO.getById(2L)));
        teamCompList.add(new Team_competitions(null, teamDAO.getById(3L), competitionDAO.getById(3L)));
        teamCompList.add(new Team_competitions(null, teamDAO.getById(4L), competitionDAO.getById(3L)));
        teamCompList.add(new Team_competitions(null, teamDAO.getById(5L), competitionDAO.getById(4L)));
        teamCompList.add(new Team_competitions(null, teamDAO.getById(1L), competitionDAO.getById(4L)));

        team_competitionsDAO.saveCollection(teamCompList);

        List<Sportsmans_competitions> sportCompList = new ArrayList<>();

        sportCompList.add(new Sportsmans_competitions(null, sportsmanDAO.getById(1L), competitionDAO.getById(1L)));
        sportCompList.add(new Sportsmans_competitions(null, sportsmanDAO.getById(2L), competitionDAO.getById(1L)));
        sportCompList.add(new Sportsmans_competitions(null, sportsmanDAO.getById(4L), competitionDAO.getById(5L)));
        sportCompList.add(new Sportsmans_competitions(null, sportsmanDAO.getById(5L), competitionDAO.getById(5L)));

        sportsmans_competitionsDAO.saveCollection(sportCompList);
    }

    @BeforeAll
    @AfterEach
    void clean() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("TRUNCATE sportsman RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE sportsman_person_id_seq RESTART WITH 1;").executeUpdate();

            session.createSQLQuery("TRUNCATE team RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE team_team_id_seq RESTART WITH 1;").executeUpdate();

            session.createSQLQuery("TRUNCATE team_competitions RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE team_competitions_team_comp_id_seq RESTART WITH 1;").executeUpdate();

            session.createSQLQuery("TRUNCATE sportsmans_competitions RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE sportsmans_competitions_sport_part_id_seq RESTART WITH 1;").executeUpdate();

            session.createSQLQuery("TRUNCATE sports RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE sports_sport_id_seq RESTART WITH 1;").executeUpdate();

            session.createSQLQuery("TRUNCATE seats RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE seats_seats_id_seq RESTART WITH 1;").executeUpdate();

            session.createSQLQuery("TRUNCATE competition RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE competition_competition_id_seq RESTART WITH 1;").executeUpdate();
            session.getTransaction().commit();
        }
    }
}
