package collections;

import java.util.List;

public class CollectionToArrayExample {

    public static void main(String[] args) {
        List<String> list = List.of("apple", "banana", "cherry");

        // Usando o novo método toArray com um IntFunction para criar um array de String
        String[] array = list.toArray(String[]::new);

        for (String fruit : array) {
            System.out.println(fruit);
        }
    }
}
