package school.hei.tsinjo.endpoint.http.model;

import static school.hei.tsinjo.model.psp.PspType.ORANGE_MONEY;

import school.hei.tsinjo.model.psp.PspType;

public record DonationCreationForm(
    String email, String firstName, String lastName, PspType pspType, String pspId) {

  public DonationCreationForm(String email, String firstName, String lastName, String pspId) {
    this(email, firstName, lastName, ORANGE_MONEY, pspId);
  }
}
