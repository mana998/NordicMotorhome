package com.example.demo.Model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.valueOf;
import static java.time.temporal.ChronoUnit.DAYS;

public class Agreement {

    // prices are in euros
    final double freeKmPerDay = 400.00;
    final double transferCostPerKm = 0.70;
    final double tankCharge = 70.00;
    final double pricePerExtraKm = 1.00;
    // cancellation costs
    final int maxDaysPriorToRental = 50;
    final int minDaysPriorToRental = 15;
    final double percentageMaximumDays = 0.20;
    final double minimumCancellationCost = 200;
    final double percentageMinimumDays = 0.50;
    final double percentageLessThanMinimumDays = 0.80;
    final double percentageSameDay = 0.95;
    final List<String> highSeason = Arrays.asList("JUNE", "JULY", "AUGUST");
    final List<String> shoulderSeason = Arrays.asList("SEPTEMBER", "OCTOBER", "APRIL", "MAY");

    private int id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate; // dates are important!!!
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
    private Vehicle vehicle; // either plate or name or both
    private Renter renter; // last name and first letter of first name
    private double drivenKm;
    private double pickUpPoint;
    private double dropOffPoint;
    private List<Item> items;
    private boolean levelGasoline;
    private boolean isCancelled;
    // prices
    private double totalCost; // also important!!!
    private double extraKmCost; // the number of km that exceed the free km per day
    private double vehicleCost; // days * price per day for the given vehicle
    private double itemsCost; // total cost of items included
    private double transferCost; // cost for transferring the vehicle to wished location
    private double tankCost; // cost added if the tank is less than half full at drop-off
    // cancellation
    private double cancellationCost;

    public Agreement() {}

    public Agreement(int id, LocalDate startDate, LocalDate endDate, Vehicle vehicle, Renter renter, double drivenKm, double pickUpPoint, double dropOffPoint, List<Item> items, boolean levelGasoline, boolean isCancelled, double totalCost, double extraKmCost, double vehicleCost, double itemsCost, double transferCost, double tankCost, double cancellationCost) {
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
        this.totalCost = totalCost;
        this.extraKmCost = extraKmCost;
        this.vehicleCost = vehicleCost;
        this.itemsCost = itemsCost;
        this.transferCost = transferCost;
        this.tankCost = tankCost;
        this.cancellationCost = cancellationCost;
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

    public double getTotalCost() {
        return totalCost;
    }

    public double getExtraKmCost() {
        return extraKmCost;
    }

    public double getVehicleCost() {
        return vehicleCost;
    }

    public double getItemsCost() {
        return itemsCost;
    }

    public double getTransferCost() {
        return transferCost;
    }

    public double getTankCost() {
        return tankCost;
    }

    public double getCancellationCost() {
        return cancellationCost;
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

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public void setExtraKmCost(double extraKmCost) {
        this.extraKmCost = extraKmCost;
    }

    public void setVehicleCost(double vehicleCost) {
        this.vehicleCost = vehicleCost;
    }

    public void setItemsCost(double itemsCost) {
        this.itemsCost = itemsCost;
    }

    public void setTransferCost(double transferCost) {
        this.transferCost = transferCost;
    }

    public void setTankCost(double tankCost) {
        this.tankCost = tankCost;
    }

    public void setCancellationCost(double cancellationCost) {
        this.cancellationCost = cancellationCost;
    }

    public double calculateTotalCost() {
        return calculateExtraKmCost() + calculateVehicleCost() + calculateItemsCost() + calculateTransferCost() + calculateTankCost();
    }

    public double calculateExtraKmCost() {
        int days = findDifferenceInDays(startDate, endDate);
        double extraKm = drivenKm - (days * freeKmPerDay);
        extraKmCost = extraKm * pricePerExtraKm;
        if (extraKmCost > 0) {
            return extraKmCost;
        } else {
            return 0;
        }
    }

    public double calculateVehicleCost() {
        int daysBetween = findDifferenceInDays(startDate, endDate);
        double price = vehicle.getPrice();
        String month = valueOf(startDate.getMonth());
        if (highSeason.contains(month)) {
            price *= 0.5;
        } else if (shoulderSeason.contains(month)) {
            price *= 0.2;
        }
        return daysBetween * price;
    }

    public double calculateItemsCost() {
        double result = 0;
        for (Item item : items) {
            result += item.getPrice();
        }
        return result;
    }

    public double calculateTransferCost() {
        if (pickUpPoint != 0 || dropOffPoint != 0) {
            return pickUpPoint * transferCostPerKm + dropOffPoint * transferCostPerKm;
        }
        return 0;
    }

    public double calculateTankCost() {
        if (!levelGasoline) {
            return tankCharge;
        }
        return 0;
    }

    public double calculateCancellationCost() {
        LocalDate currentDate = LocalDate.now();
        int daysDifference = findDifferenceInDays(currentDate, startDate);
        if (daysDifference >= maxDaysPriorToRental) {
            cancellationCost = totalCost * percentageMaximumDays;
            if (cancellationCost < minimumCancellationCost) {
                return minimumCancellationCost;
            }
            return cancellationCost;
        } else if (daysDifference >= minDaysPriorToRental) {
            return totalCost * percentageMinimumDays;
        } else if (daysDifference >= 1) {
            return totalCost * percentageLessThanMinimumDays;
        } else {
            return totalCost * percentageSameDay;
        }
    }

    public int findDifferenceInDays(LocalDate dateBefore, LocalDate dateAfter) {
        return  (int) DAYS.between(dateBefore, dateAfter);
    }

    @Override
    public String toString() {
        return "Agreement{" +
                "freeKmPerDay=" + freeKmPerDay +
                ", transferCostPerKm=" + transferCostPerKm +
                ", tankCharge=" + tankCharge +
                ", pricePerExtraKm=" + pricePerExtraKm +
                ", maxDaysPriorToRental=" + maxDaysPriorToRental +
                ", minDaysPriorToRental=" + minDaysPriorToRental +
                ", percentageMaximumDays=" + percentageMaximumDays +
                ", minimumCancellationCost=" + minimumCancellationCost +
                ", percentageMinimumDays=" + percentageMinimumDays +
                ", percentageLessThanMinimumDays=" + percentageLessThanMinimumDays +
                ", percentageSameDay=" + percentageSameDay +
                ", id=" + id +
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
                ", totalCost=" + totalCost +
                ", extraKmCost=" + extraKmCost +
                ", vehicleCost=" + vehicleCost +
                ", itemsCost=" + itemsCost +
                ", transferCost=" + transferCost +
                ", tankCost=" + tankCost +
                ", cancellationCost=" + cancellationCost +
                '}';
    }
}
