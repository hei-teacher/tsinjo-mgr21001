package school.hei.tsinjo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.tsinjo.endpoint.http.model.DonationCreationForm;
import school.hei.tsinjo.endpoint.http.model.ThEvent;

@Service
@AllArgsConstructor
public class DonationFormService {

  private final EventService eventService;

  public DonationCreationForm getPrefillDonationForm(String email) {
    var events = eventService.findAllWithPaymentResolution();
    var thEvent = events.stream().map(ThEvent::new).toList();

    var lastEvent =
        thEvent.stream()
            .filter(e -> e.event().getUser().getEmail().equals(email))
            .reduce((first, second) -> second)
            .orElse(null);

    if (lastEvent != null) {
      return new DonationCreationForm(
          lastEvent.event().getUser().getFirstName(),
          lastEvent.event().getUser().getLastName(),
          "");
    } else {
      return new DonationCreationForm("", "", "");
    }
  }
}
