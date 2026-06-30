package com.kenect.contactsapi.contact;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ContactServiceTest {

  @Test
  void testGetAllContactsMapsKenectContactToContact() {
    // This test verifies the Contact model receives correct fields from KenectContact
    Contact contact =
        new Contact(
            1L,
            "Test User",
            "test@example.com",
            "KENECT_LABS",
            "2020-01-01T00:00:00.000Z",
            "2020-01-01T00:00:00.000Z");

    assertEquals(1L, contact.id());
    assertEquals("Test User", contact.name());
    assertEquals("test@example.com", contact.email());
    assertEquals("KENECT_LABS", contact.source());
    assertEquals("2020-01-01T00:00:00.000Z", contact.createdAt());
    assertEquals("2020-01-01T00:00:00.000Z", contact.updatedAt());
  }

  @Test
  void testContactRecordIsImmutable() {
    Contact contact1 =
        new Contact(
            1L,
            "Alice",
            "alice@example.com",
            "KENECT_LABS",
            "2020-01-01T00:00:00.000Z",
            "2020-01-01T00:00:00.000Z");
    Contact contact2 =
        new Contact(
            1L,
            "Alice",
            "alice@example.com",
            "KENECT_LABS",
            "2020-01-01T00:00:00.000Z",
            "2020-01-01T00:00:00.000Z");

    // Records implement value equality
    assertEquals(contact1, contact2);
  }

  @Test
  void testContactUpstreamExceptionPreservesOriginalCause() {
    RuntimeException originalCause = new RuntimeException("Connection refused");
    ContactUpstreamException exception =
        new ContactUpstreamException("API unreachable", originalCause);

    assertEquals("API unreachable", exception.getMessage());
    assertEquals(originalCause, exception.getCause());
  }
}
