package com.fabiano.kafkaflex.model;

import com.fabiano.kafkaflex.service.Retryable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class Customer implements Serializable, Retryable {
  @Serial
  private static final long serialVersionUID = 3841958614891245654L;
  @NotNull(message = "Name can not be empty")
  @Size(min = 2, max = 50)
  private String name;
  @Email(message = "Invalid e-mail", regexp = ".+[@].+[\\.].+")
  private String email;
  @NotNull
  private String phone;
  @Valid
  @NotNull
  private Address address;
  @NotNull(message = "Country can not be empty")
  private String country;
  @NotNull
  @Pattern(regexp = "normal|retry", flags = Pattern.Flag.CASE_INSENSITIVE)
  private String processingFlag;

  @Override
  public boolean isRetryable() {
    return this.processingFlag.equals("retry");
  }
}
