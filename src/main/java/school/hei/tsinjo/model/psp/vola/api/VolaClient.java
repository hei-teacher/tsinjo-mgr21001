package school.hei.tsinjo.model.psp.vola.api;

import school.hei.tsinjo.model.psp.PspType;
import school.hei.tsinjo.model.psp.vola.api.gen.volaClient.api.PaymentControllerApi;
import school.hei.tsinjo.model.psp.vola.api.gen.volaClient.client.ApiClient;
import school.hei.tsinjo.model.psp.vola.api.gen.volaClient.client.ApiException;
import school.hei.tsinjo.model.psp.vola.api.gen.volaClient.model.Payment;

public class VolaClient {
  private final String apiKey;
  private final PaymentControllerApi paymentControllerApi;

  public VolaClient(String baseUrl, String apiKey) {
    this.apiKey = apiKey;
    var apiClient = new ApiClient();
    apiClient.updateBaseUri(baseUrl);
    this.paymentControllerApi = new PaymentControllerApi(apiClient);
  }

  public Payment create(PspType pspType, String pspId, String email, String scope)
      throws ApiException {
    return paymentControllerApi.createPayment(apiKey, email, pspType.toString(), pspId, scope);
  }

  public Payment get(PspType pspType, String pspId, String email, String scope)
      throws ApiException {
    return paymentControllerApi.getPayment(apiKey, email, pspType.toString(), pspId);
  }
}
