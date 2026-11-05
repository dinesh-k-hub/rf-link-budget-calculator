
package com.ece.rf;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RfController {

    @PostMapping("/calculate")
    public Map<String, Object> calculate(@RequestBody Map<String, Double> input) {

        double txPower = get(input, "txPower");
        double txGain = get(input, "txGain");
        double rxGain = get(input, "rxGain");
        double frequency = get(input, "frequency");
        double distance = get(input, "distance");
        double cableLoss = get(input, "cableLoss");
        double systemLoss = get(input, "systemLoss");
        double sensitivity = get(input, "sensitivity");

        if (frequency <= 0 || distance <= 0) {
            throw new IllegalArgumentException(
                "Frequency and distance must be greater than zero."
            );
        }

        double fspl = App.calculateFSPL(frequency, distance);

        double receivedPower = App.calculateReceivedPower(
            txPower, txGain, rxGain, fspl, cableLoss, systemLoss
        );

        double linkMargin = App.calculateLinkMargin(
            receivedPower, sensitivity
        );

        return Map.of(
            "fspl", fspl,
            "receivedPower", receivedPower,
            "linkMargin", linkMargin,
            "linkAvailable", linkMargin >= 0
        );
    }

    private double get(Map<String, Double> input, String key) {

        Double value = input.get(key);

        if (value == null || !Double.isFinite(value)) {
            throw new IllegalArgumentException(
                "Invalid or missing value: " + key
            );
        }

        return value;
    }
}
