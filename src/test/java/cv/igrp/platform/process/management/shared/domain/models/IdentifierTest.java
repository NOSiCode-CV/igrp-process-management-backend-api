package cv.igrp.platform.process.management.shared.domain.models;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class IdentifierTest {

  @Test
  void shouldCreateIdentifierWithProvidedUuid() {
    UUID uuid = UUID.randomUUID();

    Identifier identifier = Identifier.create(uuid);

    assertNotNull(identifier);
    assertEquals(uuid, identifier.getValue());
  }

  @Test
  void shouldCreateIdentifierFromString() {
    UUID uuid = UUID.randomUUID();

    Identifier identifier = Identifier.create(uuid.toString());

    assertNotNull(identifier);
    assertEquals(uuid, identifier.getValue());
  }

  @Test
  void shouldGenerateNewIdentifierWhenValueIsNull() {
    Identifier identifier = Identifier.create((UUID) null);

    assertNotNull(identifier);
    assertNotNull(identifier.getValue());
  }

  @Test
  void shouldGenerateIdentifierUsingGenerateMethod() {
    Identifier identifier = Identifier.generate();

    assertNotNull(identifier);
    assertNotNull(identifier.getValue());
  }

  @Test
  void shouldGenerateUniqueIdentifiers() {
    Identifier id1 = Identifier.generate();
    Identifier id2 = Identifier.generate();

    assertNotEquals(id1, id2);
    assertNotEquals(id1.getValue(), id2.getValue());
  }

  @Test
  void shouldThrowExceptionForInvalidStringUuid() {
    assertThrows(IllegalArgumentException.class, () -> Identifier.create("34567"));
  }

  @Test
  void shouldRespectEqualsAndHashCode() {
    UUID uuid = UUID.randomUUID();

    Identifier id1 = Identifier.create(uuid);
    Identifier id2 = Identifier.create(uuid);

    assertEquals(id1, id2);
    assertEquals(id1.hashCode(), id2.hashCode());
  }

}
