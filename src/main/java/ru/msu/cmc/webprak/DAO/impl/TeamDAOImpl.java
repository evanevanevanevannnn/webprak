package ru.msu.cmc.webprak.DAO.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprak.DAO.TeamDAO;
import ru.msu.cmc.webprak.models.Team;

@Repository
public class TeamDAOImpl extends CommonDAOImpl<Team, Long> implements TeamDAO {

    public TeamDAOImpl() {
        super(Team.class);
    }
}
