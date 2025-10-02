package school.hei.tsinjo.service.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;
import org.junit.jupiter.api.Test;
import school.hei.tsinjo.model.Payment;
import school.hei.tsinjo.model.psp.PspType;
import school.hei.tsinjo.model.psp.vola.VolaPsp;
import school.hei.tsinjo.model.psp.vola.api.VolaClient;

public class VolaPspTest {

  @Test
  void creationInstant_should_match_between_vola_and_tsinjo() {

    String baseUrl = System.getenv("VOLA_API_URL");
    String apiKey = System.getenv("VOLA_API_KEY");

    String tsinjoId = "tsinjo-test";
    String pspId = "MP250805.0922.B95953";
    String email = "ninah@mail.hei.school";

    VolaClient volaClient = new VolaClient(baseUrl, apiKey);
    VolaPsp volaPsp = new VolaPsp(volaClient);

    var raw = volaClient.get(PspType.ORANGE_MONEY, pspId, email);
    assertNotNull(raw, "Vola API returned null");
    assertNotNull(raw.getPspPayment().getCreationInstant(), "Creation instant should not be null");
    Instant rawCreation = raw.getPspPayment().getCreationInstant().toInstant();

    Payment mapped = volaPsp.get(tsinjoId, PspType.ORANGE_MONEY, pspId, email);
    assertNotNull(mapped, "Mapped Payment is null");
    assertNotNull(mapped.creationInstant(), "Payment creation instant mapped should not be null");
    assertEquals(
        rawCreation,
        mapped.creationInstant(),
        "❌ creationInstant mismatch: raw="
            + rawCreation
            + " vs mapped="
            + mapped.creationInstant());

    System.out.println("✅ creationInstant matches: " + rawCreation);
  }
}
