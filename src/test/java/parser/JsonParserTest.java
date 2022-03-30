package parser;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import shop.Cart;
import shop.RealItem;
import shop.VirtualItem;
import utils.Utils;

import java.io.*;

public class JsonParserTest {
    private JsonParser jsonParser;
    private Cart cart;
    private String cartName;
    private File file;
    private File file2;
    private RealItem ri;
    private VirtualItem vi;

    @BeforeTest(groups = {"setup"})
    void prepare() {
        jsonParser = new JsonParser();
        cartName = RandomStringUtils.random(10, true, false);
        cart = new Cart(cartName);

        ri = new RealItem();
        ri.setName("RealItem");
        ri.setPrice(10.75);
        ri.setWeight(5.20);

        vi = new VirtualItem();
        vi.setName("VirtualItem");
        vi.setPrice(30.43);
        vi.setSizeOnDisk(53.98);

        cart.addRealItem(ri);
        cart.addVirtualItem(vi);

        jsonParser.writeToFile(cart);
        file = new File("src/main/resources/" + cartName + ".json");

        Utils.writeCartToFile(cart);
        file2 = new File("src/main/resources/" + cart.getCartName() + "_util.json");
    }

    @Test(groups = {"group_1"})
    void writeToFileWritesCorrectCartInfo() {
        Assert.assertEquals(Utils.readFromFile(file), String.format(
                "{\"cartName\":\"%s\",\"realItems\":[{\"weight\":%.1f,\"name\":\"%s\",\"price\":%.2f}]," +
                        "\"virtualItems\":[{\"sizeOnDisk\":%.2f,\"name\":\"%s\",\"price\":%.2f}],\"total\":%.3f}",
                cartName, ri.getWeight(), ri.getName(), ri.getPrice(),
                vi.getSizeOnDisk(), vi.getName(), vi.getPrice(), cart.getTotalPrice()
        ));
    }

    @Test(groups = {"group_1"})
    void readFromFileReadsCorrectCartInfo() {
        Cart cart2 = jsonParser.readFromFile(file2);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(cart2.getCartName(), cart.getCartName());
        softAssert.assertEquals(cart2.getTotalPrice(), cart.getTotalPrice());
    }


    @Test(groups = {"group_1", "group_2"}, enabled = false)
    void writeToFileCreatesNewFile() {
        Assert.assertTrue(file.exists());
    }

    @Test(groups = {"group_2"}, dataProvider = "filePathProvider")
    void noSuchFileExceptionThrownIfNonexistentFilenameProvided (String filePath) {
        Assert.assertThrows(NoSuchFileException.class,
                () -> jsonParser.readFromFile(new File(filePath))
        );
    }

    @DataProvider(name = "filePathProvider")
    public static Object[][] filePath() {
        return new Object[][] {{"src/main/resources/nonexistent.json"},
                                {"src/main/resources/nonexistent.txt"},
                                {"src/main/nonexistent.json"},
                                {"src/nonexistent.json"},
                                {"nonexistent.json"}};
    }

    @AfterTest(groups = {"teardown"})
    void cleanUp() {
        file.delete();
        file2.delete();
    }
}
