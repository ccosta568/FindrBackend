package org.com.favorites;

import org.com.login.entity.User;
import org.com.login.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoritedEventService {
    private final FavoritedEventRepository repository;
    private final UserRepository userRepository;

    public FavoritedEventService(FavoritedEventRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<FavoritedEvent> getFavoritesForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return repository.findByUser(user);
    }

    public FavoritedEvent saveFavorite(String username, FavoritedEvent event) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if the event already exists for this user
        boolean exists = repository.existsByUserIdAndEventId(user.getId(), event.getEventId());
        if (exists) {
            throw new IllegalArgumentException("Event already saved to favorites");
        }
        // Set the user for the event
        event.setUser(user);
        return repository.save(event);
    }

    // Add the method to delete a favorite event
    public boolean deleteFavorite(String username, Long eventId) {
        // Find the event by ID and username
        FavoritedEvent event = findFavoriteEventByIdAndUser(eventId, username);

        if (event != null) {
            // Perform the delete operation (e.g., remove from the database)
            repository.delete(event); // Delete the event
            return true;
        }

        return false; // If the event was not found or user is not authorized
    }

    private FavoritedEvent findFavoriteEventByIdAndUser(Long eventId, String username) {
        // Look up the event by ID and ensure it belongs to the user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return repository.findByIdAndUser(eventId, user)
                .orElseThrow(() -> new IllegalArgumentException("Event not found or not authorized"));
    }
}

