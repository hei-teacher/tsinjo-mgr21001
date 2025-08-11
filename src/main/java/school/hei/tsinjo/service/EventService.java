package school.hei.tsinjo.service;

import static school.hei.tsinjo.model.PaymentStatus.VERIFYING;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.tsinjo.model.Event;
import school.hei.tsinjo.model.psp.vola.VolaPsp;
import school.hei.tsinjo.repository.EventRepository;

@Component
@AllArgsConstructor
public class EventService {

  private final EventRepository eventRepository;
  private final VolaPsp volaPsp;

  public List<Event> findAllWithPaymentResolution() {
    return eventRepository.findAllByOrderByCreationInstantDesc().stream()
        .map(this::resolvePayment)
        .toList();
  }

  private Event resolvePayment(Event event) {
    var payment = event.getPayment();
    if (VERIFYING.equals(payment.status())) {
      var resolvedPayment =
          volaPsp.get(payment.id(), payment.pspType(), payment.pspId(), event.getUser().getEmail());
      return eventRepository.save(event.withPayment(resolvedPayment));
    }

    return event;
  }
}
