package school.hei.tsinjo.model.psp.vola;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import school.hei.tsinjo.model.Payment;
import school.hei.tsinjo.model.PaymentStatus;
import school.hei.tsinjo.model.psp.Psp;
import school.hei.tsinjo.model.psp.PspType;
import school.hei.tsinjo.model.psp.vola.api.VolaClient;
import school.hei.tsinjo.model.psp.vola.api.gen.client.model.PspPayment;

@Slf4j
@AllArgsConstructor
public class VolaPsp implements Psp {
  private final VolaClient volaClient;

  @Override
  public Payment create(String tsinjoId, PspType pspType, String pspId, String email) {
    var volaPayment = volaClient.create(pspType, pspId, email);
    return toPayment(tsinjoId, volaPayment);
  }

  @Override
  public Payment get(String tsinjoId, PspType pspType, String pspId, String email) {
    var volaPayment = volaClient.get(pspType, pspId, email);
    return toPayment(tsinjoId, volaPayment);
  }

  private Payment toPayment(
      String tsinjoId, school.hei.tsinjo.model.psp.vola.api.gen.client.model.Payment volaPayment) {

    if (volaPayment == null) {
      log.warn("Vola returned null for tsinjoId={}", tsinjoId);
      return new Payment(tsinjoId, null, null, null, PaymentStatus.VERIFYING, null, null);
    }

    PspPayment volaPspPayment = volaPayment.getPspPayment();

    Instant lastVerificationInstant =
        Optional.ofNullable(volaPayment.getLastPspVerificationInstant())
            .map(Date::toInstant)
            .orElse(null);

    Instant pspCreation =
        (volaPspPayment == null)
            ? null
            : Optional.ofNullable(volaPspPayment.getCreationInstant())
                .map(Date::toInstant)
                .orElse(null);

    Instant rootCreation =
        Optional.ofNullable(volaPayment.getCreationInstant()).map(Date::toInstant).orElse(null);

    Instant chosenCreation = pspCreation != null ? pspCreation : rootCreation;

    if (chosenCreation == null) chosenCreation = lastVerificationInstant;

    return new Payment(
        tsinjoId,
        (volaPspPayment == null ? null : volaPspPayment.getAmount()),
        (volaPspPayment == null ? null : toPspType(volaPspPayment.getPspType())),
        (volaPspPayment == null ? null : volaPspPayment.getId()),
        toPaymentStatus(volaPayment.getVerificationStatus()),
        lastVerificationInstant,
        chosenCreation);
  }

  private PspType toPspType(PspPayment.PspTypeEnum volaPspType) {
    if (volaPspType == null) return null;
    return switch (volaPspType) {
      case ORANGE_MONEY -> PspType.ORANGE_MONEY;
    };
  }

  private PaymentStatus toPaymentStatus(
      school.hei.tsinjo.model.psp.vola.api.gen.client.model.Payment.VerificationStatusEnum
          volaPaymentStatus) {
    if (volaPaymentStatus == null) return PaymentStatus.VERIFYING;
    return switch (volaPaymentStatus) {
      case VERIFYING -> PaymentStatus.VERIFYING;
      case SUCCEEDED -> PaymentStatus.CONFIRMED;
      case FAILED -> PaymentStatus.REFUSED;
    };
  }
}
