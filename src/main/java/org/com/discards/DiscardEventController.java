package org.com.discards;

import org.com.login.entity.User;
import org.com.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discards")
public class DiscardEventController {

    @Autowired
    private DiscardEventService discardEventService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public void discardEvent(@RequestBody DiscardEvent discardEvent) {
        String username = getAuthenticatedUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        discardEventService.discardEvent(user, discardEvent);
    }

    @GetMapping
    public List<DiscardEvent> getMyDiscards() {
        String username = getAuthenticatedUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return discardEventService.getDiscardsForUser(user.getId()); // ✨ pass user.getId()
    }

    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }
}
