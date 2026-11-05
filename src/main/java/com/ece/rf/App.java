
package com.ece.rf;

import java.util.Scanner;

public class App {

    // Calculate Free Space Path Loss (FSPL)
    // Frequency is in MHz and distance is in km.
    public static double calculateFSPL(
            double frequencyMHz, double distanceKm) {

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

        double txPower;
        double txGain;
        double rxGain;
        double frequency;
        double distance;
        double cableLoss;
        double systemLoss;
        double sensitivity;

        System.out.println("======================================");
        System.out.println("       RF LINK BUDGET CALCULATOR");
        System.out.println("======================================");

        // Jenkins mode: receive 8 command-line arguments
        if (args.length == 8) {

            txPower = Double.parseDouble(args[0]);
            txGain = Double.parseDouble(args[1]);
            rxGain = Double.parseDouble(args[2]);
            frequency = Double.parseDouble(args[3]);
            distance = Double.parseDouble(args[4]);
            cableLoss = Double.parseDouble(args[5]);
            systemLoss = Double.parseDouble(args[6]);
            sensitivity = Double.parseDouble(args[7]);

            System.out.println("Running in Jenkins Parameter Mode");

        } else {

            // Manual console mode
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter Transmitter Power (dBm): ");
            txPower = scanner.nextDouble();

            System.out.print("Enter Transmitter Antenna Gain (dBi): ");
            txGain = scanner.nextDouble();

            System.out.print("Enter Receiver Antenna Gain (dBi): ");
            rxGain = scanner.nextDouble();

            System.out.print("Enter Frequency (MHz): ");
            frequency = scanner.nextDouble();

            System.out.print("Enter Distance (km): ");
            distance = scanner.nextDouble();

            System.out.print("Enter Cable Loss (dB): ");
            cableLoss = scanner.nextDouble();

            System.out.print("Enter System Loss (dB): ");
            systemLoss = scanner.nextDouble();

            System.out.print("Enter Receiver Sensitivity (dBm): ");
            sensitivity = scanner.nextDouble();

            scanner.close();
        }

        try {

            double fspl = calculateFSPL(frequency, distance);

            double receivedPower = calculateReceivedPower(
                    txPower,
                    txGain,
                    rxGain,
                    fspl,
                    cableLoss,
                    systemLoss);

            double linkMargin = calculateLinkMargin(
                    receivedPower, sensitivity);

            System.out.println("\n========== CALCULATION RESULTS ==========");

            System.out.printf("Transmitter Power     : %.2f dBm%n", txPower);
            System.out.printf("Frequency             : %.2f MHz%n", frequency);
            System.out.printf("Distance              : %.2f km%n", distance);
            System.out.printf("Free Space Path Loss  : %.2f dB%n", fspl);
            System.out.printf("Received Power        : %.2f dBm%n", receivedPower);
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
    }
}
