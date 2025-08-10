package school.hei.tsinjo.endpoint.http.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DonationCreationForm {
  String email;
  String firstName;
  String lastName;

  String orangePaymentReference;
}
