package ru.msu.cmc.webprak.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.webprak.DAO.impl.SportsmanDAOImpl;
import ru.msu.cmc.webprak.DAO.impl.TeamDAOImpl;
import ru.msu.cmc.webprak.DAO.impl.Team_participanceDAOImpl;
import ru.msu.cmc.webprak.models.Sportsman;
import ru.msu.cmc.webprak.models.Team;
import ru.msu.cmc.webprak.models.Team_participance;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class SportsmanDAOTest {

    @Autowired
    private final SportsmanDAO sportsmanDAO = new SportsmanDAOImpl();
    @Autowired
    private final TeamDAO teamDAO = new TeamDAOImpl();
    @Autowired
    private final Team_participanceDAO team_participanceDAO = new Team_participanceDAOImpl();
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testSimpleManipulations() {

//        All sportsmen exist
        List<Sportsman> allSportsmen = (List<Sportsman>)sportsmanDAO.getAll();
        assertEquals(5, allSportsmen.size());

//        All teams exist
        List<Team> allTeams = (List<Team>)teamDAO.getAll();
        assertEquals(5, allTeams.size());

//        First team exists
        Team firstTeam = teamDAO.getById(1L);
        assertEquals(true, firstTeam != null);

//        First team has 2 players in it
        List<Sportsman> firstTeamPlayers = sportsmanDAO.getTeamPlayers(firstTeam);
        assertEquals(2, firstTeamPlayers.size());
        assertEquals("sportsman 1", firstTeamPlayers.get(0).getName());

//        First sportsman participances exits
        List<Team_participance> thirdSportsmanTeams = team_participanceDAO.getPersonTeams(sportsmanDAO.getById(1L));
        assertEquals(2, thirdSportsmanTeams.size());
        assertEquals("team 2", thirdSportsmanTeams.get(0).getTeam().getName());

//        Basic operations are working
        Sportsman person = sportsmanDAO.getById(3L);
        assertEquals(3, person.getId());

        Sportsman notExistingPerson = sportsmanDAO.getById(100L);
        assertNull(notExistingPerson);
    }

    @Test
    void testUpdate() {
        String newBirthDate = "01.01.1999";
        Long sportsmanId = 2L;

        Sportsman sportsman = sportsmanDAO.getById(sportsmanId);
        sportsman.setBirth_date(newBirthDate);
        sportsmanDAO.update(sportsman);

        Sportsman updatedSportsman = sportsmanDAO.getById(sportsmanId);
        assertEquals(updatedSportsman.getBirth_date(), newBirthDate);
    }

    @Test
    void testDelete() {
        Long sportsmanId = 4L;

        Sportsman deletePerson = sportsmanDAO.getById(sportsmanId);
        sportsmanDAO.delete(deletePerson);

        Sportsman shouldBeDeleted = sportsmanDAO.getById(sportsmanId);
        assertNull(shouldBeDeleted);
    }

    @BeforeEach
    void beforeEach() {
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

        List<Team_participance> teamPartList = new ArrayList<>();

        teamPartList.add(new Team_participance(null, sportsmanDAO.getById(1L), teamDAO.getById(2L), "01.01.2010", "01.01.2011"));
        teamPartList.add(new Team_participance(null, sportsmanDAO.getById(1L), teamDAO.getById(3L), "01.01.2010", "01.01.2011"));
        teamPartList.add(new Team_participance(null, sportsmanDAO.getById(2L), teamDAO.getById(4L), "01.01.2010", "01.01.2011"));
        teamPartList.add(new Team_participance(null, sportsmanDAO.getById(3L), teamDAO.getById(5L), "01.01.2010", "01.01.2011"));
        teamPartList.add(new Team_participance(null, sportsmanDAO.getById(4L), teamDAO.getById(5L), "01.01.2010", "01.01.2011"));

        team_participanceDAO.saveCollection(teamPartList);
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

            session.createSQLQuery("TRUNCATE team_participance RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE team_participance_team_part_id_seq RESTART WITH 1;").executeUpdate();
            session.getTransaction().commit();
        }
    }
}
