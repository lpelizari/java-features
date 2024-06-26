# **Novas features incluídas na versão 11 do Java**


## 1. **Novo Cliente HTTP (Java SE 11)**
Inclusão de uma nova API HTTP baseada em Java para substituir a antiga `HttpURLConnection`. A nova API suporta HTTP/2 e WebSocket e é mais fácil de usar.

**Exemplo de Código:**

```java
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpClientExample {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.example.com/data"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
}
```

Agora abaixo está um exemplo de uma classe de teste em Java 11 utilizando a API HTTP, que inclui um exemplo de como se conectar a um websocket, enviar uma mensagem e receber respostas.

Primeiro, certifique-se de que você tem as dependências corretas no seu projeto. Se estiver usando Maven, o arquivo `pom.xml` deve incluir:

```xml
<dependencies>
    <dependency>
        <groupId>org.java-websocket</groupId>
        <artifactId>Java-WebSocket</artifactId>
        <version>1.5.2</version>
    </dependency>
</dependencies>
```

Aqui está o código da classe de teste utilizando a API HTTP do Java 11 e um exemplo de websocket:

```java
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;

public class WebSocketTest {
    public static void main(String[] args) throws Exception {
        URI uri = URI.create("wss://echo.websocket.org");
        CountDownLatch latch = new CountDownLatch(1);
        
        HttpClient client = HttpClient.newHttpClient();
        WebSocket.Builder builder = client.newWebSocketBuilder();
        
        WebSocket webSocket = builder.buildAsync(uri, new EchoListener(latch)).join();
        webSocket.sendText("Hello, WebSocket!", true);
        
        latch.await();
    }
    
    static class EchoListener implements Listener {
        private final CountDownLatch latch;

        EchoListener(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void onOpen(WebSocket webSocket) {
            System.out.println("WebSocket opened");
            Listener.super.onOpen(webSocket);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            System.out.println("Received message: " + data);
            latch.countDown();
            return Listener.super.onText(webSocket, data, last);
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            error.printStackTrace();
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            System.out.println("WebSocket closed with status " + statusCode + ", reason: " + reason);
            latch.countDown();
            return Listener.super.onClose(webSocket, statusCode, reason);
        }
    }
}
```

### Explicando o código acima:

1. **Configuração do WebSocket**: A classe `WebSocketTest` cria um URI que aponta para um serviço de eco de WebSocket. O `CountDownLatch` é usado para esperar pela resposta do WebSocket.

2. **Criação do HttpClient**: Um `HttpClient` é criado usando o método `newHttpClient()`.

3. **Construção do WebSocket**: Um `WebSocket.Builder` é obtido do `HttpClient` e usado para construir um `WebSocket` assíncrono com o URI do WebSocket e um `Listener`.

4. **Envio de Mensagem**: Após a construção, o WebSocket envia uma mensagem "Hello, WebSocket!" usando o método `sendText`.

5. **Listener**: A classe `EchoListener` implementa a interface `WebSocket.Listener` para manipular eventos de WebSocket:
   - **onOpen**: É chamado quando a conexão é aberta.
   - **onText**: É chamado quando uma mensagem de texto é recebida.
   - **onError**: É chamado em caso de erros.
   - **onClose**: É chamado quando a conexão é fechada.

Este código estabelece uma conexão WebSocket, envia uma mensagem, e imprime qualquer mensagem recebida. O `CountDownLatch` garante que o programa aguarde até que uma resposta seja recebida antes de terminar a execução.


## 2. **API de Processamento de Strings**
Novas funcionalidades para a classe `String` que facilitam o trabalho com strings. Aqui estão as principais inclusões:

1. **`isBlank()`**:
   - Verifica se uma string é vazia ou contém apenas espaços em branco.
   - Retorna `true` se a string for vazia ou contiver apenas espaços em branco; caso contrário, retorna `false`.
   - Exemplo:
     ```java
     String str1 = "";
     String str2 = "   ";
     String str3 = "abc";

     System.out.println(str1.isBlank()); // true
     System.out.println(str2.isBlank()); // true
     System.out.println(str3.isBlank()); // false
     ```

