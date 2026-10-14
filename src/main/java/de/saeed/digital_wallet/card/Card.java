package de.saeed.digital_wallet.card;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "cards")
public class Card {

    @Id
    private UUID id;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Column(name = "last_four", nullable = false, length = 4)
    private String lastFour;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CardStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected Card() {
        // Required by JPA
    }

    public Card(
            String displayName,
            String lastFour
    ) {
        this.id = UUID.randomUUID();
        this.displayName = displayName;
        this.lastFour = lastFour;
        this.status = CardStatus.ACTIVE;
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getLastFour() {
        return lastFour;
    }

    public CardStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void block() {
        this.status = CardStatus.BLOCKED;
    }

    public void activate() {
        this.status = CardStatus.ACTIVE;
    }
}

