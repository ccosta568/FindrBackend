package org.com.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EventBriteScraper {

    private static final String EVENTBRITE_URL = "https://www.eventbrite.com";
    private static final long CACHE_EXPIRY_TIME = 30 * 60 * 1000; // 30 minutes
    private static final Map<String, CacheEntry> eventCache = new ConcurrentHashMap<>();

    public List<Event> getEventbriteEvents(String location) {
        CacheEntry cachedData = eventCache.get(location);
        if (cachedData != null && !isCacheExpired(cachedData.timestamp)) {
            return cachedData.events;
        }

        List<Event> events = scrapeEvents(location);
        eventCache.put(location, new CacheEntry(events, System.currentTimeMillis()));
        return events;
    }

    private List<Event> scrapeEvents(String location) {
        List<Event> events = new ArrayList<>();

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
     //   options.addArguments("--headless=new"); // Enable this for headless mode
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-gpu");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));

        // Disable image loading to improve performance
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.managed_default_content_settings.images", 2);
        options.setExperimentalOption("prefs", prefs);

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3)); //try to speed up

        try {
            String zipSlug = location.replaceAll("\\s+", "-").toLowerCase();
            String searchUrl = EVENTBRITE_URL + "/d/" + zipSlug + "/events/";
            driver.get(searchUrl);

            try {
                WebElement exploreMoreLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.see-more-link")));
                exploreMoreLink.click();
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h3.Typography_root__487rx")));
                System.out.println("Clicked 'Explore more events' link.");
            } catch (TimeoutException e) {
                System.out.println("Explore more events link not found or clickable — continuing anyway.");
            }

            Thread.sleep(500); // Let results load try to speed this up

            List<WebElement> eventTitles = driver.findElements(By.cssSelector("h3.Typography_root__487rx"));
            List<WebElement> eventPrices = driver.findElements(By.xpath("//div[contains(@class, 'priceWrapper')]//p"));
            List<WebElement> eventPlaces = driver.findElements(By.xpath("//div[@class='Stack_root__1ksk7']//p[not(contains(text(),'•'))]"));
            List<WebElement> eventTimes = driver.findElements(By.xpath("//p[contains(@class, 'Typography_root__487rx') and contains(text(), '•')]"));
            List<WebElement> eventImages = driver.findElements(By.cssSelector("a.event-card-link img.event-card-image"));
            List<WebElement> eventLinks = driver.findElements(By.xpath("//div[contains(@class, 'Stack_root__1ksk7')]//a[contains(@class, 'event-card-link')]"));

            for (int i = 0; i < eventTitles.size(); i++) {
                String title = eventTitles.get(i).getText();
                String priceText = (i < eventPrices.size()) ? eventPrices.get(i).getText() : "Free";
                double price = parsePrice(priceText);

                String date = "Date not available";
                String time = "Time not available";
                String host = "Host not available";

                try {
                    WebElement card = eventLinks.get(i).findElement(By.xpath("ancestor::div[contains(@class, 'Stack_root__1ksk7')]"));
                    List<WebElement> lines = card.findElements(By.cssSelector("p[class*='event-card__clamp-line--one']"));
                    if (lines.size() >= 2) {
                        String[] dateTime = parseDateAndTime(lines.get(0).getText());
                        date = dateTime[0];
                        time = dateTime[1];
                        host = lines.get(1).getText();
                    } else if (lines.size() == 1) {
                        String[] dateTime = parseDateAndTime(lines.get(0).getText());
                        date = dateTime[0];
                        time = dateTime[1];
                    }
                } catch (Exception e) {
                    System.out.println("Failed to extract lines for card " + i + ": " + e.getMessage());
                }

                // Fallback time from separate element
                if ("Time not available".equals(time)) {
                    time = parseTimeFromElement(eventTimes, i);
                }

                String img = (i < eventImages.size()) ? eventImages.get(i).getAttribute("src") : "https://example.com/default-image.jpg";
                String link = (i < eventLinks.size()) ? eventLinks.get(i).getAttribute("href") : "https://example.com/default-link";

                String eventId = EventIdGenerator.generateEventId(title, date);
                events.add(new Event(eventId, title, host, time, date, img, price, link));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
             driver.quit(); // Uncomment when running outside development
        }

        return events;
    }

    private boolean isCacheExpired(long timestamp) {
        return System.currentTimeMillis() - timestamp > CACHE_EXPIRY_TIME;
    }

    private String[] parseDateAndTime(String text) {
        String[] parts = text.split("•");
        String date = parts[0].trim();
        String time = (parts.length > 1) ? parts[1].trim() : "Time not available";
        return new String[]{date, time};
    }

    private String parseTimeFromElement(List<WebElement> timeElements, int index) {
        if (index < timeElements.size()) {
            String[] parts = timeElements.get(index).getText().split("•");
            return (parts.length > 1) ? parts[1].trim() : "Time not available";
        }
        return "Time not available";
    }

    private double parsePrice(String priceText) {
        try {
            String numericPrice = priceText.replaceAll("[^\\d.]", "");
            return Double.parseDouble(numericPrice);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private static class CacheEntry {
        List<Event> events;
        long timestamp;

        CacheEntry(List<Event> events, long timestamp) {
            this.events = events;
            this.timestamp = timestamp;
        }
    }
}