2. **`strip()`, `stripLeading()`, `stripTrailing()`**:
   - `strip()`: Remove espaços em branco de ambos os lados da string.
   - `stripLeading()`: Remove espaços em branco do início da string.
   - `stripTrailing()`: Remove espaços em branco do final da string.
   - Esses métodos consideram todos os caracteres Unicode de espaço em branco, ao contrário de `trim()` que considera apenas os caracteres com código ASCII abaixo de 32.
   - Exemplo:
     ```java
     String str = "   Hello World!   ";

     System.out.println(str.strip());        // "Hello World!"
     System.out.println(str.stripLeading()); // "Hello World!   "
     System.out.println(str.stripTrailing());// "   Hello World!"
     ```

3. **`lines()`**:
   - Converte uma string em um `Stream<String>`, dividindo-a em linhas.
   - Cada linha termina com um `\n`, `\r`, ou `\r\n`.
   - Exemplo:
     ```java
     String multilineStr = "Line1\nLine2\rLine3\r\nLine4";
     multilineStr.lines().forEach(System.out::println);
     // Output:
     // Line1
     // Line2
     // Line3
     // Line4
     ```

4. **`repeat(int count)`**:
   - Repete a string um número específico de vezes.
   - Retorna uma nova string que é a concatenação da string original repetida `count` vezes.
   - Exemplo:
     ```java
     String str = "abc";
     String repeatedStr = str.repeat(3);
     System.out.println(repeatedStr); // "abcabcabc"
     ```

5. **`strip()` versus `trim()`**:
   - `strip()`: Remove todos os caracteres Unicode de espaço em branco.
   - `trim()`: Remove apenas os caracteres com código ASCII menor que 32.
   - Exemplo:
     ```java
     String str = "\u2000 Hello \u2000"; // \u2000 é um espaço em branco unicode
     System.out.println(str.strip()); // "Hello"
     System.out.println(str.trim());  // "\u2000 Hello \u2000"
     ```

6. **`isEmpty()` versus `isBlank()`**:
   - `isEmpty()`: Verifica se a string tem comprimento zero.
   - `isBlank()`: Verifica se a string é vazia ou contém apenas espaços em branco.
   - Exemplo:
     ```java
     String emptyStr = "";
     String blankStr = "   ";

     System.out.println(emptyStr.isEmpty()); // true
     System.out.println(blankStr.isEmpty()); // false

     System.out.println(emptyStr.isBlank()); // true
     System.out.println(blankStr.isBlank()); // true
     ```


## 3. **Tipo Inferido para Variáveis Locais (var)**

Incluso a possibilidade do uso de `var` para declarar parâmetros de expressões lambda, proporcionando maior flexibilidade e uniformidade ao escrever lambdas.

### Aplicação:

1. **Uso Consistente**: Se optar por usar `var` em uma expressão lambda, você deve usá-lo para todos os parâmetros dessa lambda. Não é permitido misturar `var` com tipos explícitos ou inferidos.

   ```java
   (var a, var b) -> a.length() - b.length(); // Correto
   (String a, var b) -> a.length() - b.length(); // Incorreto
   (var a, b) -> a.length() - b.length(); // Incorreto
   ```

2. **Necessidade de Inicialização**: Como sempre, `var` requer que o tipo possa ser inferido a partir do contexto da inicialização.

3. **Anotações**: O uso de `var` facilita a adição de anotações aos parâmetros da lambda.

### Exemplos Práticos

#### Exemplo 1: Uso em Streams

Filtrar e mapear uma lista de strings para seus respectivos tamanhos:

**Sem `var`**:
```java
List<String> strings = List.of("a", "bb", "ccc", "dd");
List<Integer> lengths = strings.stream()
    .map(s -> s.length())
    .collect(Collectors.toList());
```

**Com `var`**:
```java
List<String> strings = List.of("a", "bb", "ccc", "dd");
List<Integer> lengths = strings.stream()
    .map((var s) -> s.length())
    .collect(Collectors.toList());
```

#### Exemplo 2: Lambda com múltiplas operações

Uma expressão lambda que faz várias operações:

