package calculate_cost_of_rental;

import calculate_cost_of_rental.Utils.HibernateUtils;
import calculate_cost_of_rental.entity.CostPerDay;
import calculate_cost_of_rental.entity.Reservation;
import org.threeten.extra.LocalDateRange;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {

        LocalDateRange rentRange;
        HashMap<String, List<Integer>> numberOfDaysInSeason = new HashMap<>();
        List<CostPerDay> PricesPerDayInSeason = new ArrayList<>();
        List<LocalDateRange> oldReservations = new ArrayList<>();

        //setting variables from predefined tables in mysql
        PricesPerDayInSeason = HibernateUtils.getPricesPerDaysInSeasons();

        //getting the dates for rental nad calculating the total cost
        rentRange = getRentRange();
        numberOfDaysInSeason = getNumberOfDaysInSeasons(rentRange, PricesPerDayInSeason);

        //showing spread costs
        showSpreadCosts(numberOfDaysInSeason);

        //saving the reservation
        int iTotalCost = calculateTotalCost(numberOfDaysInSeason);
        savingReservation(rentRange, iTotalCost);

    }

    /*
    getRentRange() IN CONNECTION WITH getDate() IS FOR CHOOSING THE TIME PERIOD FOR THE RENTAL
    */
    public static LocalDateRange getRentRange(){
        LocalDateRange rentRange = null;
        LocalDate beginDate = null;
        LocalDate endDate = null;
        boolean bRentRangeChosen=false;

        while (!bRentRangeChosen) {
            beginDate = getDate("begin");
            if (beginDate != null) {


                boolean bDateChosenCorrectly = false;

                while (!bDateChosenCorrectly) {
                    endDate = getDate("end");
                    if (endDate.isAfter(beginDate)) {
                        rentRange = LocalDateRange.ofClosed(beginDate, endDate);

                        if (!checkWithOldReservation(rentRange, getLocalDateRangeFromOldReservations())) {
                            bDateChosenCorrectly= true;
                            bRentRangeChosen = true;
                        }

                    } else {
                        System.out.println("Your end date is before or the same date as Your begin date!");
                        System.out.println("Please chose another end date!");
                    }
                }
            }
        }
            if (beginDate == null || endDate ==null){
                System.out.println("You have not chosen dates");
                System.out.println("Do You want to repeat? Y/N");
                String s = scanner.nextLine();
                switch (s) {
                    case "n", "N" -> {
                        bRentRangeChosen = true;
                        System.out.println("You have not chosen a date!");
                    }
                }
        }
        return rentRange;
    }
        //choosing LocalDate
    public static LocalDate getDate(String start_end){
        LocalDate date = null;
        boolean bDateChosen = false;

        while (!bDateChosen) {
            System.out.printf("Type Your desired %s date of Your rental (YYYY-MM-DD): %n", start_end);
            String sDate = scanner.nextLine();
            try {
                date = LocalDate.parse(sDate);
                if(start_end.equals("begin")){
                    if(date.isBefore(LocalDate.now().plusDays(3))){
                            if (date.isBefore(LocalDate.now())) {
                                System.out.println("You have chosen a date that is already in the past. Please chose another one.");
                            }else {
                                System.out.println("Reservation has to be done with at least 3 days advance!");
                                System.out.println("Starting date cannot be before: "+LocalDate.now().plusDays(3));
                                System.out.println("If You wish to rent earlier please contact us and check possibility, otherwise chose different date");
                            }
                    }else {
                        bDateChosen = true;
                    }
                }
                if(start_end.equals("end")){
                    bDateChosen=true;
                }
            } catch (Exception e) {
                System.out.println("You wrote a date with a different format, please use format YYYY-MM-DD");
            }
            if(sDate.equals("exit")){
                bDateChosen = true;
            }
        }
        return date;
    }

    /*
    getNumberOfDaysInSeasons() CALCULATES THE AMOUNT OF DAYS IN DIFFERENT SEASONS FORM THE CHOSEN TIME PERIOD
     */
    public static HashMap<String, List<Integer>> getNumberOfDaysInSeasons(LocalDateRange rentRange, List<CostPerDay> seasons) {
        LocalDate beginDate = rentRange.getStart();
        LocalDate endDate = rentRange.getEnd();
        HashMap<String, List<Integer>> numberOfDaysInSeason = new HashMap<>();
        List<Integer> DaysAndPrices = new ArrayList<>();

        for (CostPerDay c : seasons) {
            LocalDateRange ldr = LocalDateRange.ofClosed(c.getBeginDate(), c.getEndDate());
            if (rentRange.isConnected(ldr)) {
                int iTempNumberOfDays=0;
               if(numberOfDaysInSeason.containsKey(c.getsSeasonName())){
                   iTempNumberOfDays = numberOfDaysInSeason.get(c.getsSeasonName()).get(0)+ldr.intersection(rentRange).lengthInDays();
                   numberOfDaysInSeason.put(c.getsSeasonName(),new ArrayList<>());
                   numberOfDaysInSeason.get(c.getsSeasonName()).add(iTempNumberOfDays);
                   numberOfDaysInSeason.get(c.getsSeasonName()).add(c.getiCost());
                } else {
                   numberOfDaysInSeason.put(c.getsSeasonName(), new ArrayList<>());
                   numberOfDaysInSeason.get(c.getsSeasonName()).add(ldr.intersection(rentRange).lengthInDays()+iTempNumberOfDays);
                   numberOfDaysInSeason.get(c.getsSeasonName()).add(c.getiCost());
               }
            }
        }

        int iTotalCost = calculateTotalCost(numberOfDaysInSeason);
        System.out.printf("Total cost for you rental from %tD to %tD (%d days) is: %d%n", rentRange.getStart(),rentRange.getEnd(), rentRange.lengthInDays() ,iTotalCost);
        return numberOfDaysInSeason;
    }

    public static int calculateTotalCost(HashMap<String, List<Integer>> numberOfDaysInSeason){
        final int[] iTotalCost = {0};
        int iCost=0;

        numberOfDaysInSeason.forEach((key, value)-> {
            iTotalCost[0] += iTotalCost[0] +(value.get(0)*value.get(1));
        });
        for (int i: iTotalCost){
            iCost+=i;
        }
        return iCost;
    }

    public static void showSpreadCosts(HashMap<String, List<Integer>> numberOfDaysInSeason){
        System.out.println("Do You want to see the spread of the cos per season? (Y/N)");
        String s = scanner.nextLine();

        if(s.equals("y")||s.equals("Y")){
            numberOfDaysInSeason.forEach((key, value)-> {
                System.out.printf("There are %d days in %s which cost: %d%n", value.get(0), key, value.get(1)*value.get(0));
            });
        }
    }

    /*
    savingReservation() SAVES THE CHOSEN PERIOD OF TIME WITH CONTACT INFORMATION FOR FUTURE USES
    */
    public static void savingReservation(LocalDateRange rentRange, int iTotalCost){

        System.out.println("Do You want to save your reservation? (Y/N");
        String l = scanner.nextLine();
        if(l.equals("y")||l.equals("Y")){
            System.out.println("Write Your first name: ");
            String sFirstName = scanner.nextLine();
            System.out.println("Write your last name: ");
            String sLastName = scanner.nextLine();
            System.out.println("Write your e-mail adress: ");
            String sMail = scanner.nextLine();
            System.out.println("Write your telephone number: ");
            int iTelephoneNumber = scanner.nextInt();
            LocalDate beginDate = rentRange.getStart();
            LocalDate endDate = rentRange.getEnd().plusDays(2); //getting end date and adding 2 days for cleaning and maintenance

            Reservation reservation = new Reservation(sFirstName, sLastName, sMail, iTelephoneNumber, beginDate, endDate, iTotalCost);
            HibernateUtils.saveRentDates(reservation);
        }
    }

    /*
    checkWithOldReservation() CHECKS FOR CONFLICTS WITH ALREADY SAVED RESERVATION PERIODS AND SHOWS WHICH ARE CONFLICT DATES
    IT COMPARES ONLY WITH DATES THAT ARE AFTER 'now()'
    */
    public static boolean checkWithOldReservation(LocalDateRange rentRange, List<LocalDateRange> oldReservations){
        boolean bIsConnected= false;
        for(LocalDateRange ldr:oldReservations){
            if(ldr.isConnected(rentRange)){
                if(ldr.isAfter(LocalDate.now())) {
                    System.out.println("Your desired period is in conflict with another reservation!");
                    LocalDate beginOfConflict = ldr.intersection(rentRange).getStart();
                    LocalDate endOfConflict = ldr.intersection(rentRange).getEnd();
                    System.out.printf("The conflicting dates are from %tD to %tD%n%n", beginOfConflict, endOfConflict);
                    System.out.println("Please chose different dates!");
                    bIsConnected = true;
                }
            }
        }


        return bIsConnected;
    }

    public static List<LocalDateRange> getLocalDateRangeFromOldReservations(){
        List<LocalDateRange> oldReservationsRanges = new ArrayList<>();
        List<Reservation> oldReservations = HibernateUtils.gettingOldReservations();
        for (Reservation r :oldReservations){
            oldReservationsRanges.add(LocalDateRange.of(r.getBeginDate(), r.getEndDate()));
        }
        return oldReservationsRanges;
    }
}