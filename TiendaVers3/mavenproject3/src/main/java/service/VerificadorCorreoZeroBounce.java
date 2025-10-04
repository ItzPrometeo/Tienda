package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class VerificadorCorreoZeroBounce {

    private static final String API_KEY = "b3b514db8f3c4fe482f6f555bcc13419"; // ← Reemplaza con tu API KEY real

    public static boolean esCorreoValido(String correo) {
        try {
            String urlStr = "https://api.zerobounce.net/v2/validate?api_key=" + API_KEY + "&email=" + correo;
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );
            String inputLine;
            StringBuilder respuesta = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                respuesta.append(inputLine);
            }
            in.close();

            JSONObject json = new JSONObject(respuesta.toString());
            String status = json.getString("status"); // deliverable, undeliverable, unknown, etc.

            return status.equalsIgnoreCase("valid") || status.equalsIgnoreCase("deliverable");

        } catch (Exception e) {
            e.printStackTrace();
            return false; // En caso de error, no se valida el correo.
        }
    }
}
