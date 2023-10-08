import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SimpleHttpClient {
    public static void main(String[] args) {
        // get the command line args, arg0 -> server, arg 1 -> path
        // TODO 
        if (args.length < 2) {
            System.out.println("Usage: SimpleHttpClient <server> <path>");
            System.exit(0);
        }
        String server = args[0];
        String path = args[1];

        try (Socket socket = new Socket(server, 8080)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), false);
            out.println("GET " + path + " HTTP/1.1");
//            out.println("Host: " + server);
            out.println();
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO
    public void start(){

    }
}


