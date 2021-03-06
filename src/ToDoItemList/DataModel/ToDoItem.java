package ToDoItemList.DataModel;

import java.io.Serializable;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Created by tusharmathur on 07/06/17.
 */
public class ToDoItem implements Serializable{
    String shortDescription;
    String details;
    LocalDate deadline;

    public ToDoItem(String shortDescription, String details, LocalDate deadline) {
        this.shortDescription = shortDescription;
        this.details = details;
        this.deadline = deadline;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDetails() {
        return details;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return this.shortDescription;
    }

    public boolean isDeadlinePast(){
        if(LocalDate.now().isAfter(this.getDeadline()))
            return true;
        return false;
    }

    public boolean isDeadlineApproching(){
        if(LocalDate.now().equals(this.getDeadline()))
            return true;
        if(LocalDate.now().isBefore(this.getDeadline()) && DAYS.between(LocalDate.now(),this.getDeadline())<=2)
            return true;
        return false;
    }
    public void printDeadline(){
        System.out.println(this.getDeadline());
    }
}
