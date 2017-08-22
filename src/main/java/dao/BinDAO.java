package dao;

import models.Bin;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.Vector;

public class BinDAO {

    private Session session;

    public BinDAO(Session session) {
        this.session = session;
    }

    public Bin getOneBin(String name) throws HibernateException {
        Criteria criteria = session.createCriteria(Bin.class);
        return ((Bin) criteria.add(Restrictions.eq("name", name)).uniqueResult());
    }

    public Vector<Bin> getManyBins(Vector<String> names) throws HibernateException {
        Vector<Bin> result = new Vector<>(names.size());
        for (String name : names) {
            Criteria criteria = session.createCriteria(Bin.class);
            result.add((Bin) criteria.add(Restrictions.eq("name", name)).uniqueResult());
        }
        return result;
    }

    public long insertBin(Bin bin) throws HibernateException {
        return (Long) session.save(bin);
    }


    public void updateBin(Bin bin) throws HibernateException {
        session.update(bin);
    }

}
