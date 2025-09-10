package cv.igrp.platform.process.management.shared.domain.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class NameTest {

  @Test
  void shouldCreateNameSuccessfully() {
    Name name = Name.create(" John Doe ");
    assertEquals("John Doe", name.getValue());
    assertEquals("JOHN DOE", name.getNormalizedName());
  }

  @Test
  void shouldNormalizeNameRemovingAccents() {
    Name name = Name.create("Álvaro José");
    assertEquals("Álvaro José", name.getValue());
    assertEquals("ALVARO JOSE", name.getNormalizedName());
  }

  @Test
  void shouldThrowExceptionWhenNameIsNull() {
    var exception = assertThrows(IllegalArgumentException.class, () -> Name.create(null));
    assertEquals("The value of the name is required.", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionWhenNameIsEmpty() {
    var exception = assertThrows(IllegalArgumentException.class, () -> Name.create("   "));
    assertEquals("The value of the name is required.", exception.getMessage());
  }

  @Test
  void shouldBeEqualWhenNamesHaveSameNormalizedValue() {
    Name name1 = Name.create("José");
    Name name2 = Name.create("JOSÉ");
    assertEquals(name1, name2);
    assertEquals(name1.hashCode(), name2.hashCode());
  }

}

