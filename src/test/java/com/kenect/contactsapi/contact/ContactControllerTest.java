package com.kenect.contactsapi.contact;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContactControllerTest {

  private ContactController controller;
  private MockContactService mockService;

  @BeforeEach
  void setup() {
    mockService = new MockContactService();
    controller = new ContactController(mockService);
  }

  @Test
  void testGetContactsWithoutPageReturnsAllContacts() {
    List<Contact> result = controller.getContacts(Optional.empty());

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("Test User 1", result.get(0).name());
    assertEquals("KENECT_LABS", result.get(0).source());
  }

  @Test
  void testGetContactsWithoutPageReturnsEmptyListWhenServiceReturnsEmpty() {
    mockService.setReturnEmpty(true);

    List<Contact> result = controller.getContacts(Optional.empty());

    assertNotNull(result);
    assertEquals(0, result.size());
  }

  @Test
  void testGetContactsWithPageReturnsSpecificPage() {
    List<Contact> result = controller.getContacts(Optional.of(2));

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("Page 2 User 1", result.get(0).name());
    assertEquals("KENECT_LABS", result.get(0).source());
  }

  private static class MockContactService extends ContactService {
    private boolean returnEmpty = false;

    private MockContactService() {
      super("http://localhost", "/test", "token");
    }

    public void setReturnEmpty(boolean returnEmpty) {
      this.returnEmpty = returnEmpty;
    }

    @Override
    public List<Contact> getAllContacts() {
      if (returnEmpty) {
        return List.of();
      }
      return List.of(
          new Contact(
              1L,
              "Test User 1",
              "test1@example.com",
              "KENECT_LABS",
              "2020-01-01T00:00:00.000Z",
              "2020-01-01T00:00:00.000Z"),
          new Contact(
              2L,
              "Test User 2",
              "test2@example.com",
              "KENECT_LABS",
              "2020-01-02T00:00:00.000Z",
              "2020-01-02T00:00:00.000Z"));
    }

    @Override
    public List<Contact> getContactsForPage(int pageNumber) {
      return List.of(
          new Contact(
              3L,
              "Page 2 User 1",
              "page2user1@example.com",
              "KENECT_LABS",
              "2020-01-03T00:00:00.000Z",
              "2020-01-03T00:00:00.000Z"),
          new Contact(
              4L,
              "Page 2 User 2",
              "page2user2@example.com",
              "KENECT_LABS",
              "2020-01-04T00:00:00.000Z",
              "2020-01-04T00:00:00.000Z"));
    }
  }
}
