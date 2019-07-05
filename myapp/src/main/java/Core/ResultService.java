package Core;

import Entity.Result;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ResultService {
    public static void addResult(Result result) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.save(result);
//        session.evict(result);
        tx.commit();
        session.close();
    }

    public static void initResult() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        String sql = "truncate table Result";
//        String sql = "alter table Result AUTO_INCREMENT=0;";
        session.createSQLQuery(sql).executeUpdate();
        session.flush();
        tx.commit();
        session.close();
    }
}
