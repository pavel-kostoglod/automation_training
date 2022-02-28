package shop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class RealItemTest {
    @Test
    void getWeightIsEqualToSet () {
        double r = new Random().nextDouble();
        RealItem realItem = new RealItem();
        realItem.setWeight(r);
        Assertions.assertEquals(r, realItem.getWeight());
    }
}
