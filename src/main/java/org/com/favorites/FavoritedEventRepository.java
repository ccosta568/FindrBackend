package org.com.favorites;

import org.com.login.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritedEventRepository extends JpaRepository<FavoritedEvent, Long> {
    // Retrieve all favorite events by user
    List<FavoritedEvent> findByUser(User user);

    // Find a favorite event by its ID and user
    Optional<FavoritedEvent> findByIdAndUser(Long id, User user);

    // Check if an event with the given userId and eventId already exists
    boolean existsByUserIdAndEventId(Long userId, String eventId);
}
