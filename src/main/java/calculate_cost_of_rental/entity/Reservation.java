package calculate_cost_of_rental.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name="RESERVATIONS")
public class Reservation {
    @Id
    @Column(name="ID")
    private int iID;
    @Column(name="FIRSTNAME")
    private String sFirstName;
    @Column(name="LASTNAME")
    private String sLastName;
    @Column(name="EMAILADRESS")
    private String sEmailAdress;
    @Column(name="TELEPHONENUMBER")
    private int iTelephoneNumber;
    @Column(name="BEGINDATE")
    private LocalDate beginDate;
    @Column(name="ENDDATE")
    private LocalDate endDate;
    @Column(name="COST")
    private int iTotalCost;

    public Reservation(String sFirstName, String sLastName, String sEmailAdress, int iTelephoneNumber, LocalDate beginDate, LocalDate endDate, int iTotalCost) {
        this.sFirstName = sFirstName;
        this.sLastName = sLastName;
        this.sEmailAdress = sEmailAdress;
        this.iTelephoneNumber = iTelephoneNumber;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.iTotalCost = iTotalCost;
    }

    public Reservation() {
    }

    public int getiID() {
        return iID;
    }

    public void setiID(int iID) {
        this.iID = iID;
    }

    public String getsFirstName() {
        return sFirstName;
    }

    public void setsFirstName(String sFirstName) {
        this.sFirstName = sFirstName;
    }

    public String getsLastName() {
        return sLastName;
    }

    public void setsLastName(String sLastName) {
        this.sLastName = sLastName;
    }

    public String getsEmailAdress() {
        return sEmailAdress;
    }

    public void setsEmailAdress(String sEmailAdress) {
        this.sEmailAdress = sEmailAdress;
    }

    public int getiTelephoneNumber() {
        return iTelephoneNumber;
    }

    public void setiTelephoneNumber(int iTelephoneNumber) {
        this.iTelephoneNumber = iTelephoneNumber;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getiTotalCost() {
        return iTotalCost;
    }

    public void setiTotalCost(int iTotalCost) {
        this.iTotalCost = iTotalCost;
    }
}