**Sem `var`**:
```java
BiFunction<Integer, Integer, Integer> multiplyAndAdd = (x, y) -> {
    int product = x * y;
    return product + 10;
};
```

**Com `var`**:
```java
BiFunction<Integer, Integer, Integer> multiplyAndAdd = (var x, var y) -> {
    int product = x * y;
    return product + 10;
};
```

#### Exemplo 3: Adição de anotações de checagem de Null

**Sem `var`**:
```java
BiFunction<String, String, String> concat = (s1, s2) -> s1 + s2;
```

**Com `var`**:
```java
BiFunction<String, String, String> concat = (@NonNull var s1, @NonNull var s2) -> s1 + s2;
```

#### Exemplo 4: Processamento de Mapas

Necessidade de iterar sobre um mapa e processar suas entradas:

**Sem `var`**:
```java
Map<String, Integer> map = Map.of("a", 1, "b", 2, "c", 3);
map.forEach((key, value) -> System.out.println(key + ": " + value));
```

**Com `var`**:
```java
Map<String, Integer> map = Map.of("a", 1, "b", 2, "c", 3);
map.forEach((var key, var value) -> System.out.println(key + ": " + value));
```

### 4. **Executar Arquivos de Código-Fonte Java**
Você pode executar diretamente arquivos `.java` sem a necessidade de compilação explícita.

