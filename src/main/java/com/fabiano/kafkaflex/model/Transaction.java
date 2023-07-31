package com.fabiano.kafkaflex.model;

import com.fabiano.kafkaflex.service.Retryable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction implements Serializable, Retryable {
  @Serial
  private static final long serialVersionUID = 864177672965759153L;
  private int id;
  @DecimalMin(message = "Invalid amount", value = "0.0", inclusive = false)
  private BigDecimal amount;
  @NotNull(message = "Field origin can not be null")
  private String origin;
  @NotNull(message = "Field destiny can not be null")
  private String destiny;
  private String date;
  @NotNull
  @Pattern(regexp = "normal|retry", flags = Pattern.Flag.CASE_INSENSITIVE)
  private String processingFlag;

  @Override
  public boolean isRetryable() {
    return this.processingFlag.equals("retry");
  }
}
