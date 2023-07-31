package org.example;

import com.microsoft.playwright.*;

public class Main {
    public static void main(String[] args) {
        // Initialize playwright object
        Playwright playwright = Playwright.create();
        // Launch browser to interact. In this case we are opening local browser chrome
        // Browser browser = playwright.chromium().launch(
        //     new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50).setChannel("chrome"));

        Browser browser = playwright.webkit().launch(
            new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));

        // open a page to navigate through pages
        Page page1 = browser.newPage();
        page1.navigate("http://www.wikipedia.com");

        // print title of the page
        System.out.println("Title: "+page1.title());
        System.out.println("URL: "+page1.url());

        // closing statements after the execution is completed
        // page.close();
        // browser.close();
        // playwright.close();
    }
}