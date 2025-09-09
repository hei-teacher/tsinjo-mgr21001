package school.hei.tsinjo.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.tsinjo.endpoint.http.model.DonationCreationForm;
import school.hei.tsinjo.model.Donation;
import school.hei.tsinjo.model.Event;
import school.hei.tsinjo.model.Payment;
import school.hei.tsinjo.model.User;

public class DonationFormServiceTest {

  private EventService eventService;
  private DonationFormService donationFormService;

  @BeforeEach
  void setUp() {
    eventService = mock(EventService.class);
    donationFormService = new DonationFormService(eventService);
  }

  @Test
  void returns_empty_form_when_no_event_found() {
    when(eventService.findAllWithPaymentResolution()).thenReturn(List.of());

    DonationCreationForm form = donationFormService.getPrefillDonationForm("user@example.com");

    Assertions.assertEquals("", form.firstName());
    Assertions.assertEquals("", form.lastName());
    Assertions.assertEquals("", form.pspId());
  }

  @Test
  void returns_prefilled_form_with_last_event_user_data() {
    var user = new User("1", "Tiavina", "Andriamamivony", "user@example.com");
    var payment = new Payment("p1", 1000, null, "PSP123", null, Instant.now());
    Event event = new Donation("e1", payment, user, Instant.now());

    when(eventService.findAllWithPaymentResolution()).thenReturn(List.of(event));

    DonationCreationForm form = donationFormService.getPrefillDonationForm("user@example.com");

    Assertions.assertEquals("Tiavina", form.firstName());
    Assertions.assertEquals("Andriamamivony", form.lastName());
    Assertions.assertEquals("", form.pspId());
  }

  @Test
  void ignores_events_from_other_users() {
    var otherUser = new User("2", "Alice", "Wonderland", "alice@example.com");
    var payment = new Payment("p2", 500, null, "PSP456", null, Instant.now());
    Event event = new Donation("e2", payment, otherUser, Instant.now());

    when(eventService.findAllWithPaymentResolution()).thenReturn(List.of(event));

    DonationCreationForm form = donationFormService.getPrefillDonationForm("user@example.com");

    Assertions.assertEquals("", form.firstName());
    Assertions.assertEquals("", form.lastName());
    Assertions.assertEquals("", form.pspId());
  }
}
