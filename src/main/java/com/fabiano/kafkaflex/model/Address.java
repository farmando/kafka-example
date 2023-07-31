package com.fabiano.kafkaflex.model;

import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address implements Serializable {
  @Serial
  private static final long serialVersionUID = -5502527933212406489L;
  @NotNull(message = "Street can not be empty")
  private String street;
  private String complement;
  private String number;
  @NotNull(message = "Zipcode can not be empty")
  private String zipCode;
}
