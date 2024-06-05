package lambdaWithVar;

import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class VarLambdaExamples {

    public static void main(String[] args) {

        // Exemplo 1: Uso em Streams
        List<String> strings = List.of("a", "bb", "ccc", "dd");
        List<Integer> lengths = strings.stream()
                .map((var s) -> s.length())
                .collect(Collectors.toList());
        System.out.println("Lengths of strings: " + lengths);

        // Exemplo 2: Lambda com Múltiplas Operações
        BiFunction<Integer, Integer, Integer> multiplyAndAdd = (var x, var y) -> {
            int product = x * y;
            return product + 10;
        };
        System.out.println("Result of multiplyAndAdd(3, 5): " + multiplyAndAdd.apply(3, 5));

        // Exemplo 3: Adição de Anotações de Checagem de Null
        BiFunction<String, String, String> concat = (@NonNull var s1, @NonNull var s2) -> s1 + s2;
        System.out.println("Concatenation of 'hello' and 'world': " + concat.apply("hello", "world"));

        // Exemplo 4: Processamento de Mapas
        Map<String, Integer> map = Map.of("a", 1, "b", 2, "c", 3);
        map.forEach((var key, var value) -> System.out.println(key + ": " + value));
    }
}

