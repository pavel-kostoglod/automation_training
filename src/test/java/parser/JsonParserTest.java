package parser;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import shop.Cart;

import java.io.File;

public class JsonParserTest {
    private JsonParser jsonParser;
    private Cart cart;
    private String cartName;
    private File file;

    @BeforeEach
    void prepare() {
        jsonParser = new JsonParser();
        cartName = RandomStringUtils.random(10, true, false);
        cart = new Cart(cartName);
        jsonParser.writeToFile(cart);
        file = new File("src/main/resources/" + cartName + ".json");
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
