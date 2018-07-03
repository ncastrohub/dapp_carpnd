package model;

import model.exceptions.*;
import org.junit.Test;
import utils.builders.PublicationBuilder;

import org.joda.time.LocalDate;
import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

//import static org.junit.jupiter.api.Assertions.*;


public class TestPublication {

    @Test
    public void testPublicationCreation(){

        User owner = mock(User.class);
        MoneyAndAmountForPublication pricePerHour = mock(MoneyAndAmountForPublication.class);
        Vehicle car = mock(Vehicle.class);
        AdressLocation acquirePlace = mock(AdressLocation.class);

        LinkedList<AdressLocation> returnLocations = new LinkedList<>();
        returnLocations.add(mock(AdressLocation.class));
        returnLocations.add(mock(AdressLocation.class));
        returnLocations.add(mock(AdressLocation.class));
        returnLocations.add(mock(AdressLocation.class));

        PublicationsEnabledDays availableDays = mock(PublicationsEnabledDays.class);

        Publication publication = PublicationBuilder.start()
                .withOwner(owner)
                .withPricePerHour(pricePerHour)
                .withVehicle(car)
                .withAcquireLocation(acquirePlace)
                .withRestoreLocations(returnLocations)
                .withAvaibleDays(availableDays)
                .build();

        assertThat(publication.getOwner()).isEqualTo(owner);
        assertThat(publication.getCostPerHour()).isEqualTo(pricePerHour);
        assertThat(publication.getVehicle()).isEqualTo(car);
        assertThat(publication.getAcquireLocation()).isEqualTo(acquirePlace);
        assertThat(publication.getReturnLocations()).isEqualTo(returnLocations);
        assertThat(publication.getAvaiblesDays()).isEqualTo(availableDays);

    }


    @Test
    public void testWhenReserveVehicleForSomeDaysAPublicationReservedIsCreated() throws DayAlreadyReservedException, DayDisabledException, InvalidAmountOfDaysToReserveException, NotEnoughCreditException {

        User owner = mock(User.class);
        User customer = mock(User.class);

        MoneyAndAmountForPublication pricePerHour = mock(MoneyAndAmountForPublication.class);
        Vehicle car = mock(Vehicle.class);
        AdressLocation acquirePlace = mock(AdressLocation.class);

        LinkedList<AdressLocation> returnLocations = new LinkedList<>();
        AdressLocation returnLocation = mock(AdressLocation.class);

        returnLocations.add(returnLocation);
        returnLocations.add(mock(AdressLocation.class));
        returnLocations.add(mock(AdressLocation.class));
        returnLocations.add(mock(AdressLocation.class));

        PublicationsEnabledDays availableDays = mock(PublicationsEnabledDays.class);

        Publication publication = PublicationBuilder.start()
                .withOwner(owner)
                .withPricePerHour(pricePerHour)
                .withVehicle(car)
                .withAcquireLocation(acquirePlace)
                .withRestoreLocations(returnLocations)
                .withAvaibleDays(availableDays)
                .build();

        LinkedList<LocalDate> reservationDays = new LinkedList<>();
        LocalDate dayOne = LocalDate.now().plusDays(1);
        LocalDate dayTwo = LocalDate.now().plusDays(2);
        LocalDate dayThree = LocalDate.now().plusDays(3);
        reservationDays.add(dayOne);
        reservationDays.add(dayTwo);
        reservationDays.add(dayThree);

        Reservation reservation = publication.makeReservation(customer, reservationDays, returnLocation);
        assertThat(reservation.getPublicationSnapshot().publication.getId()).isEqualTo(publication.getId());
    }

    @Test
    public void testWhenPublicationIsReservedDaysReservedAreNotAvailable() throws DayAlreadyReservedException, DayDisabledException, InvalidAmountOfDaysToReserveException, NotEnoughCreditException {
        User owner = mock(User.class);
        User customer = mock(User.class);

        MoneyAndAmountForPublication pricePerHour = mock(MoneyAndAmountForPublication.class);
        Vehicle car = mock(Vehicle.class);
        AdressLocation acquirePlace = mock(AdressLocation.class);

        LinkedList<AdressLocation> returnLocations = new LinkedList<>();
        AdressLocation returnLocation = mock(AdressLocation.class);

        returnLocations.add(returnLocation);
        returnLocations.add(mock(AdressLocation.class));
        returnLocations.add(mock(AdressLocation.class));
        returnLocations.add(mock(AdressLocation.class));

        PublicationsEnabledDays availableDays = new PublicationsEnabledDays();

        Publication publication = PublicationBuilder.start()
                .withOwner(owner)
                .withPricePerHour(pricePerHour)
                .withVehicle(car)
                .withAcquireLocation(acquirePlace)
                .withRestoreLocations(returnLocations)
                .withAvaibleDays(availableDays)
                .build();

        LinkedList<LocalDate> reservationDays = new LinkedList<>();
        LocalDate dayOne = LocalDate.now().plusDays(1);
        LocalDate dayTwo = LocalDate.now().plusDays(2);
        LocalDate dayThree = LocalDate.now().plusDays(3);
        reservationDays.add(dayOne);
        reservationDays.add(dayTwo);
        reservationDays.add(dayThree);
        Publication spied = spy(publication);

        doNothing().when(spied).canPay(customer.availableMoney, reservationDays.size());
        Reservation reservation = publication.makeReservation(customer, reservationDays, returnLocation);

        assertThat(publication.canReserve(dayOne)).isFalse();
        assertThat(publication.canReserve(dayTwo)).isFalse();
        assertThat(publication.canReserve(dayThree)).isFalse();

    }


