package shop;


import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Random;

public class VirtualItemTest {
    @Test(groups = {"group_1"})
    void getSizeIsEqualToSet () {
        double r = new Random().nextDouble();
        VirtualItem virtualItem1 = new VirtualItem();
        virtualItem1.setSizeOnDisk(r);
        Assert.assertEquals(r, virtualItem1.getSizeOnDisk());
    }
}
