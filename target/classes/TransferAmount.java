import com.google.gson.Gson;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;

public class TransferAmount extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException{

        resp.setContentType("application/json");
        try{
            JSONObject payload = RequestParsing.parseToJSON(req);
            Double amountToTransfer = payload.getDouble("amount");
            int recipientID = payload.getInt("recipientID");

            if(amountToTransfer.isNaN() || null == amountToTransfer || amountToTransfer.compareTo(Double.parseDouble("0.00")) < 0 || amountToTransfer.compareTo(Double.parseDouble("0.00")) == 0){
                HashMap<String,String> responseMessage = new HashMap<>();
                responseMessage.put("message", "Invalid Amount Entered");
                System.out.println("Invalid Amount Entered. Could Not Transfer Funds");

                Gson gson = new Gson();
                String response = gson.toJson(responseMessage);
                resp.getWriter().println(response);
                resp.setStatus(400);
            }
            else{
                transferFunds(recipientID,amountToTransfer);

                HashMap<String,String> responseMessage = new HashMap<>();
                responseMessage.put("message", "Amount Transfered Successfully");
                System.out.println("Amount Transfered Successfully");

                Gson gson = new Gson();
                String response = gson.toJson(responseMessage);
                resp.getWriter().println(response);
                resp.setStatus(200);
            }
        }catch (Exception e){
            e.printStackTrace();
            resp.setStatus(500);

            HashMap<String,String> responseMessage = new HashMap<>();
            responseMessage.put("message", e.getMessage());
            System.out.println("Could Not Transfer Funds");

            Gson gson = new Gson();
            String response = gson.toJson(responseMessage);
            resp.getWriter().println(response);
        }
    }

    private void transferFunds(int recipientID, Double amount) throws Exception{
        Connection connection = Database.getConnection();

        String query = "UPDATE users SET balance = balance + ? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(query);

        ps.setDouble(1, amount);
        ps.setInt(2, recipientID);

        if(ps.executeUpdate() < 1)
            throw new RuntimeException("Could not transfer funds");
    }
}