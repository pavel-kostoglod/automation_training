package parser;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import shop.Cart;
import shop.RealItem;
import shop.VirtualItem;

import java.io.*;
import java.lang.reflect.Field;
import java.util.List;

public class JsonParserTest {
    private JsonParser jsonParser;
    private Cart cart;
    private String cartName;
    private File file;
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
    }

    @Test
    void writeToFileWritesCorrectCartInfo() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Assertions.assertEquals(reader.readLine(), String.format(
                    "{\"cartName\":\"%s\",\"realItems\":[{\"weight\":%.1f,\"name\":\"%s\",\"price\":%.2f}]," +
                            "\"virtualItems\":[{\"sizeOnDisk\":%.2f,\"name\":\"%s\",\"price\":%.2f}],\"total\":%.3f}",
                    cartName, ri.getWeight(), ri.getName(), ri.getPrice(),
                    vi.getSizeOnDisk(), vi.getName(), vi.getPrice(), cart.getTotalPrice()
            ));
        } catch (FileNotFoundException ex) {
            throw new NoSuchFileException(String.format("File %s.json not found!", file), ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void readFromFileReadsCorrectCartInfo() {
        Cart cart2 = jsonParser.readFromFile(new File("src/main/resources/" + cartName + ".json"));
        List<RealItem> realItems = null;
        List<VirtualItem> virtualItems = null;
        List<RealItem> realItems2 = null;
        List<VirtualItem> virtualItems2 = null;
        try {
            Field realItemsField = cart.getClass().getDeclaredField("realItems");
            realItemsField.setAccessible(true);
            realItems = (List<RealItem>) realItemsField.get(cart);

            Field virtualItemsField = cart.getClass().getDeclaredField("virtualItems");
            virtualItemsField.setAccessible(true);
            virtualItems = (List<VirtualItem>) virtualItemsField.get(cart);

            realItems2 = (List<RealItem>) realItemsField.get(cart2);

            virtualItems2 = (List<VirtualItem>) virtualItemsField.get(cart2);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        List<RealItem> finalRealItems = realItems;
        List<RealItem> finalRealItems2 = realItems2;
        List<VirtualItem> finalVirtualItems = virtualItems;
        List<VirtualItem> finalVirtualItems2 = virtualItems2;

        Assertions.assertAll(
                () -> Assertions.assertEquals(cart.getCartName(), cart2.getCartName()),
                () -> Assertions.assertEquals(String.valueOf(cart.getTotalPrice()), String.valueOf(cart2.getTotalPrice())),
                () -> Assertions.assertEquals(finalRealItems.toString(), finalRealItems2.toString()),
                () -> Assertions.assertEquals(finalVirtualItems.toString(), finalVirtualItems2.toString())
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
    }
}
