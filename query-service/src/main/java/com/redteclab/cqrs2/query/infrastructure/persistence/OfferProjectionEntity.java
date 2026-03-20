package com.redteclab.cqrs2.query.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "offer_projection")
public class OfferProjectionEntity {

    @Id
    @Column(name = "offer_id")
    private String offerId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private int version;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected OfferProjectionEntity() {
    }

    public OfferProjectionEntity(String offerId, String title, BigDecimal price,
                                  boolean active, int version, Instant updatedAt) {
        this.offerId = offerId;
        this.title = title;
        this.price = price;
        this.active = active;
        this.version = version;
        this.updatedAt = updatedAt;
    }

    public String getOfferId() {
        return offerId;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isActive() {
        return active;
    }

    public int getVersion() {
        return version;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
