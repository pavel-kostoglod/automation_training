package shop;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Random;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CartTest {
    private Cart cart;
    private static final double TAX = 0.2;
    private String cartName;
    private RealItem realItem;
    private VirtualItem virtualItem;
    private double realItemPrice;
    private double virtualItemPrice;

    @BeforeAll
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

    @Test
    void getCartNameIsEqualToPassedToConstructor() {
        Assertions.assertEquals(cartName, cart.getCartName());
    }

    @Test
    void getTotalPriceEqualToSumOfItemsPricesPlusTax() {
        Assertions.assertEquals((
                (realItemPrice + realItemPrice*TAX) +
                (virtualItemPrice + virtualItemPrice*TAX)),
                cart.getTotalPrice()
        );
    }

    @Test
    void checkTotalPriceAfterItemsRemoving() {
        cart.deleteRealItem(realItem);
        cart.deleteVirtualItem(virtualItem);
        Assertions.assertEquals((
                (realItemPrice + realItemPrice*TAX) +
                (virtualItemPrice + virtualItemPrice*TAX)),
                cart.getTotalPrice()
        );
    }

}
