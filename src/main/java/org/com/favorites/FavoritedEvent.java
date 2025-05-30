package org.com.favorites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.com.login.entity.User;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
@Table(name = "favorited_events", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "eventId"})
})
public class FavoritedEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne  // Many events can belong to one user
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "event_id", nullable = false)
    private String eventId;


    private String title;
    private String host;
    private String date;
    private String time;
    private BigDecimal price;

    @Column(columnDefinition = "TEXT")
    private String img;

    @Column(columnDefinition = "TEXT")
    private String link;
}

