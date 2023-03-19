package calculate_cost_of_rental.Utils;

import calculate_cost_of_rental.entity.CostPerDay;
import calculate_cost_of_rental.entity.Reservation;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.threeten.extra.LocalDateRange;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class HibernateUtils {

    private static SessionFactory sessionFactory = null;

    /*
    CREATING A SESSION FACTORY
    */
    public static SessionFactory getSessionFactory() {

        try {
            if (sessionFactory==null) {
                //create configuration
                Configuration configuration = new Configuration();
                configuration.configure("hibernate.cfg.xml");
                configuration.addAnnotatedClass(CostPerDay.class);
                configuration.addAnnotatedClass(Reservation.class);

                //create session factory
                sessionFactory = configuration.buildSessionFactory();
            }
        } catch (HibernateException e) {
            e.printStackTrace();
            System.err.println("SessionFactory was not created");
        }

        return sessionFactory;
    }

    /*
    READS THE PREDEFINED PRICES FROM THE DATABASE FOR EACH OF THE DEFINED "SEASONS"
    */
    public static List<CostPerDay> getPricesPerDaysInSeasons(){
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        List<CostPerDay> Seasons = new ArrayList<>();

        if (sessionFactory!=null) {
            //Initialize session object
            Session session = sessionFactory.openSession();

            session.beginTransaction();
            Seasons = session.createQuery("from CostPerDay", CostPerDay.class).getResultList();
            session.getTransaction().commit();
        }

        return Seasons;
    }

    /*
    SAVING THE RANGE IN THE DATA BASE
    */
    public static void saveRentDates (Reservation reservation){
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();

        if (sessionFactory!=null) {
            //Initialize session object
            Session session = sessionFactory.openSession();
            try {
                session.beginTransaction();
                session.save(reservation);
                session.getTransaction().commit();
                System.out.println("Saving complete!");
            } catch (Exception e) {
                System.err.println("Saving of the reservation failed!");
            }
        }

    }

    /*
    READING THE ALREADY SAVED RESERVATION DATES
    */
    public static List<Reservation> gettingOldReservations(){
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        List<Reservation> oldReservations = new ArrayList<>();

        if (sessionFactory!=null) {
            //Initialize session object
            Session session = sessionFactory.openSession();
            try {
                session.beginTransaction();
                oldReservations = session.createQuery("from Reservation R", Reservation.class).getResultList();
                session.getTransaction().commit();


            } catch (Exception e) {

            }
        }
        return oldReservations;
    }
}
