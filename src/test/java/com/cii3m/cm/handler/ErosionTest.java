package com.cii3m.cm.handler;

import com.cii3m.cm.dto.ErosionPoint;
import com.cii3m.cm.handler.converter.Erosion;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErosionTest {

  @ParameterizedTest
  @MethodSource("countSequencesData")
  void testCountSequences(int[] input, ErosionPoint expected) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Erosion instance = new Erosion();
    Method method = Erosion.class.getDeclaredMethod("countSequences", int[].class);
    method.setAccessible(true);
    ErosionPoint actual = (ErosionPoint) method.invoke(instance, input);
    assertEquals(expected.toString(), actual.toString());
  }

  @ParameterizedTest
  @MethodSource("isEmptyData")
  void testIsEmpty(int[] erodes, ErosionPoint erosionPoint, boolean expected) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Erosion instance = new Erosion();
    Method method = Erosion.class.getDeclaredMethod("isEmpty", int[].class, ErosionPoint.class);
    method.setAccessible(true);
    boolean actual = (boolean) method.invoke(instance, erodes, erosionPoint);
    assertEquals(expected, actual);
  }

  private static Stream<Arguments> countSequencesData() {
    return Stream.of(
        Arguments.of(new int[] {1, 1, 0, 0, 0, 0, 0, 1},
            ErosionPoint.builder()
                .countSequenceZero(1)
                .maxSequenceZero(5)
                .countSequenceOne(1)
                .maxSequenceOne(3)
                .build()),
        Arguments.of(new int[] {1, 0, -1, 0, 0, 0, 0, 1},
            ErosionPoint.builder()
                .countSequenceMinusOne(1)
                .maxSequenceMinusOne(1)
                .countSequenceZero(2)
                .maxSequenceZero(4)
                .countSequenceOne(1)
                .maxSequenceOne(2)
                .build()),
        Arguments.of(new int[] {0, 0, -1, -1, -1, 0, 0, 1},
            ErosionPoint.builder()
                .countSequenceMinusOne(1)
                .maxSequenceMinusOne(3)
                .countSequenceZero(2)
                .maxSequenceZero(2)
                .countSequenceOne(1)
                .maxSequenceOne(1)
                .build()),
        Arguments.of(new int[] {-1, -1, 1, 1, 1, 1, 1, -1},
            ErosionPoint.builder()
                .countSequenceMinusOne(1)
                .maxSequenceMinusOne(3)
                .countSequenceOne(1)
                .maxSequenceOne(5)
                .build()),
        Arguments.of(new int[] {0, -1, 1, -1, -1, -1, -1, 0},
            ErosionPoint.builder()
                .countSequenceMinusOne(2)
                .maxSequenceMinusOne(4)
                .countSequenceZero(1)
                .maxSequenceZero(2)
                .countSequenceOne(1)
                .maxSequenceOne(1)
                .build()),
        Arguments.of(new int[] {-1, -1, 1, 1, 1, -1, -1, 0},
            ErosionPoint.builder()
                .countSequenceMinusOne(2)
                .maxSequenceMinusOne(2)
                .countSequenceZero(1)
                .maxSequenceZero(1)
                .countSequenceOne(1)
                .maxSequenceOne(3)
                .build()),
        Arguments.of(new int[] {-1, 0, 1, -1, 0, 1, -1, 0},
            ErosionPoint.builder()
                .countSequenceMinusOne(3)
                .maxSequenceMinusOne(1)
                .countSequenceZero(3)
                .maxSequenceZero(1)
                .countSequenceOne(2)
                .maxSequenceOne(1)
                .build()),
        Arguments.of(new int[] {-1, -1, -1, -1, 0, 0, 0, 0},
            ErosionPoint.builder()
                .countSequenceMinusOne(1)
                .maxSequenceMinusOne(4)
                .countSequenceZero(1)
                .maxSequenceZero(4)
                .build()),
        Arguments.of(new int[] {2, 1, 2, 1, 2, 2, 2, 2},
            ErosionPoint.builder()
                .countSequenceOne(2)
                .maxSequenceOne(1)
                .countSequenceTwo(2)
                .maxSequenceTwo(5)
                .build())
    );
  }

  private static Stream<Arguments> isEmptyData() {
    return Stream.of(
        Arguments.of(
            new int[] {0, -1, 0, -1, 0, -1, 0, -1},
            ErosionPoint.builder()
                .countSequenceMinusOne(4)
                .maxSequenceMinusOne(1)
                .countSequenceZero(4)
                .maxSequenceZero(1)
                .countSequenceOne(0)
                .maxSequenceOne(0)
                .build(),
            true
        ),
        Arguments.of(
            new int[] {0, 0, 0, 0, -1, -1, -1, -1},
            ErosionPoint.builder()
                .countSequenceMinusOne(1)
                .maxSequenceMinusOne(4)
                .countSequenceZero(1)
                .maxSequenceZero(4)
                .countSequenceOne(0)
                .maxSequenceOne(0)
                .build(),
            true
        ),
        Arguments.of(
            new int[] {-1, -1, -1, -1, 0, 1, 0, 0},
            ErosionPoint.builder()
                .countSequenceMinusOne(1)
                .maxSequenceMinusOne(4)
                .countSequenceZero(2)
                .maxSequenceZero(2)
                .countSequenceOne(1)
                .maxSequenceOne(1)
                .build(),
            true
        ),
        Arguments.of(
            new int[] {-1, -1, -1, -1, 0, 0, 1, 0},
            ErosionPoint.builder()
                .countSequenceMinusOne(1)
                .maxSequenceMinusOne(4)
                .countSequenceZero(2)
                .maxSequenceZero(2)
                .countSequenceOne(1)
                .maxSequenceOne(1)
                .build(),
            true
        ),
        Arguments.of(
            new int[] {-1, -1, 1, 1, 0, 1, 1, -1},
            ErosionPoint.builder()
                .countSequenceMinusOne(1)
                .maxSequenceMinusOne(3)
                .countSequenceZero(1)
                .maxSequenceZero(1)
                .countSequenceOne(2)
                .maxSequenceOne(2)
                .build(),
            true
        ),
        Arguments.of(
            new int[] {-1, -1, 1, 1, 1, 1, 1, -1},
            ErosionPoint.builder()
                .countSequenceMinusOne(1)
                .maxSequenceMinusOne(3)
                .countSequenceZero(0)
                .maxSequenceZero(0)
                .countSequenceOne(1)
                .maxSequenceOne(5)
                .build(),
            true
        ),
        Arguments.of(
            new int[] {-1, -1, -1, 0, 1, 1, 1, 0},
            ErosionPoint.builder()
                .countSequenceMinusOne(1)
                .maxSequenceMinusOne(3)
                .countSequenceZero(2)
                .maxSequenceZero(1)
                .countSequenceOne(1)
                .maxSequenceOne(3)
                .build(),
            true
        ),
        Arguments.of(
            new int[] {-1, -1, -1, 0, 0, 0, -1, -1},
            ErosionPoint.builder()
                .countSequenceMinusOne(1)
                .maxSequenceMinusOne(5)
                .countSequenceZero(1)
                .maxSequenceZero(3)
                .build(),
            true
        )
    );
  }
}
