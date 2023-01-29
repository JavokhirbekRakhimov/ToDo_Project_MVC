package uz.ovir.ovir_project;

import java.time.LocalDate;
import java.time.Period;

public class MAin {
    public static void main(String[] args) {
        LocalDate localDate=LocalDate.of(2022,1,20);
        LocalDate localDate2=LocalDate.of(2022,1,3);
        System.out.println("Period.between(localDate,localDate2) = " + Period.between(localDate, localDate2).getDays());
    }
}
