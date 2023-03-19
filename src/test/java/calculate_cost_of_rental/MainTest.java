package calculate_cost_of_rental;

import calculate_cost_of_rental.entity.CostPerDay;
import net.bytebuddy.asm.Advice;
import org.junit.Test;
import org.threeten.extra.LocalDateRange;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void getNumberOfDaysInSeasons() {
        LocalDate testDate11 = LocalDate.of(2023, 10,01);
        LocalDate testDate12 = LocalDate.of(2023, 10,10);
        LocalDateRange testRange = LocalDateRange.ofClosed(testDate11, testDate12);
        List<CostPerDay> testSeasons = new ArrayList<>();
        testSeasons.add(new CostPerDay(1,"Middle", LocalDate.parse("2023-09-01"), LocalDate.parse("2023-10-31"), 300));


        Map<String , List<Integer>> resultMap = Main.getNumberOfDaysInSeasons(testRange, testSeasons);

        Map<String, List<Integer>> correctMap = new HashMap<>();
        correctMap.put("Middle", new ArrayList<>());
        correctMap.get("Middle").add(10);
        correctMap.get("Middle").add(300);

        assertEquals(correctMap, resultMap);
    }

    @Test
    public void calculateTotalCost() {

        HashMap<String, List<Integer>> testList = new HashMap<>();

        testList.put("Summer", new ArrayList<>() );
        testList.get("Summer").add(1);
        testList.get("Summer").add(400);

        //Test for one season
        int iResult = Main.calculateTotalCost(testList);
        int iCorrect = 400;
        assertEquals(iCorrect, iResult);
    }

    @Test
    public void checkWithOldReservation() {
        List<LocalDateRange> testOldReservations = new ArrayList<>();
        LocalDateRange testRange = LocalDateRange.parse("2023-10-01/2023-10-10");
        testOldReservations.add(LocalDateRange.parse("2023-09-20/2023-10-05"));

        boolean bResult = Main.checkWithOldReservation(testRange, testOldReservations);
        boolean bCorrect = true;

        assertEquals(bCorrect, bResult);
    }
}