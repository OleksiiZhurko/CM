package com.cii3m.cm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ErosionPoint {
  private int maxSequenceMinusOne;
  private int countSequenceMinusOne;
  private int maxSequenceZero;
  private int countSequenceZero;
  private int maxSequenceOne;
  private int countSequenceOne;
  private int maxSequenceTwo;
  private int countSequenceTwo;
}
