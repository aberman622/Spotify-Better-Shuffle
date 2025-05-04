package ServerSide;

import java.io.*;
import java.net.*;
import java.util.Base64;
import javax.net.ssl.HttpsURLConnection;

public class SpotifyAuth {

    public static String getAccessToken () throws IOException {
        String clientId = "08e7f7136d734100b0eea58920686996";
        String clientSecret = "3b88706b7e1d4a48ab783b29f9c7b751";

        String auth = new String(Base64.getEncoder().encode((clientId + ":" + clientSecret).getBytes()));
        String url = "https://accounts.spotify.com/api/token";

        // connection
        URL obj = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Basic " + auth);
        connection.setDoOutput(true);

        String body = "grant_type=client_credentials";
        connection.getOutputStream().write(body.getBytes());

        // response
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Extract access token from  response
        String responseBody = response.toString();
        String accessToken = responseBody.split("\"access_token\":\"")[1].split("\"")[0];

        return accessToken;
    }
}
