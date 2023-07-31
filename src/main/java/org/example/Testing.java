package org.example;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Testing {

    public static void main(String[] args) throws FileNotFoundException{

        int id = 0, transactionid = 0;
        double total_amount_due = 1.00, amount = 1.00;
        String date = "1", description = "1", name = "1";

        HashMap<Integer, HashMap<Integer, HashMap<String, String>>> statements = new HashMap<>();
        HashMap<Integer, HashMap<String, String>> statement_data = new HashMap<>();

        // Accessing data from json file
        File jsonFile = new File("/Users/se/Documents/Programming/Playwright/temp/src/main/resources/data.json");
        FileInputStream json_data = null;
        JsonNode jsonNode = null;
        try {
            json_data = new FileInputStream(jsonFile);
            ObjectMapper objectMapper = new ObjectMapper();
            jsonNode = objectMapper.readTree(json_data);

            JsonNode statementsArray = jsonNode.get("statements");
            if (statementsArray.isArray()) {
                for (JsonNode statementNode : statementsArray) {
                    
                    HashMap<String, String> data = new HashMap<>();

                    id = statementNode.get("id").asInt();
                    name = statementNode.get("name").asText();
                    total_amount_due = statementNode.get("total_amount_due").asDouble();

                    data.put("name", name);
                    data.put("total_amount_due", String.valueOf(total_amount_due));

                    HashMap<Integer, HashMap<String, String>> transactions = new HashMap<>();

                    JsonNode transactionsArray = statementNode.get("transactions");
                    if (transactionsArray.isArray()) {
                        for (JsonNode transactionNode : transactionsArray) {

                            HashMap<String, String> transaction_data = new HashMap<>();

                            transactionid = transactionNode.get("Tid").asInt();
                            date = transactionNode.get("date").asText();
                            description = transactionNode.get("description").asText();
                            amount = transactionNode.get("amount").asDouble();

                            transaction_data.put("date",date);
                            transaction_data.put("description",description);
                            transaction_data.put("amount",String.valueOf(amount));

                            transactions.put(transactionid, transaction_data);
                        }
                    }

                    statements.put(id, transactions);

                    statement_data.put(id,data);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (json_data != null) {
                try {
                    json_data.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println(statements);
        System.out.println(statement_data);


        // String localFilePath = "/Users/se/Documents/Programming/Playwright/temp/src/main/resources/statement.html";

        // Playwright playwright = Playwright.create();
        // Browser browser = playwright.chromium().launch();

        // // open a page to navigate through pages
        // Page page = browser.newPage();
        // page.navigate("file://" + localFilePath);

        // // Change the value using ids
        // for (int i=0; i<ids.size();i++){
        //     page.evaluate("document.querySelector('#"+ids.get(i)+"').textContent = 'Shubham Shreshth Credit Card Statement';");
        // }

        // // page.setContent(localFilePath);
        // page.pdf(new Page.PdfOptions().setPath(Paths.get("statement.pdf")));

        // // closing statements after the execution is completed
        // page.close();
        // browser.close();
        // playwright.close();

        // evaluateonnewdocument
        // addscripttag
    }
}
