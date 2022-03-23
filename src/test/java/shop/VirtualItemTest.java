package shop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class VirtualItemTest {
    @Test
    void getSizeIsEqualToSet () {
        double r = new Random().nextDouble();
        VirtualItem virtualItem1 = new VirtualItem();
        virtualItem1.setSizeOnDisk(r);
        Assertions.assertEquals(r, virtualItem1.getSizeOnDisk());
    }
}
