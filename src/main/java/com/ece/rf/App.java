
package com.ece.rf;

import java.util.Scanner;

public class App {

    // Calculate Free Space Path Loss (FSPL)
    // Frequency is in MHz and distance is in km.
    public static double calculateFSPL(double frequencyMHz, double distanceKm) {

        if (frequencyMHz <= 0 || distanceKm <= 0) {
            throw new IllegalArgumentException(
                    "Frequency and distance must be greater than zero.");
        }

        return 32.44
                + (20 * Math.log10(frequencyMHz))
                + (20 * Math.log10(distanceKm));
    }

    // Calculate received signal power in dBm
    public static double calculateReceivedPower(
            double transmitPower,
            double transmitGain,
            double receiveGain,
            double fspl,
            double cableLoss,
            double systemLoss) {

        return transmitPower
                + transmitGain
                + receiveGain
                - fspl
                - cableLoss
                - systemLoss;
    }

    // Calculate link margin in dB
    public static double calculateLinkMargin(
            double receivedPower,
            double receiverSensitivity) {

        return receivedPower - receiverSensitivity;
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("======================================");
        System.out.println("       RF LINK BUDGET CALCULATOR");
        System.out.println("======================================");

        System.out.print("Enter Transmitter Power (dBm): ");
        double txPower = scanner.nextDouble();

        System.out.print("Enter Transmitter Antenna Gain (dBi): ");
        double txGain = scanner.nextDouble();

        System.out.print("Enter Receiver Antenna Gain (dBi): ");
        double rxGain = scanner.nextDouble();

        System.out.print("Enter Frequency (MHz): ");
        double frequency = scanner.nextDouble();

        System.out.print("Enter Distance (km): ");
        double distance = scanner.nextDouble();

        System.out.print("Enter Cable Loss (dB): ");
        double cableLoss = scanner.nextDouble();

        System.out.print("Enter System Loss (dB): ");
        double systemLoss = scanner.nextDouble();

        System.out.print("Enter Receiver Sensitivity (dBm): ");
        double sensitivity = scanner.nextDouble();

        try {
            double fspl = calculateFSPL(frequency, distance);

            double receivedPower = calculateReceivedPower(
                    txPower, txGain, rxGain,
                    fspl, cableLoss, systemLoss);

            double linkMargin = calculateLinkMargin(
                    receivedPower, sensitivity);

            System.out.println("\n========== CALCULATION RESULTS ==========");

            System.out.printf("Free Space Path Loss : %.2f dB%n", fspl);
            System.out.printf("Received Power       : %.2f dBm%n", receivedPower);
            System.out.printf("Link Margin           : %.2f dB%n", linkMargin);

            if (linkMargin > 0) {
                System.out.println("Link Status           : LINK AVAILABLE");
            } else if (linkMargin == 0) {
                System.out.println("Link Status           : MARGIN IS ZERO");
            } else {
                System.out.println("Link Status           : LINK NOT AVAILABLE");
            }

            System.out.println("==========================================");

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }
}