**Exemplo de Código:**
Salve o seguinte código em `HelloWorld.java` e execute com `java HelloWorld.java`.

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
```

### 5. **Leitura e Escrita de arquivos**

Na versão 11 do Java, a classe `java.nio.file.Files` recebeu algumas adições significativas que ampliam sua funcionalidade para manipulação de arquivos e diretórios. Estes métodos ajudam a simplificar operações comuns e a aumentar a eficiência e legibilidade do código. Abaixo, descrevo os métodos adicionais em detalhes:

#### 5.1. `writeString(Path, CharSequence, OpenOption...)`

Este método permite escrever uma sequência de caracteres (`CharSequence`) em um arquivo. É uma maneira conveniente de escrever texto em um arquivo sem precisar gerenciar um `BufferedWriter` ou `FileWriter`.

#### Sintaxe:
```java
public static Path writeString(Path path, CharSequence csq, OpenOption... options) throws IOException
```

#### Exemplo:
```java
Path filePath = Paths.get("example.txt");
String content = "Hello, World!";
Files.writeString(filePath, content);
```

#### 5.2. `readString(Path)`

Este método permite ler todo o conteúdo de um arquivo e retorná-lo como uma string. Ele simplifica a leitura de arquivos de texto, eliminando a necessidade de um `BufferedReader` ou `FileReader`.

#### Sintaxe:
```java
public static String readString(Path path) throws IOException
```

#### Exemplo:
```java
Path filePath = Paths.get("example.txt");
String content = Files.readString(filePath);
System.out.println(content);
```

### 6. **Padronização do Epsilon GC**

No versão 11 do Java foi incluído o **Epsilon Garbage Collector** (Epsilon GC), um coletor de lixo experimental. O Epsilon GC é um coletor de lixo de "não-operação", significando que ele não faz nenhuma coleta de lixo ativa. Sua principal função é alocar memória sem nunca liberá-la.

### 6.1. Objetivo do Epsilon GC

O Epsilon GC é projetado para cenários onde a administração da memória e a coleta de lixo não são desejadas ou são desnecessárias. Exemplos de tais cenários incluem:

- **Testes de desempenho**: Onde o impacto de diferentes coletores de lixo devem ser comparados.
- **Curta duração de vida**: Aplicações que têm um ciclo de vida muito curto e não precisam liberar memória.
- **Medição de sobrecarga**: Avaliar a sobrecarga de memória e a latência adicionadas por coletores de lixo.
- **Desenvolvimento de alocadores**: Testes iniciais de novos alocadores de memória e técnicas de gerenciamento de heap.

### 6.2. Características do Epsilon GC

1. **Alocação Sem Coleta**: O Epsilon GC simplesmente aloca memória sem realizar nenhuma forma de coleta de lixo. Quando a memória heap está cheia, a JVM encerra com uma mensagem de falta de memória (OutOfMemoryError).

2. **Previsibilidade**: Como não há coleta de lixo, o Epsilon GC proporciona uma latência muito baixa e previsível para alocações de memória. Isso é benéfico para medir a carga base da alocação de memória sem a interferência de pausas de coleta de lixo.

3. **Simples e Leve**: Sem a lógica complexa de coleta de lixo, o Epsilon GC é simples e possui um overhead mínimo, o que facilita o diagnóstico de problemas de desempenho relacionados à alocação de memória.

### 6.3. Ativação do Epsilon GC

Para ativar o Epsilon GC, você pode usar a seguinte opção de linha de comando ao iniciar a JVM:

```sh
java -XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC -Xmx1g -jar YourApplication.jar
```

### 6.4. Limitações

1. **Sem Coleta de Lixo**: O Epsilon GC não faz coleta de lixo. Assim que a memória heap estiver cheia, a JVM terminará com um `OutOfMemoryError`.
2. **Uso Específico**: Devido à sua natureza, o Epsilon GC não é adequado para aplicações de longo prazo ou aquelas que requerem a reciclagem de memória.

### 6.5. Uso em Diferentes Cenários

#### Cenário 1: Testes de Desempenho

Para comparar o impacto de diferentes coletores de lixo, o Epsilon GC pode ser usado para medir a performance base de uma aplicação sem a interferência de pausas de coleta de lixo.

```sh
java -XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC -Xmx1g -jar PerformanceTest.jar
```

#### Cenário 2: Aplicações de Curta Duração

Aplicações que têm um tempo de vida curto e são reiniciadas frequentemente podem se beneficiar do Epsilon GC, pois não precisam liberar memória.

```sh
java -XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC -Xmx512m -jar ShortLivedApp.jar
```

#### Cenário 3: Medição de Sobrecarga

Para medir a sobrecarga introduzida pelos coletores de lixo, Epsilon GC pode ser utilizado para estabelecer uma linha base.

```sh
java -XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC -Xmx2g -jar OverheadMeasurement.jar
```

### 6.6. Mensagens de Log

O Epsilon GC pode gerar mensagens de log informativas para diagnosticar alocações e esgotamentos de memória:

```sh
java -XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC -Xlog:gc=info -Xmx1g -jar YourApplication.jar
```

Exemplo de saída de log:
```
[0.012s][info][gc] Using Epsilon
[0.012s][info][gc] Heap: 1.0 GiB reserved, 1.0 GiB (100.0%) committed, 1.0 GiB (100.0%) used
...
[3.456s][info][gc] OutOfMemoryError: Java heap space
```

### 6.7. Conclusão

O Epsilon GC é uma adição poderosa ao arsenal de ferramentas de desempenho do Java, fornecendo um coletor de lixo que não realiza coleta de lixo. Ele é ideal para casos de uso específicos, como testes de desempenho, aplicações de curta duração e medições de sobrecarga. No entanto, devido à sua natureza, não é adequado para aplicações que requerem a reciclagem contínua de memória.


### 7. **Depreciação de Pacotes e APIs**

No Java 11, várias classes, métodos, e pacotes foram deprecados como parte do esforço contínuo para melhorar e modernizar a plataforma Java. Aqui estão alguns dos principais pacotes e APIs que foram deprecados na versão.

### 7.1. Depreciações Principais no Java 11

#### 1. `java.security.acl` (Pacote Completo)

O pacote `java.security.acl` foi deprecado na versão 11. Este pacote contém classes e interfaces que definem um modelo de controle de acesso baseado em listas de controle de acesso (ACLs).

#### Motivo da Depreciação:
O modelo ACL foi considerado obsoleto e pouco utilizado. O pacote não oferece a flexibilidade e a segurança encontradas em outras abordagens de controle de acesso, como políticas baseadas em permissões que são usadas no modelo de segurança Java mais moderno.

#### Alternativas:
Recomenda-se o uso de APIs de segurança mais modernas, como o framework de segurança baseado em permissões do Java.

#### 2. Métodos da Classe `java.util.logging.LogManager`

Certos métodos da classe `java.util.logging.LogManager` foram deprecados.

#### Exemplos de Métodos Deprecados:
- `public void addPropertyChangeListener(PropertyChangeListener l)`
- `public void removePropertyChangeListener(PropertyChangeListener l)`

#### Motivo da Depreciação:
Esses métodos de gerenciamento de eventos foram considerados desnecessários para a maioria dos casos de uso e adicionavam complexidade ao gerenciamento de logs.

#### Alternativas:
Para a configuração de logging, utilize as funcionalidades principais da API de logging, evitando o uso de listeners de propriedades.

#### 3. `Thread.destroy()` e `Thread.stop(Throwable)`

Os métodos `destroy()` e `stop(Throwable)` da classe `Thread` foram deprecados.

#### Motivo da Depreciação:
Esses métodos são inerentemente inseguros. O método `destroy()` nunca foi implementado, e `stop(Throwable)` pode corromper o estado dos objetos, deixando a aplicação em um estado inconsistente.

#### Alternativas:
Use mecanismos de controle de threads mais seguros, como flags de interrupção (`Thread.interrupt()`).

#### 4. `javafx` (Pacote Completo)

O JavaFX, anteriormente parte do JDK, foi removido e passou a ser distribuído separadamente.

#### Motivo da Depreciação:
A separação visa permitir que o JavaFX seja desenvolvido e atualizado independentemente do ciclo de lançamento do JDK, permitindo um ritmo mais rápido de melhorias e correções de bugs.

#### Alternativas:
Os desenvolvedores que desejam usar JavaFX podem adicioná-lo como uma biblioteca independente através de sistemas de build como Maven ou Gradle.

#### 5. `javax.xml.bind` (JAXB) e `javax.activation`

As APIs `javax.xml.bind` (JAXB) e `javax.activation` foram deprecadas e removidas do JDK.

#### Motivo da Depreciação:
Estas APIs foram movidas para módulos separados para modularizar melhor o JDK e reduzir o tamanho do runtime base.

#### Alternativas:
Para continuar utilizando essas APIs, os desenvolvedores devem adicionar as dependências correspondentes de JAXB e Activation através de seus gerenciadores de dependência (Maven, Gradle, etc.).

#### 6. `javax.annotation.processing.Filer.createSourceFile(String, Element...)`

O método `createSourceFile` da interface `javax.annotation.processing.Filer` foi deprecado.

#### Motivo da Depreciação:
Este método é propenso a erros e sua funcionalidade pode ser substituída por outros métodos mais seguros e robustos.

#### Alternativas:
Use métodos de criação de arquivos que não requerem uma lista de elementos para evitar erros de compilação e runtime.

### Conclusão

A depreciação de pacotes e APIs no Java 11 reflete o esforço contínuo da comunidade Java para manter a plataforma moderna, segura e eficiente. As depreciações muitas vezes indicam que existem abordagens melhores e mais seguras disponíveis, e os desenvolvedores são incentivados a migrar para essas alternativas. É importante acompanhar as notas de lançamento e as documentações de cada versão do Java para estar ciente dessas mudanças e ajustar o código conforme necessário.


### 8. **Novas Métodos para Coleções**

No Java 11, a linguagem e a biblioteca padrão foram aprimoradas com a introdução de novos métodos para coleções, facilitando ainda mais o trabalho com dados em coleções.

#### 8.1. `toArray(IntFunction<A[]>)` em `Collection`

O método `toArray(IntFunction<A[]>)` foi adicionado à interface `Collection`. Ele permite a criação de um array de um tipo específico diretamente a partir de uma coleção, utilizando uma função para definir o tipo de array.

#### Sintaxe:
```java
default <T> T[] toArray(IntFunction<T[]> generator)
```

#### Exemplo:
```java
import java.util.List;
import java.util.function.IntFunction;

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
```

#### Explicação:
- **IntFunction<T[]> generator**: Um gerador de função que recebe o tamanho do array e retorna um novo array do tipo desejado.
- **String[]::new**: Referência de método que cria um array de `String` com o tamanho especificado.


### 9. **Melhorias no Fluxo de Pilha de Exceção**

No Java 11, foram introduzidas melhorias significativas no fluxo de pilha de exceção (exception stack trace) que facilitam a depuração de erros e a compreensão do contexto em que as exceções ocorrem. Essas melhorias estão centradas principalmente em dois novos métodos na classe `Throwable`:

1. `Throwable::getSuppressed`
2. `Throwable::getStackTrace`

Além disso, a introdução de métodos como `Throwable::addSuppressed` e `StackWalker` em versões anteriores, complementa essas funcionalidades para uma gestão mais eficaz das exceções. Segue abaixo:

### 9.1. `Throwable::getSuppressed`

O método `getSuppressed` retorna um array de exceções suprimidas que foram adicionadas a este `Throwable`. Exceções suprimidas são aquelas que ocorrem, mas são capturadas para não interromper o fluxo de execução principal. Este método foi introduzido no Java 7, mas seu uso e importância são reforçados com as melhorias gerais no gerenciamento de exceções em versões posteriores.

#### Sintaxe:
```java
public final Throwable[] getSuppressed()
```

#### Exemplo:
```java
public class SuppressedExceptionExample {
    public static void main(String[] args) {
        try {
            methodWithSuppressedException();
        } catch (Exception e) {
            System.err.println("Caught: " + e);
            for (Throwable t : e.getSuppressed()) {
                System.err.println("Suppressed: " + t);
            }
        }
    }

