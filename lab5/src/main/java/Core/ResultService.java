package Core;

import Entity.Result;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ResultService {
    public static void addResult(Result result) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.save(result);
        tx.commit();
        session.close();
    }
}
