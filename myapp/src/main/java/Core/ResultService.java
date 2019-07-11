package Core;

import Entity.Commodity;
import Entity.Result;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

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

    public static void clearResult() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("delete from Result").executeUpdate();
        tx.commit();
        session.close();
    }

    public static Result getResultById(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        List<Result> results =
                (List<Result>) session
                .createQuery("select result from Entity.Result result where id=:id")
                .setParameter("id", id).list();
        tx.commit();
        session.close();
        return results.get(0);
    }
    public static List<Result> getResultByUserId(int user_id){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        List<Result> results =
                (List<Result>) session
                        .createQuery("select result from Entity.Result result where user_id=:user_id")
                        .setParameter("user_id", user_id).list();
        tx.commit();
        session.close();
        return results;
    }
}
