package school.hei.tsinjo.model.psp;

import school.hei.tsinjo.model.Payment;
import school.hei.tsinjo.model.psp.vola.api.gen.volaClient.client.ApiException;

public interface Psp {
  Payment create(String tsinjoId, PspType pspType, String pspId, String email, String scope);

  Payment get(String tsinjoId, PspType pspType, String pspId, String email, String scope)
      throws ApiException;
}
