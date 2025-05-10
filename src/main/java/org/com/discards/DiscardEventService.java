package org.com.discards;

import org.com.login.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscardEventService {

    @Autowired
    private DiscardEventRepository discardEventRepository;

    public void discardEvent(User user, DiscardEvent discardEvent) {
        if (discardEvent.getEventId() == null|| discardEvent.getEventId().isEmpty()) {
            // Set the event link using the discardEvent's link field
            throw new IllegalArgumentException("Discarded event must have a EventId");
        }

        if (!discardEventRepository.existsByUserIdAndEventId(user.getId(), discardEvent.getEventId())) {
            discardEvent.setUser(user);
            discardEventRepository.save(discardEvent);
        }
    }

    public List<DiscardEvent> getDiscardsForUser(Long userId) { // ✨ pass userId
        return discardEventRepository.findByUserId(userId);
    }
}


