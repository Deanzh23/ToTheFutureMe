package com.dean.j2ee.ttfm.letter.db;

import com.dean.j2ee.framework.db.ConvenientDao;
import com.dean.j2ee.ttfm.letter.bean.LetterEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 信件DB
 */
@Repository
public class LetterDao extends ConvenientDao {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * 保存信件实例
     *
     * @param letterEntity
     */
    public void saveOrUpdate(LetterEntity letterEntity) {
        saveOrUpdate(sessionFactory, letterEntity);
    }

    /**
     * 查找在时间段内的信件集
     *
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    public List<LetterEntity> findAllLetterBySendTime(long startDateTime, long endDateTime) {
        List<LetterEntity> letterEntities = null;

        Session session = sessionFactory.openSession();

        Query<LetterEntity> query = session.createQuery("from LetterEntity as l where l.receiveDateTime >= ? and l.receiveDateTime <= ? ");
        query.setParameter(0, startDateTime);
        query.setParameter(1, endDateTime);
        letterEntities = query.list();

        session.close();

        return letterEntities;
    }

}
