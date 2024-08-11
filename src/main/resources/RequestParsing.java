import org.json.JSONObject;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

public class RequestParsing {

    public static JSONObject parseToJSON(HttpServletRequest request) throws Exception{
        StringBuilder payloadString = new StringBuilder();

        String line = "";
        try (BufferedReader reader = request.getReader()){
            while ((line = reader.readLine()) != null){
                payloadString.append(line);
            }
        }

        JSONObject parsedRequest = new JSONObject(payloadString.toString());
        return parsedRequest;
    }
}