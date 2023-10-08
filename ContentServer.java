import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.io.*;


public class ContentServer {
    List<WeatherData> weatherDataList;

    public static void main(String[] args) {
        ContentServer  cs = new ContentServer();

    }

    public ContentServer(){
        weatherDataList = new ArrayList<>();
        readInformation("input.txt");
    }


    public void HttpPutMethod(){
        String server = "localhost";
        String path = "/example";
        String content = "Hello, this is the content!";

        try (Socket socket = new Socket(server, 8080)) {
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);

            writer.println("PUT " + path + " HTTP/1.1");
            writer.println("Host: " + server);
            writer.println("Content-Length: " + content.length());
            writer.println(); // Empty line to end headers
            writer.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




public void sendHTTPPut(Object weatherData, String serverAddress, int port) {
    try (Socket socket = new Socket(serverAddress, port);
         PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

        // Step 2: Convert object to JSON. In real-life scenarios, you might use a library like Gson.
        String jsonString = convertObjectToJson(weatherData); // This is a mock method.

        // Step 3: Calculate necessary headers.
        String contentLength = String.valueOf(jsonString.length());
        
        // Step 4: Send the request.
        out.println("PUT /weather.json HTTP/1.1");
        out.println("User-Agent: ATOMClient/1.0");
        out.println("Content-Type: application/json; charset=utf-8");
        out.println("Content-Length: " + contentLength);
        out.println(""); // Important, end of headers!
        out.println(jsonString);

        // Step 5: Optionally handle the server response.
        String responseLine;
        while ((responseLine = in.readLine()) != null) {
            System.out.println(responseLine);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

// Mock method to convert object to JSON.
private String convertObjectToJson(Object object) {
    // In real life, use a library like Gson to convert object to JSON.
    // Here's a simple mocked version for the sake of the example.
    return "{ \"id\": \"IDS60901\", ... }";
}


    public List<WeatherData> readInformation(String fileLocation){
        try (BufferedReader reader = new BufferedReader(new FileReader(fileLocation))) {
            String line;
            WeatherData data = new WeatherData();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) { // Ensure line contains valid data
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    switch (key) {
                        case "id":
                            data.setId(value);
                            break;
                        case "name":
                            data.setName(value);
                            break;
                        case "state":
                            data.setState(value);
                            break;
                        case "time_zone":
                            data.setTimeZone(value);
                            break;
                        case "lat":
                            data.setLat(Double.parseDouble(value));
                            break;
                        case "lon":
                            data.setLon(Double.parseDouble(value));
                            break;
                        case "local_date_time":
                            data.setLocalDateTime(value);
                            break;
                        case "local_date_time_full":
                            data.setLocalDateTimeFull(value);
                            break;
                        case "air_temp":
                            data.setAirTemp(Double.parseDouble(value));
                            break;
                        case "apparent_t":
                            data.setApparentT(Double.parseDouble(value));
                            break;
                        case "cloud":
                            data.setCloud(value);
                            break;
                        case "dewpt":
                            data.setDewpt(Double.parseDouble(value));
                            break;
                        case "press":
                            data.setPress(Double.parseDouble(value));
                            break;
                        case "rel_hum":
                            data.setRelHum(Integer.parseInt(value));
                            break;
                        case "wind_dir":
                            data.setWindDir(value);
                            break;
                        case "wind_spd_kmh":
                            data.setWindSpdKmh(Integer.parseInt(value));
                            break;
                        case "wind_spd_kt":
                            data.setWindSpdKt(Integer.parseInt(value));
                            break;
                    }
                } else if (line.trim().isEmpty() && data.getId() != null) { // Empty line, end of record
                    weatherDataList.add(data);
                    data = new WeatherData(); // Create new object for next record
                }
            }
            if (data.getId() != null && !weatherDataList.contains(data)) {
                weatherDataList.add(data);
            }
        }catch (IOException e) {
            // TODO: handle exception
        }
        return weatherDataList;
    }
}




