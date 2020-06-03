package com.example.demo.Model;

import org.decimal4j.util.DoubleRounder;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static java.lang.String.valueOf;
import static java.time.temporal.ChronoUnit.DAYS;

public class Agreement { // Dimitrios

    // prices are in euros
    final double transferFeePerKm = 0.70;
    final double fuelFee = 70.00;
    final double freeKmPerDay = 400.00;
    final double pricePerExtraKm = 1.00;
    // cancellation costs
    final double minimumCancellationCost = 200.00;
    final int maxDaysPriorToRental = 50;
    final double percentageMaxDays = 0.20;
    final int minDaysPriorToRental = 15;
    final double percentageMinDays = 0.50;
    final double percentageLessThanMinDays = 0.80;
    final double percentageSameDay = 0.95;
    // high season and middle season months
    final List<String> highSeason = Arrays.asList("JUNE", "JULY", "AUGUST");
    final List<String> middleSeason = Arrays.asList("SEPTEMBER", "OCTOBER", "APRIL", "MAY");

    private int id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
    private Vehicle vehicle;
    private Renter renter;
    private double drivenKm; // km driven by the renter
    private double pickUpPoint;
    private double dropOffPoint;
    private List<Item> items;
    private boolean levelGasoline;
    private boolean isCancelled;

    public Agreement() {}

    public Agreement(int id, LocalDate startDate, LocalDate endDate, Vehicle vehicle, Renter renter, double drivenKm,
                     double pickUpPoint, double dropOffPoint, List<Item> items, boolean levelGasoline, boolean isCancelled) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.vehicle = vehicle;
        this.renter = renter;
        this.drivenKm = drivenKm;
        this.pickUpPoint = pickUpPoint;
        this.dropOffPoint = dropOffPoint;
        this.items = items;
        this.levelGasoline = levelGasoline;
        this.isCancelled = isCancelled;
    }

    public int getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Renter getRenter() {
        return renter;
    }

    public double getDrivenKm() {
        return drivenKm;
    }

    public double getPickUpPoint() {
        return pickUpPoint;
    }

    public double getDropOffPoint() {
        return dropOffPoint;
    }

    public List<Item> getItems() {
        return items;
    }

    public boolean isLevelGasoline() {
        return levelGasoline;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setRenter(Renter renter) {
        this.renter = renter;
    }

    public void setDrivenKm(double drivenKm) {
        this.drivenKm = drivenKm;
    }

    public void setPickUpPoint(double pickUpPoint) {
        this.pickUpPoint = pickUpPoint;
    }

    public void setDropOffPoint(double dropOffPoint) {
        this.dropOffPoint = dropOffPoint;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setLevelGasoline(boolean levelGasoline) {
        this.levelGasoline = levelGasoline;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public double calculateTotalPrice() {
      return calculateExtraKmFee() + calculateVehicleFee() + calculateItemsFee() + calculateTransferFee()
                    + calculateFuelFee();
    }

    public double calculateInvoiceTotal() {
        if (isCancelled) {
            return calculateCancellationFee();
        } else {
            return calculateTotalPrice();
        }
    }

    public double calculateExtraKmFee() {
        int days = findDifferenceInDays(startDate, endDate);
        if (days==0) {
            days=1;
        }
        double extraKm = drivenKm - (days * freeKmPerDay);
        double extraKmCost;
        extraKmCost = extraKm * pricePerExtraKm;
        if (extraKmCost > 0) {
            return extraKmCost;
        } else {
            return 0;
        }
    }

    public double calculateVehicleFee() {
        int daysBetween = findDifferenceInDays(startDate, endDate);
        double price = vehicle.getPrice();
        String month = valueOf(startDate.getMonth());
        if (highSeason.contains(month)) {
            price *= 1.6;
        } else if (middleSeason.contains(month)) {
            price *= 1.3;
        }
        if (daysBetween == 0) {
            daysBetween = 1;
        }
        return daysBetween * price;
    }

    public double calculateItemsFee() {
        double result = 0;
        for (Item item : items) {
            result += item.getPrice();
        }
        return result;
    }

    public double calculateTransferFee() {
        if (pickUpPoint != 0 || dropOffPoint != 0) {
            double result = (pickUpPoint * transferFeePerKm) + (dropOffPoint * transferFeePerKm);
            return DoubleRounder.round(result, 2);
        }
        return 0;
    }

    public double calculateFuelFee() {
        // if level of gasoline in the tank is more than half full, then no charge
        if (levelGasoline) {
            return 0.0;
            // else, charge the renter
        } else {
            return fuelFee;
        }
    }

    public int findDifferenceInDays(LocalDate dateBefore, LocalDate dateAfter) {
        return  (int) DAYS.between(dateBefore, dateAfter);
    }

    public double calculateCancellationFee() {
        double cancellationFee = 0;
        LocalDate currentDate = LocalDate.now();
        int daysDifference = findDifferenceInDays(currentDate, startDate);
        if (daysDifference >= maxDaysPriorToRental) {
            cancellationFee = calculateTotalPrice() * percentageMaxDays;
            if (cancellationFee < minimumCancellationCost) {
                return minimumCancellationCost;
            }
            return cancellationFee;
        } else if (daysDifference >= minDaysPriorToRental) {
            return DoubleRounder.round(calculateTotalPrice() * percentageMinDays, 2);
        } else if (daysDifference >= 1) {
            return DoubleRounder.round(calculateTotalPrice() * percentageLessThanMinDays, 2);
        } else {
            return DoubleRounder.round(calculateTotalPrice() * percentageSameDay, 2);
        }
    }

    public boolean canBeCancelled(LocalDate startDate) {
        LocalDate currentDate = LocalDate.now();
        return findDifferenceInDays(currentDate, startDate) >= 0 && drivenKm==0;
    }

    @Override
    public String toString() {
        return "Agreement{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", vehicle=" + vehicle +
                ", renter=" + renter +
                ", drivenKm=" + drivenKm +
                ", pickUpPoint=" + pickUpPoint +
                ", dropOffPoint=" + dropOffPoint +
                ", items=" + items +
                ", levelGasoline=" + levelGasoline +
                ", isCancelled=" + isCancelled +
                '}';
    }
}