    static void methodWithSuppressedException() throws Exception {
        Exception primary = new Exception("Primary Exception");
        try {
            throw new Exception("Suppressed Exception");
        } catch (Exception e) {
            primary.addSuppressed(e);
        }
        throw primary;
    }
}
```

#### Explicação:
- **primary.addSuppressed(e)**: Adiciona a exceção suprimida `e` à exceção primária `primary`.
- **e.getSuppressed()**: Retorna um array das exceções suprimidas adicionadas à exceção `e`.

### 9.2. `Throwable::getStackTrace`

O método `getStackTrace` foi aprimorado para permitir uma melhor manipulação e visualização do stack trace, especialmente em contextos de depuração complexos. Este método retorna um array de `StackTraceElement` que representa o stack trace completo.

#### Sintaxe:
```java
public StackTraceElement[] getStackTrace()
```

#### Exemplo:
```java
public class StackTraceExample {
    public static void main(String[] args) {
        try {
            methodThatThrowsException();
        } catch (Exception e) {
            System.err.println("Caught: " + e);
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println(element);
            }
        }
    }

    static void methodThatThrowsException() throws Exception {
        throw new Exception("Exception with stack trace");
    }
}
```

#### Explicação:
- **e.getStackTrace()**: Retorna um array de `StackTraceElement` representando o stack trace da exceção `e`.

### 9.3. `Throwable::addSuppressed`

O método `addSuppressed` permite adicionar exceções suprimidas a um `Throwable`. Isso é particularmente útil no contexto de blocos try-with-resources, onde exceções secundárias podem ser suprimidas em favor de exceções primárias.

#### Sintaxe:
```java
public final void addSuppressed(Throwable exception)
```

#### Exemplo:
Veja o exemplo fornecido na seção de `getSuppressed`.

### 9.4. `StackWalker`

Introduzido no Java 9 e reforçado no Java 11, `StackWalker` é uma API poderosa para acessar e manipular o stack trace de maneira mais flexível e eficiente do que os métodos tradicionais.

#### Sintaxe e Exemplo:
```java
import java.lang.StackWalker.StackFrame;

