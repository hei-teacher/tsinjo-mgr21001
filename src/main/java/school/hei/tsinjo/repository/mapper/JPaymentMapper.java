package school.hei.tsinjo.repository.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import school.hei.tsinjo.model.Payment;
import school.hei.tsinjo.model.psp.PspType;
import school.hei.tsinjo.repository.jpa.model.JPayment;

@Slf4j
@Component
public class JPaymentMapper {
  public Payment toDomain(JPayment jPayment) {
    return new Payment(
        jPayment.getId(),
        jPayment.getAmount(),
        PspType.ORANGE_MONEY,
        jPayment.getPspId(),
        jPayment.getStatus(),
        jPayment.getPspLastVerificationInstant(),
        jPayment.getCreationInstant());
  }

  public JPayment toEntity(Payment payment) {
    JPayment j =
        new JPayment(
            payment.id(),
            payment.amount(),
            payment.status(),
            payment.pspId(),
            payment.pspLastVerificationInstant(),
            payment.creationInstant());

    log.info(
        "JPayment to persist: id={} pspId={} creationInstant={} lastVerification={}",
        j.getId(),
        j.getPspId(),
        j.getCreationInstant(),
        j.getPspLastVerificationInstant());

    return j;
  }
}
