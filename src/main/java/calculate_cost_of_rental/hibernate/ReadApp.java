package calculate_cost_of_rental.hibernate;

import calculate_cost_of_rental.Utils.HibernateUtils;
import calculate_cost_of_rental.entity.CostPerDay;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class ReadApp {

    public static CostPerDay reading(int iIndex){
        //create session factory
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        CostPerDay season = null;

        if (sessionFactory!=null) {
            //Initialize session object
            Session session = sessionFactory.openSession();

            session.beginTransaction();
            season  = session.get(CostPerDay.class,iIndex);
            session.getTransaction().commit();
        } return season;
    }
}
