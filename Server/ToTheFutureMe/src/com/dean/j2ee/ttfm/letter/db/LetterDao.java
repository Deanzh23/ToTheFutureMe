package com.dean.j2ee.ttfm.letter.db;

import com.dean.j2ee.framework.db.ConvenientDao;
import com.dean.j2ee.ttfm.letter.bean.LetterEntity;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

}
