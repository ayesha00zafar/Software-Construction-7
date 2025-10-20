package twitter;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

/**
 * Minimal Tweet class compatible with tests that pass either Date or Instant.
 */
public final class Tweet {
    private final long id;
    private final String author;
    private final String text;
    private final Date timestamp; // store as java.util.Date for compatibility

    // Constructor used by tests which pass java.util.Date
    public Tweet(long id, String author, String text, Date timestamp) {
        this.id = id;
        this.author = Objects.requireNonNull(author, "author");
        this.text = Objects.requireNonNull(text, "text");
        this.timestamp = new Date( Objects.requireNonNull(timestamp, "timestamp").getTime() ); // defensive copy
    }

    // Convenience constructor for tests that pass Instant
    public Tweet(long id, String author, String text, Instant timestamp) {
        this(id, author, text, Date.from(Objects.requireNonNull(timestamp, "timestamp")));
    }

    public long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    // If tests expect Date, this returns Date. If they expect Instant, they can call getTimestamp().toInstant()
    public Date getTimestamp() {
        return new Date(timestamp.getTime()); // defensive copy
    }
}

