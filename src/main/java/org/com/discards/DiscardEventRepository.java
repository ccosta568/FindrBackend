package org.com.discards;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiscardEventRepository extends JpaRepository<DiscardEvent, Long> {
    // Retrieve all discard events by user
    List<DiscardEvent> findByUserId(Long userId);

    // Check if an event with the given userId and eventId already exists
    boolean existsByUserIdAndEventId(Long userId, String eventId);
}

