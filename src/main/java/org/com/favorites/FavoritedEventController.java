package org.com.favorites;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoritedEventController {

    private final FavoritedEventService service;

    public FavoritedEventController(FavoritedEventService service) {
        this.service = service;
    }

    @GetMapping
    public List<FavoritedEvent> getFavorites() {
        // Get the username from the authenticated user
        String username = getAuthenticatedUsername();
        return service.getFavoritesForUser(username);
    }

    @PostMapping
    public FavoritedEvent addFavorite(@RequestBody FavoritedEvent event) {
        // Get the username from the authenticated user
        String username = getAuthenticatedUsername();
        return service.saveFavorite(username, event);
    }

    @DeleteMapping("/{eventId}")
    public void deleteFavorite(@PathVariable Long eventId) throws Exception {
        // Get the username from the authenticated user
        String username = getAuthenticatedUsername();
        boolean deleted = service.deleteFavorite(username, eventId);

        if (!deleted) {
            throw new Exception();
        }
    }

    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null; // Get the username from the token
    }
}

