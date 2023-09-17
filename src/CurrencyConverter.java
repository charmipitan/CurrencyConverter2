import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class CurrencyConverter {
    public static void main(String[] args) {
        HashMap<Integer, String> currencyCodes = new HashMap<Integer, String>();

        // Initializing currencyCodes HashMap
        currencyCodes.put(1, "GDP");
        currencyCodes.put(2, "USD");
        currencyCodes.put(3, "EUR");
        currencyCodes.put(4, "CAD");
        currencyCodes.put(5, "HKD");

        String fromCode, toCode;
        double amount;

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Welcome to Charmaine's Currency Converter! ");
            System.out.print("Currency converting FROM?");
            System.out.println("1: GDP (Great British Pounds)\t 2: USD (US Dollars)\t 3: EUR (EUROS)\t 4: CAD (Canadian Dollars)\t 4: HKD (Hong Kong Dollar)");

            fromCode = currencyCodes.get(scanner.nextInt());

            System.out.println("Currency converting TO?");
            toCode = currencyCodes.get(scanner.nextInt());

            System.out.println("Amount you wish to convert?");
            amount = scanner.nextFloat();
        }

        try {
            sendHttpGETRequest(fromCode, toCode, amount);
            System.out.println("Thank you. The amount has been successfully converted.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Sends an HTTP GET request to obtain the exchange rate
    private static void sendHttpGETRequest(String fromCode, String toCode, double amount) throws MalformedURLException {
        String GET_URL = "https://v6.exchangerate-api.com/v6/276d99b9f7227508c89d37a4/pair/" + fromCode + "/" + toCode;

        URL url = new URL(GET_URL);

        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // SUCCESS
                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject exchangeRateJson = new JSONObject(response.toString());
                Double exchangeRate = exchangeRateJson.getJSONObject("rate").getDouble(fromCode);
                System.out.println(exchangeRateJson.getJSONObject("rates"));
                System.out.println(exchangeRate);
                // for debugging
                System.out.println();
                System.out.println(amount + " " + fromCode + " = " + amount / exchangeRate + " " + toCode);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
