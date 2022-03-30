package shop;


import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Random;

public class RealItemTest {
    @Test(groups = {"group_2"})
    void getWeightIsEqualToSet () {
        double r = new Random().nextDouble();
        RealItem realItem = new RealItem();
        realItem.setWeight(r);
        Assert.assertEquals(r, realItem.getWeight());
    }
}
