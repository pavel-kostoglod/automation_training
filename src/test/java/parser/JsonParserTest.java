package parser;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

    @BeforeEach
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

    @Test
    void writeToFileWritesCorrectCartInfo() {
        Assertions.assertEquals(Utils.readFromFile(file), String.format(
                "{\"cartName\":\"%s\",\"realItems\":[{\"weight\":%.1f,\"name\":\"%s\",\"price\":%.2f}]," +
                        "\"virtualItems\":[{\"sizeOnDisk\":%.2f,\"name\":\"%s\",\"price\":%.2f}],\"total\":%.3f}",
                cartName, ri.getWeight(), ri.getName(), ri.getPrice(),
                vi.getSizeOnDisk(), vi.getName(), vi.getPrice(), cart.getTotalPrice()
        ));
    }

    @Test
    void readFromFileReadsCorrectCartInfo() {
        Cart cart2 = jsonParser.readFromFile(file2);
        Assertions.assertAll(
                () -> Assertions.assertEquals(cart2.getCartName(), cart.getCartName()),
                () -> Assertions.assertEquals(cart2.getTotalPrice(), cart.getTotalPrice())
        );
    }


    @Test
    @Disabled
    void writeToFileCreatesNewFile() {
        Assertions.assertTrue(file.exists());
    }

    // Exception test. Parametrized
    @ParameterizedTest
    @ValueSource(strings = {
            "src/main/resources/nonexistent.json",
            "src/main/resources/nonexistent.txt",
            "src/main/nonexistent.json",
            "src/nonexistent.json",
            "nonexistent.json"
    })
    void noSuchFileExceptionThrownIfNonexistentFilenameProvided (String filePath) {
        Assertions.assertThrows(NoSuchFileException.class,
                () -> jsonParser.readFromFile(new File(filePath))
        );
    }

    @AfterEach
    void cleanUp() {
        file.delete();
        file2.delete();
    }
}
