package httpclient;

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