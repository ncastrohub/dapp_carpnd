package model;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import model.exceptions.*;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class PublicationsEnabledDays extends IdModel {

    public List<LocalDate> disabledDays;

    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate hoy;

    public List<LocalDate> reservedDays;


    public LocalDate getHoy() {
        return hoy;
    }

    public void setHoy(LocalDate hoy) {
        this.hoy = hoy;
    }

    public void setDisabledDays(List<LocalDate> disabledDays) {
        this.disabledDays = disabledDays;
    }

    public void setReservedDays(List<LocalDate> reservedDays) {
        this.reservedDays = reservedDays;
    }

    public List<LocalDate> getReservedDays() {
        return reservedDays;
    }

    public List<LocalDate> getDisabledDays() {
        return this.disabledDays;
    }

    public PublicationsEnabledDays(){

        this.disabledDays = new LinkedList<>();
        this.reservedDays = new LinkedList<>();
        this.hoy = LocalDate.now();
    }

    public PublicationsEnabledDays(List<LocalDate> reservedDays, List<LocalDate> disabledDays) {
        this.disabledDays = disabledDays;
        this.reservedDays = reservedDays;
        this.hoy = LocalDate.now();
    }

    public void reserveDay(LocalDate dayOne) throws DayDisabledException, DayAlreadyReservedException {
        if (disabledDays.contains(dayOne)) {
            throw new DayDisabledException();
        }
        if (reservedDays.contains(dayOne)){
            throw new DayAlreadyReservedException();
        }
        this.reservedDays.add(dayOne);
    }

    public void setDisabled(LocalDate disabled) throws DayAlreadyDisabledException {
        if (this.disabledDays.contains(disabled)){
            throw new DayAlreadyDisabledException();
        }
        this.disabledDays.add(disabled);
    }

    public boolean canReserve(LocalDate dayOne) {
        return !this.reservedDays.contains(dayOne) && !this.disabledDays.contains(dayOne);
    }

    public void releaseDay(LocalDate date) throws DayNotReservedException {
        if (!this.reservedDays.contains(date)){
            throw new DayNotReservedException();
        }else {
            this.reservedDays.remove(date);
        }
    }

    public void reserveDays(List<LocalDate> reservationDays) throws DayAlreadyReservedException, DayDisabledException, InvalidAmountOfDaysToReserveException {
        if (reservationDays.size() > 4) {
            throw new InvalidAmountOfDaysToReserveException();
        }
        for (LocalDate localDate : reservationDays) {
            this.reserveDay(localDate);
        }
    }

    public void releaseDays(LinkedList<LocalDate> reservationDays) throws DayNotReservedException {
        for (LocalDate localDate : reservationDays) {
            this.releaseDay(localDate);
        }
    }

    public void disableDays(List<LocalDate> reservationDays) throws DayAlreadyDisabledException {
        for (LocalDate localDate : reservationDays) {
            this.setDisabled(localDate);
        }
    }

}

