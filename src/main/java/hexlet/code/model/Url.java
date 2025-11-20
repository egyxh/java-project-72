package hexlet.code.model;

import java.time.Instant;

public class Url {
    private Long id;
    private String name;
    private Instant createdAt;

    public Url() {
    }

    public Url(String name) {
        this.name = name;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
