package school.hei.tsinjo.service;

import static school.hei.tsinjo.model.PaymentStatus.VERIFYING;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import school.hei.tsinjo.model.Event;
import school.hei.tsinjo.model.psp.vola.VolaPsp;
import school.hei.tsinjo.repository.EventRepository;

@Component
@Slf4j
public class EventService {

  private final EventRepository eventRepository;
  private final VolaPsp volaPsp;
  private final String scope;

  public EventService(
      EventRepository eventRepository, VolaPsp volaPsp, @Value("SCOPE") String scope) {
    this.eventRepository = eventRepository;
    this.volaPsp = volaPsp;
    this.scope = scope;
  }

  public List<Event> findAllWithPaymentResolution() {
    return eventRepository.findAllByOrderByCreationInstantDesc().stream()
        .map(this::resolvePayment)
        .toList();
  }

  private Event resolvePayment(Event event) {
    var payment = event.getPayment();
    if (!VERIFYING.equals(payment.status())) {
      return event;
    }

    var resolvedPayment = payment;
    try {
      resolvedPayment =
          volaPsp.get(
              payment.id(), payment.pspType(), payment.pspId(), event.getUser().getEmail(), scope);
    } catch (Exception e) {
      log.error("Could not resolve payment for event: {}", event);
      return event;
    }
    return eventRepository.save(event.withPayment(resolvedPayment));
  }
}
