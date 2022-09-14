package ru.msu.cmc.webprak.DAO.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprak.DAO.SportsDAO;
import ru.msu.cmc.webprak.models.Sports;

@Repository
public class SportsDAOImpl extends CommonDAOImpl<Sports, Long> implements SportsDAO {

    public SportsDAOImpl() {
        super(Sports.class);
    }
}
