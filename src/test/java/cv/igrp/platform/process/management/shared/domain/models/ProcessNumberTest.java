package cv.igrp.platform.process.management.shared.domain.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProcessNumberTest {
  @Test
  void create_ShouldReturnCode_WhenValidValue() {
    // Act
    ProcessNumber number = ProcessNumber.create("12345678");

    // Assert
    assertNotNull(number);
    assertEquals("12345678", number.getValue());
  }

  @Test
  void builder_ShouldReturnCode_WhenValidValue() {
    // Act
    ProcessNumber number = ProcessNumber.builder()
        .value("1234567")
        .build();

    // Assert
    assertNotNull(number);
    assertEquals("1234567", number.getValue());
  }

  @Test
  void constructor_ShouldThrowException_WhenNullValue() {
    // Act & Assert
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> ProcessNumber.builder().value(null).build());

    assertEquals("The value of the processNumber is required.", ex.getMessage());
  }

  @Test
  void constructor_ShouldThrowException_WhenEmptyValue() {
    // Act & Assert
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> ProcessNumber.builder().value("").build());

    assertEquals("The value of the processNumber is required.", ex.getMessage());
  }

  @Test
  void constructor_ShouldThrowException_WhenBlankValue() {
    // Act & Assert
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> ProcessNumber.builder().value("   ").build());

    assertEquals("The value of the processNumber is required.", ex.getMessage());
  }

  @Test
  void equalsAndHashCode_ShouldBeBasedOnValue() {
    // Arrange
    ProcessNumber number1 = ProcessNumber.create("123456");
    ProcessNumber number2 = ProcessNumber.create("123456");
    ProcessNumber number3 = ProcessNumber.create("45678");

    // Assert
    assertEquals(number1, number2);
    assertEquals(number1.hashCode(), number2.hashCode());
    assertNotEquals(number1, number3);

  }

}
