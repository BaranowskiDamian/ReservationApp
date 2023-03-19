package calculate_cost_of_rental.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="COST_PER_DAY")
public class CostPerDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private int iId;
    @Column(name="SEASONNAME")
    private String sSeasonName;
    @Column(name="BEGIN_DATE")
    private LocalDate BeginDate;
    @Column(name="END_DATE")
    private LocalDate EndDate;
    @Column(name="COST")
    private int iCost;

    public CostPerDay() {
    }

    public CostPerDay(int iId, String sSeasonName, LocalDate beginDate, LocalDate endDate, int iCost) {
        this.iId = iId;
        this.sSeasonName = sSeasonName;
        BeginDate = beginDate;
        EndDate = endDate;
        this.iCost = iCost;
    }

    public int getiId() {
        return iId;
    }

    public String getsSeasonName() {
        return sSeasonName;
    }

    public LocalDate getBeginDate() {
        return BeginDate;
    }

    public LocalDate getEndDate() {
        return EndDate;
    }

    public int getiCost() {
        return iCost;
    }

    public void setBeginDate(LocalDate beginDate) {
        BeginDate = beginDate;
    }

    public void setEndDate(LocalDate endDate) {
        EndDate = endDate;
    }

    @Override
    public String toString() {
        return "CostPerDay{" +
                "iId=" + iId +
                ", sSeasonName='" + sSeasonName + '\'' +
                ", BeginDate=" + BeginDate +
                ", EndDate=" + EndDate +
                ", iCost=" + iCost +
                '}';
    }
}
