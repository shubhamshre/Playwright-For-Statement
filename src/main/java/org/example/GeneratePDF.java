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

public class GeneratePDF {

    public static void main(String[] args) throws FileNotFoundException{

        // Local variable assignment for outside access
        int id = 0, transactionid = 0;
        double total_amount_due = 1.00, amount = 1.00;
        String date = "1", description = "1", name = "1";

        // Defining the path of the HTML
        String localFilePath = "/Users/se/Documents/Programming/Playwright/temp/src/main/resources/statement.html";

        // Playwright assignment
        Playwright playwright = Playwright.create();
        // Launching the browser
        Browser browser = playwright.chromium().launch();

        // Defining the path of json file
        File jsonFile = new File("/Users/se/Documents/Programming/Playwright/temp/src/main/resources/data.json");
        FileInputStream json_data = null;
        JsonNode jsonNode = null;
        
        try {

            // Trying to access the file using ObjectMapper of Jackson library
            json_data = new FileInputStream(jsonFile);
            ObjectMapper objectMapper = new ObjectMapper();
            jsonNode = objectMapper.readTree(json_data);

            // Accessing the json file
            JsonNode statementsArray = jsonNode.get("statements");
            if (statementsArray.isArray()) {
                for (JsonNode statementNode : statementsArray) {

                    // assignment of variables of json to local variables
                    id = statementNode.get("id").asInt();
                    name = statementNode.get("name").asText();
                    total_amount_due = statementNode.get("total_amount_due").asDouble();

                    // calling new browser page using playwright
                    Page page = browser.newPage();
                    // navigating to the HTML file
                    page.navigate("file://" + localFilePath);

                    // assigning the values of json to HTML ids using evaluate function of page in playwright
                    page.evaluate("document.querySelector('#username').textContent = '"+name+"';");
                    page.evaluate("document.querySelector('#total_amount_due').textContent = '"+total_amount_due+"';");

                    // variable for multiple transactions
                    int i=1;

                    JsonNode transactionsArray = statementNode.get("transactions");
                    if (transactionsArray.isArray()) {
                        for (JsonNode transactionNode : transactionsArray) {

                            // assigning the values of json to HTML ids using evaluate function of page in playwright
                            transactionid = transactionNode.get("Tid").asInt();
                            date = transactionNode.get("date").asText();
                            description = transactionNode.get("description").asText();
                            amount = transactionNode.get("amount").asDouble();

                            // assigning the values of json to HTML ids using evaluate function of page in playwright
                            page.evaluate("document.querySelector('#transaction"+i+"-id').textContent = '"+transactionid+"';");
                            page.evaluate("document.querySelector('#transaction"+i+"-date').textContent = '"+date+"';");
                            page.evaluate("document.querySelector('#transaction"+i+"-desc').textContent = '"+description+"';");
                            page.evaluate("document.querySelector('#transaction"+i+"-amt').textContent = '"+amount+"';");

                            i++;
                        }
                    }

                    // pdf conversion
                    page.pdf(new Page.PdfOptions().setPath(Paths.get("/Users/se/Documents/Programming/Playwright/temp/src/main/pdfs/"+name+".pdf")));
                    // closing the page to avoid memory leaks
                    page.close();
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

        // closing browser and playwright to avoid un-neccesary usage of memory
        browser.close();
        playwright.close();
    }
}
