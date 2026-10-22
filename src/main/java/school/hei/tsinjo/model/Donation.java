package school.hei.tsinjo.model;

import java.time.Instant;

public final class Donation extends Event {
  public Donation(String id, Payment payment, User user, Instant creationInstant) {
    this(id, payment, user, creationInstant, "");
  }

  public Donation(String id, Payment payment, User user, Instant creationInstant, String comment) {
    super(id, payment, user, creationInstant, comment);
  }

  @Override
  public Event withPayment(Payment newPayment) {
    return new Donation(id, newPayment, user, creationInstant, comment);
  }
}
