
package com.ece.rf;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    void testCalculateFSPL() {
        double result = App.calculateFSPL(2400, 1);
        assertEquals(100.04, result, 0.01);
    }

    @Test
    void testCalculateReceivedPower() {
        double result = App.calculateReceivedPower(
                20, 2, 2, 100.04, 1, 2);

        assertEquals(-79.04, result, 0.01);
    }

    @Test
    void testCalculateLinkMargin() {
        double result = App.calculateLinkMargin(-79.04, -90);

        assertEquals(10.96, result, 0.01);
    }

    @Test
    void testInvalidFrequency() {
        assertThrows(
                IllegalArgumentException.class,
                () -> App.calculateFSPL(0, 1)
        );
    }
}
