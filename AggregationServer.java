import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class AggregationServer {
    private static final Map<String, String> contentMap = new HashMap<>();

    public static void main(String[] args) {
        // port number
        int port = 8080;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    OutputStream outputStream = socket.getOutputStream();

                    String line = reader.readLine();
                    if (line == null) continue;

                    String[] requestParts = line.split(" ");
                    
                    String method = requestParts[0];
                    String path = requestParts[1];

                    // Read headers and body
                    StringBuilder body = new StringBuilder();
                    String headerLine;
                    int contentLength = 0;
                    while (!(headerLine = reader.readLine()).isEmpty()) {
                        if (headerLine.startsWith("Content-Length: ")) {
                            contentLength = Integer.parseInt(headerLine.split(": ")[1]);
                        }
                    }

                    for (int i = 0; i < contentLength; i++) {
                        body.append((char) reader.read());
                    }

                    String responseBody;
                    if ("PUT".equalsIgnoreCase(method)) {
                        contentMap.put(path, body.toString());
                        responseBody = "HTTP/1.1 200 OK\r\n\r\nContent received";
                    } else if ("GET".equalsIgnoreCase(method)) {
                        responseBody = "HTTP/1.1 200 OK\r\n\r\n" + contentMap.getOrDefault(path, "No content available");
                    } else {
                        responseBody = "HTTP/1.1 405 Method Not Allowed\r\n\r\n";
                    }

                    outputStream.write(responseBody.getBytes("UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
