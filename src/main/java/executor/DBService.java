package executor;

import dao.BinDAO;
import dao.RequestDAO;
import models.Bin;
import models.Request;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;
import java.util.Vector;

public class DBService {
    private static final String hibernate_show_sql = "true";
    private static final String hibernate_hbm2ddl_auto = "create";

    private final SessionFactory sessionFactory;

    public DBService() {
        Configuration configuration = getH2Configuration();
        sessionFactory = createSessionFactory(configuration);
    }

    @SuppressWarnings("UnusedDeclaration")
    private Configuration getMySqlConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Bin.class);
        configuration.addAnnotatedClass(Request.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/db_example");
        configuration.setProperty("hibernate.connection.username", "tully");
        configuration.setProperty("hibernate.connection.password", "tully");
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        return configuration;
    }

    private Configuration getH2Configuration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Bin.class);
        configuration.addAnnotatedClass(Request.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:./h2db");
        configuration.setProperty("hibernate.connection.username", "tully");
        configuration.setProperty("hibernate.connection.password", "tully");
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        return configuration;
    }


    public Bin getOneBin(String name) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            BinDAO dao = new BinDAO(session);
            Bin bin = dao.getOneBin(name);
            session.close();
            return bin;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public Vector<Bin> getManyBins(Vector<String> names) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            BinDAO dao = new BinDAO(session);
            Vector<Bin>  bins = dao.getManyBins(names);
            session.close();
            return bins;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public long addBin(Bin bin) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            BinDAO dao = new BinDAO(session);
            long id = dao.insertBin(bin);
            transaction.commit();
            session.close();
            return id;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public void updateBin(Bin bin) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            BinDAO dao = new BinDAO(session);
            dao.updateBin(bin);
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public long addRequest(Request request) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            RequestDAO dao = new RequestDAO(session);
            long id = dao.insertRequest(request);
            transaction.commit();
            session.close();
            return id;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

//    public Request getOneRequest(String nameBin, String nameRequest) throws DBException {
//        try {
////            Session session = sessionFactory.openSession();
////            BinDAO dao = new BinDAO(session);
////            Bin bin = dao.getOneBin(name);
////            session.close();
////            return bin;
//        } catch (HibernateException e) {
//            throw new DBException(e);
//        }
//    }
//
//    public Set<Request> getManyRequests(String nameBin, int from, int to) throws DBException {
//        try {
////            Session session = sessionFactory.openSession();
////            BinDAO dao = new BinDAO(session);
////            Vector<Bin>  bins = dao.getManyBins(names);
////            session.close();
////            return bins;
//        } catch (HibernateException e) {
//            throw new DBException(e);
//        }
//    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}
