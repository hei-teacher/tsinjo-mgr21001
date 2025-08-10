package school.hei.tsinjo.endpoint.http;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import school.hei.tsinjo.endpoint.http.model.DonationCreationForm;
import school.hei.tsinjo.repository.EventRepository;
import school.hei.tsinjo.service.DonationCreationFormConsumer;

@Slf4j
@Controller
@AllArgsConstructor
public class TsinjoController {

  private final EventRepository eventRepository;
  private final DonationCreationFormConsumer donationCreationFormConsumer;

  @GetMapping("/")
  public String getEvents(@ModelAttribute DonationCreationForm donationCreationForm, Model model) {
    model.addAttribute("events", eventRepository.findAll());
    return "events";
  }

  @PostMapping("/donate")
  public String submitForm(@ModelAttribute DonationCreationForm donationCreationForm) {
    log.info(donationCreationForm.toString());
    donationCreationFormConsumer.accept(donationCreationForm);
    return "redirect:/";
  }
}
