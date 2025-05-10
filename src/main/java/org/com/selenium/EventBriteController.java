package org.com.selenium;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class EventBriteController {
    private final EventBriteScraper eventBriteScraper;
    private List<String> cachedEvents = null; // Cache to store results

    public EventBriteController(EventBriteScraper eventBriteScraper) {
        this.eventBriteScraper = eventBriteScraper;
    }

    @GetMapping("/eventbrite")
    public List<Event> getEventbriteEvents(@RequestParam("zipcode") String zipcode) {
        List<Event> events = eventBriteScraper.getEventbriteEvents(zipcode);
        return events.stream()
                .filter(event -> event.getTitle() != null && !event.getTitle().trim().isEmpty()) // Remove empty events
                .collect(Collectors.toList()); // Return full event objects
    }
}