public class StackWalkerExample {
    public static void main(String[] args) {
        try {
            methodThatThrowsException();
        } catch (Exception e) {
            StackWalker walker = StackWalker.getInstance();
            walker.forEach(frame -> {
                System.err.println(frame);
            });
        }
    }

    static void methodThatThrowsException() throws Exception {
        throw new Exception("Exception with stack trace");
    }
}
```

#### Explicação:
- **StackWalker.getInstance()**: Cria uma instância do `StackWalker`.
- **walker.forEach(frame -> {...})**: Itera sobre cada frame do stack trace, permitindo manipulação e visualização customizada.

### 9.5. Conclusão

As melhorias no fluxo de pilha de exceção no Java 11, juntamente com as funcionalidades introduzidas em versões anteriores, fornecem aos desenvolvedores ferramentas robustas para a depuração e gerenciamento de exceções. 
Essas melhorias ajudam a capturar o contexto completo das falhas, facilitando a identificação e correção de bugs.


### 10. **Melhorias em Interfaces**

No Java 11, foram introduzidas várias melhorias nas interfaces, tornando-as ainda mais poderosas e flexíveis. A principal adição foi a capacidade de incluir métodos privados em interfaces, permitindo uma organização mais limpa e modular do código dentro das interfaces.

### 10.1. Métodos Privados em Interfaces

Antes do Java 9, as interfaces podiam conter apenas métodos abstratos, métodos default e métodos estáticos. A partir do Java 9, e mantido no Java 11, as interfaces agora podem incluir métodos privados. Isso permite que os métodos default e estáticos compartilhem código comum de uma maneira mais organizada, sem expor esse código como parte da API pública da interface.

#### Sintaxe:

Os métodos privados em interfaces seguem a mesma sintaxe dos métodos privados em classes:

```java
private void methodName() {
    // implementação
}

