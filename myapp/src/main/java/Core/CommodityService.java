package Core;

import Entity.Commodity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CommodityService {
    public static Commodity getCommodity(int id) {
        //System.out.println(id);
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        List<Commodity> commodities = (List<Commodity>) session
                .createQuery("select commodity from Entity.Commodity commodity where id=:id")
                .setParameter("id", id).list();
        tx.commit();
        session.close();
        return commodities.get(0);
    }

    public static void updateCommodity(Commodity commodity) {
        int id = commodity.getId();
        Integer inventory = commodity.getInventory();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("update Commodity set inventory=:inventory where id=:id")
                .setParameter("inventory", inventory).setParameter("id", id).executeUpdate();
        tx.commit();
        session.close();
    }

    public static void insertCommodity(Commodity commodity) {
        int id = commodity.getId();

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.save(commodity);
        tx.commit();
        session.close();
    }

    public static void clearCommodity() {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("delete from Commodity ").executeUpdate();
        tx.commit();
        session.close();
    }

    public static List<Integer> getCommodityName() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        List<Integer> names = (List<Integer>) session.createSQLQuery("select Commodity.id from Commodity commodity").list();
        tx.commit();
        session.close();
        return names;
    }

}
