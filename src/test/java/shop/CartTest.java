package shop;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Random;


class CartTest {
    private Cart cart;
    private static final double TAX = 0.2;
    private String cartName;
    private RealItem realItem;
    private VirtualItem virtualItem;
    private double realItemPrice;
    private double virtualItemPrice;

    @BeforeTest
    void createCartAndItems() {
        cartName = RandomStringUtils.random(10, true, false);
        cart = new Cart(cartName);
        realItemPrice = new Random().nextDouble();
        virtualItemPrice = new Random().nextDouble();
        realItem = new RealItem();
        virtualItem = new VirtualItem();
        realItem.setPrice(realItemPrice);
        virtualItem.setPrice(virtualItemPrice);
        cart.addRealItem(realItem);
        cart.addVirtualItem(virtualItem);
        cart.showItems();
    }

    @Test(groups = {"group_1", "group_2"})
    void getCartNameIsEqualToPassedToConstructor() {
        Assert.assertEquals(cartName, cart.getCartName());
    }

    @Test(groups = {"group_2"})
    void getTotalPriceEqualToSumOfItemsPricesPlusTax() {
        Assert.assertEquals((
                (realItemPrice + realItemPrice*TAX) +
                (virtualItemPrice + virtualItemPrice*TAX)),
                cart.getTotalPrice()
        );
    }

    @Test(groups = {"group_2"})
    void checkTotalPriceAfterItemsRemoving() {
        cart.deleteRealItem(realItem);
        cart.deleteVirtualItem(virtualItem);
        Assert.assertEquals(0, cart.getTotalPrice()
        );
    }

}