private static void staticMethodName() {
    // implementação
}
```

### Exemplo de Uso

Aqui está um exemplo detalhado que demonstra como os métodos privados podem ser usados para melhorar a estrutura e a reutilização de código em interfaces.

#### Interface com Métodos Privados

```java
public interface MyInterface {

    // Método default que usa um método privado
    default void defaultMethod() {
        commonMethod("Default Method");
    }

    // Outro método default que usa o mesmo método privado
    default void anotherDefaultMethod() {
        commonMethod("Another Default Method");
    }

    // Método privado para compartilhar a lógica comum
    private void commonMethod(String methodName) {
        System.out.println("Executing " + methodName);
    }

    // Método estático que usa um método privado estático
    static void staticMethod() {
        commonStaticMethod("Static Method");
    }

    // Método privado estático para compartilhar a lógica comum
    private static void commonStaticMethod(String methodName) {
        System.out.println("Executing " + methodName);
    }
}
```

#### Implementação da Interface

```java
public class MyClass implements MyInterface {

    public static void main(String[] args) {
        MyClass obj = new MyClass();

        // Chamando métodos default
        obj.defaultMethod();
        obj.anotherDefaultMethod();

        // Chamando método estático da interface
        MyInterface.staticMethod();
    }
}
```

#### Saída:

```
Executing Default Method
Executing Another Default Method
Executing Static Method
```

### Benefícios dos Métodos Privados em Interfaces

1. **Reutilização de Código**: Permite que métodos default e estáticos reutilizem código sem duplicação, promovendo DRY (Don't Repeat Yourself).

2. **Encapsulamento**: Melhora o encapsulamento ao esconder a lógica de implementação que não deve ser exposta como parte da API pública.

3. **Legibilidade**: Melhora a legibilidade e a organização do código dentro das interfaces, separando claramente os métodos de utilidade interna dos métodos que compõem a API pública.

4. **Manutenção**: Facilita a manutenção do código, pois mudanças no método privado afetam apenas a interface e suas implementações, sem impactar classes externas que utilizam a interface.

### 10.2. Considerações

- **Acessibilidade**: Métodos privados em interfaces só podem ser acessados dentro da interface em que são definidos.
- **Modularidade**: Ajuda a organizar melhor o código dentro das interfaces, especialmente em interfaces complexas com múltiplos métodos default e estáticos.

### 10.3. Conclusão

As melhorias nas interfaces no Java 11, particularmente a introdução de métodos privados, representam um passo significativo na evolução da linguagem Java. Estas melhorias permitem uma melhor organização do código, maior reutilização e encapsulamento, e facilitam a manutenção. Essas adições tornam as interfaces mais poderosas e flexíveis, permitindo que os desenvolvedores escrevam código mais limpo e modular.