    @Test
    public void testWhenReservedDaysAreReleasedThatDaysCanBeReservedAgain() throws DayAlreadyReservedException, DayDisabledException, DayNotReservedException, InvalidAmountOfDaysToReserveException, NotEnoughCreditException {
        User owner = mock(User.class);
        User customer = mock(User.class);

        MoneyAndAmountForPublication pricePerHour = mock(MoneyAndAmountForPublication.class);
        Vehicle car = mock(Vehicle.class);
        AdressLocation acquirePlace = mock(AdressLocation.class);

        LinkedList<AdressLocation> returnLocations = new LinkedList<>();
        AdressLocation returnLocation = mock(AdressLocation.class);

        returnLocations.add(returnLocation);
        returnLocations.add(mock(AdressLocation.class));
        returnLocations.add(mock(AdressLocation.class));
        returnLocations.add(mock(AdressLocation.class));

        PublicationsEnabledDays availableDays = new PublicationsEnabledDays();

        Publication publication = PublicationBuilder.start()
                .withOwner(owner)
                .withPricePerHour(pricePerHour)
                .withVehicle(car)
                .withAcquireLocation(acquirePlace)
                .withRestoreLocations(returnLocations)
                .withAvaibleDays(availableDays)
                .build();

        LinkedList<LocalDate> reservationDays = new LinkedList<>();
        LocalDate dayOne = LocalDate.now().plusDays(1);
        LocalDate dayTwo = LocalDate.now().plusDays(2);
        LocalDate dayThree = LocalDate.now().plusDays(3);
        reservationDays.add(dayOne);
        reservationDays.add(dayTwo);
        reservationDays.add(dayThree);

        Reservation reservation = publication.makeReservation(customer, reservationDays, returnLocation);

        publication.releaseDays(reservationDays);

        assertThat(publication.canReserve(dayOne)).isTrue();
        assertThat(publication.canReserve(dayTwo)).isTrue();
        assertThat(publication.canReserve(dayThree)).isTrue();


    }

    @Test
    public void testAVehicleCannotBeReservedFormMoreThanFiveDaysAndLessThanOne() {
        User owner = mock(User.class);
        User customer = mock(User.class);

        MoneyAndAmountForPublication pricePerHour = mock(MoneyAndAmountForPublication.class);
        Vehicle car = mock(Vehicle.class);
        AdressLocation acquirePlace = mock(AdressLocation.class);

        LinkedList<AdressLocation> returnLocations = new LinkedList<>();
        AdressLocation returnLocation = mock(AdressLocation.class);

        returnLocations.add(returnLocation);
        returnLocations.add(mock(AdressLocation.class));
        returnLocations.add(mock(AdressLocation.class));
        returnLocations.add(mock(AdressLocation.class));

        PublicationsEnabledDays availableDays = new PublicationsEnabledDays();

        Publication publication = PublicationBuilder.start()
                .withOwner(owner)
                .withPricePerHour(pricePerHour)
                .withVehicle(car)
                .withAcquireLocation(acquirePlace)
                .withRestoreLocations(returnLocations)
                .withAvaibleDays(availableDays)
                .build();

        LinkedList<LocalDate> reservationDays = new LinkedList<>();
        LocalDate dayOne = LocalDate.now().plusDays(1);
        LocalDate dayTwo = LocalDate.now().plusDays(2);
        LocalDate dayThree = LocalDate.now().plusDays(3);
        LocalDate dayFour = LocalDate.now().plusDays(4);
        LocalDate dayFive = LocalDate.now().plusDays(5);
        LocalDate daySix = LocalDate.now().plusDays(6);

        reservationDays.add(dayOne);
        reservationDays.add(dayTwo);
        reservationDays.add(dayThree);
        reservationDays.add(dayFour);
        reservationDays.add(dayFive);
        reservationDays.add(daySix);


        assertThrows(InvalidAmountOfDaysToReserveException.class, ()-> publication.makeReservation(customer, reservationDays, returnLocation));

    }

    @Test
    public void testAsOwnerIWantToDisabledSomeDaysToCannotBeReserved() throws DayAlreadyDisabledException {
        User owner = mock(User.class);
        User customer = mock(User.class);

        MoneyAndAmountForPublication pricePerHour = mock(MoneyAndAmountForPublication.class);
        Vehicle car = mock(Vehicle.class);
        AdressLocation acquirePlace = mock(AdressLocation.class);

        LinkedList<AdressLocation> returnLocations = new LinkedList<>();
        AdressLocation returnLocation = mock(AdressLocation.class);

        returnLocations.add(returnLocation);
        returnLocations.add(mock(AdressLocation.class));
        returnLocations.add(mock(AdressLocation.class));
        returnLocations.add(mock(AdressLocation.class));

        PublicationsEnabledDays availableDays = new PublicationsEnabledDays();

        Publication publication = PublicationBuilder.start()
                .withOwner(owner)
                .withPricePerHour(pricePerHour)
                .withVehicle(car)
                .withAcquireLocation(acquirePlace)
                .withRestoreLocations(returnLocations)
                .withAvaibleDays(availableDays)
                .build();

        LinkedList<LocalDate> reservationDays = new LinkedList<>();

        LocalDate dayOne = LocalDate.now().plusDays(1);
        LocalDate dayTwo = LocalDate.now().plusDays(2);
        LocalDate dayThree = LocalDate.now().plusDays(3);
        reservationDays.add(dayOne);
        reservationDays.add(dayTwo);
        reservationDays.add(dayThree);


        publication.disabledDays(reservationDays);

        assertThrows(DayDisabledException.class, ()-> publication.makeReservation(customer, reservationDays, returnLocation));
    }

    /**/


}
