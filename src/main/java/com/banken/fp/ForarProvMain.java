package com.banken.fp;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class ForarProvMain {
    private static final String URL = "https://fp.trafikverket.se/Boka/#/search/19850801-5461/5/0";
    private static final long GENERAL_TIMEOUT = 20;
    private static final String LANGUAGE_SELECT_XPATH = "//*[@id=\"language-select\"]";
    private static final String VEHICLE_SELECT_XPATH = "//*[@id=\"vehicle-select\"]";
    private static final String LOCATION_INPUT_XPATH = "//*[@id=\"id-control-searchText\"]";
    private static final String LOCATIONS = "Alingsås, Arjeplog, Arvidsjaur, Arvika, Avesta, Boden, Bollnäs, Borlänge, Borås, Eksjö, Enköping, Eskilstuna, Eslöv, Fagersta, Falkenberg, Falköping, Falun, Farsta, Filipstad, Finspång, Gislaved, Gällivare, Gävle, Göteborg Högsbo, Göteborg-Mölndal, Göteborg-Hisingen, Hallsberg, Halmstad, Hammarstrand, Haparanda, Hedemora, Helsingborg, Hudiksvall, Härnösand, Hässleholm, Jakobsberg, Jokkmokk, Järpen, Jönköping, Kalix, Kalmar, Karlshamn, Karlskoga, Karlskrona, Kinna, Kiruna, Kisa, Kungsbacka, Kungälv, Köping, Landskrona, Lidköping, Lindesberg, Linköping, Ljungby, Ljusdal, Ludvika, Luleå, Lund, Lidköping, Lindesberg, Linköping, Lycksele, Lysekil, Malmö, Malung, Mariestad, Mjölby, Mora, Motala, Norrköping, Norrtälje 2, Nybro, Nyköping, Nynäshamn, Nässjö, Olofström, Oskarshamn, Pajala, Piteå, Ronneby, Sala, Sandviken, Simrishamn, Skellefteå, Skövde, Sollefteå, Sollentuna, Stockholm City, Strängnäs, Strömstad, Strömsund, Sundsvall, Sunne, Sveg, Säffle, Söderhamn, Södertälje, Sölvesborg, TEST ANVÄNDS EJ!, Tranås, Trelleborg, Uddevalla, Ulricehamn, Umeå, Uppsala, Varberg, Vetlanda, Vilhelmina, Vimmerby, Vindeln, Vänersborg, Växjö, Värnamo, Västervik, Västerås, Visby, Ystad, Åmål, Ånge, Älmhult, Älvsbyn, Ängelholm, Örebro, Örnsköldsvik, Östersund, Östhammar, Övertorneå";

    private static final String DATA_XPATH = "/html/body/div[2]/section[5]/div/div[9]";

    private final List<String> slots = new ArrayList<>();

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "/home/banken/development/libs/seleniumstuff/chromedriver");

        WebDriver driver = new ChromeDriver();

        ForarProvMain forarProvMain = new ForarProvMain();

        forarProvMain.printLocations(driver);
    }

    private void printLocations(WebDriver driver) {
        driver.get(URL);


        waitForVisibilityByXpath(driver, LOCATION_INPUT_XPATH);
        waitForVisibilityByXpath(driver, LANGUAGE_SELECT_XPATH);
        waitForVisibilityByXpath(driver, VEHICLE_SELECT_XPATH);

        selectSwedish(driver);
        selectRentCar(driver);

        findAvailableSlots(driver);

        slots.sort(String::compareTo);


        slots.forEach(System.out::println);
        System.out.println("slots = " + slots);
    }

    private void findAvailableSlots(WebDriver driver) {
        String[] split = LOCATIONS.split(", ");

        for (String location : split) {
            System.out.println("split = " + location);

            WebElement locationInput = driver.findElement(By.xpath(LOCATION_INPUT_XPATH));
            locationInput.clear();
            locationInput.sendKeys(location);
            locationInput.sendKeys("\n");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            WebElement data = driver.findElement(By.xpath(DATA_XPATH));

            List<WebElement> children = data.findElements(By.tagName("div"));
            if (children.isEmpty()) {
                continue;
            }
            try {
                WebElement firstSlot = children.get(0);
                WebElement dateAndTimeElementKnowledge = firstSlot.findElement(By.xpath("div/div/div[1]/div[1]/div[1]/strong"));
                String text1 = dateAndTimeElementKnowledge.getText();
                WebElement dateAndTimeDriving = firstSlot.findElement(By.xpath("div/div/div[1]/div[2]/div[1]/strong"));
                String text2 = dateAndTimeDriving.getText();
                String result = "Driving " + text2 + " knowledge " + text1 + " in " + location;
                System.out.println(result);
                slots.add(result);
            } catch (Exception ex) {
                System.out.println("Error when cheching " + location + ", assumint no slots available.");
            }
        }
    }


    private void selectRentCar(WebDriver driver) {
        WebElement element = driver.findElement(By.xpath(VEHICLE_SELECT_XPATH));
        Select select = new Select(element);
        select.selectByVisibleText("Ja, manuell");
    }

    private void selectSwedish(WebDriver driver) {
        WebElement element = driver.findElement(By.xpath(LANGUAGE_SELECT_XPATH));
        Select select = new Select(element);
        select.selectByVisibleText("Svenska");
    }

    private void waitForVisibilityByXpath(WebDriver driver, String xpath) {
        new WebDriverWait(driver, GENERAL_TIMEOUT).
                until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));

    }

//    private void searchLocation(WebDriver driver) {
//        WebElement locationInput = driver.findElement(By.xpath(LOCATION_INPUT_XPATH));
//
//        List<String> locations = new ArrayList<>();
//
//        for (int i = 0; i < START_LETTERS.length(); i++) {
//            locations.addAll(getLocations(locationInput, START_LETTERS.charAt(i)));
//        }
//
//        System.out.println("locations = " + locations);
//    }

//    private List<String> getLocations(WebElement locationInput, char startChar) {
//        locationInput.clear();
//        locationInput.sendKeys(startChar + "");
//
//        WebElement listOfLocations = locationInput.findElement(By.xpath("..")).findElement(By.tagName("ul"));
//        List<WebElement> locations = listOfLocations.findElements(By.tagName("li"));
//        List<String> locationsS = locations.stream().map(locationElement -> {
//            WebElement a = locationElement.findElement(By.tagName("a"));
//            String text = a.getText();
//            return text;
//        }).collect(Collectors.toList());
//
//        return locationsS;
//    }
}
