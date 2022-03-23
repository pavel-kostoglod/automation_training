package utils;

import com.google.gson.Gson;
import parser.NoSuchFileException;
import shop.Cart;

import java.io.*;

public class Utils {

    public static String readFromFile(File file) {
        String string = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            string = reader.readLine();
        } catch (FileNotFoundException ex) {
            throw new NoSuchFileException(String.format("File %s.json not found!", file), ex);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return string;
    }

    public static void writeCartToFile(Cart cart) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter("src/main/resources/" + cart.getCartName() + "_util.json")) {
            writer.write(gson.toJson(cart));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
