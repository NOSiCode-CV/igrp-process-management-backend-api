package cv.igrp.platform.process.management.shared.domain.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CodeTest {

  @Test
  void create_ShouldReturnCode_WhenValidValue() {
    // Act
    Code code = Code.create("12345678");

    // Assert
    assertNotNull(code);
    assertEquals("12345678", code.getValue());
  }

  @Test
  void builder_ShouldReturnCode_WhenValidValue() {
    // Act
    Code code = Code.builder()
        .value("1234567")
        .build();

    // Assert
    assertNotNull(code);
    assertEquals("1234567", code.getValue());
  }

  @Test
  void constructor_ShouldThrowException_WhenNullValue() {
    // Act & Assert
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> Code.builder().value(null).build());

    assertEquals("The value of the code is required.", ex.getMessage());
  }

  @Test
  void constructor_ShouldThrowException_WhenEmptyValue() {
    // Act & Assert
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> Code.builder().value("").build());

    assertEquals("The value of the code is required.", ex.getMessage());
  }

  @Test
  void constructor_ShouldThrowException_WhenBlankValue() {
    // Act & Assert
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> Code.builder().value("   ").build());

    assertEquals("The value of the code is required.", ex.getMessage());
  }

  @Test
  void equalsAndHashCode_ShouldBeBasedOnValue() {
    // Arrange
    Code code1 = Code.create("123456");
    Code code2 = Code.create("123456");
    Code code3 = Code.create("45678");

    // Assert
    assertEquals(code1, code2);
    assertEquals(code1.hashCode(), code2.hashCode());
    assertNotEquals(code1, code3);

  }

}
