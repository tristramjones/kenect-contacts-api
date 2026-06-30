package com.kenect.contactsapi.contact;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
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
  void testGetContactsReturnsAllContacts() {
    List<Contact> result = controller.getContacts();

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("Test User 1", result.get(0).name());
    assertEquals("KENECT_LABS", result.get(0).source());
    assertTrue(mockService.wasGetAllContactsCalled());
  }

  @Test
  void testGetContactsReturnsEmptyListWhenServiceReturnsEmpty() {
    mockService.setReturnEmpty(true);

    List<Contact> result = controller.getContacts();

    assertNotNull(result);
    assertEquals(0, result.size());
    assertTrue(mockService.wasGetAllContactsCalled());
  }

  private static class MockContactService extends ContactService {
    private boolean returnEmpty = false;
    private boolean getAllContactsCalled = false;

    private MockContactService() {
      super("http://localhost", "/test", "token");
    }

    public void setReturnEmpty(boolean returnEmpty) {
      this.returnEmpty = returnEmpty;
    }

    public boolean wasGetAllContactsCalled() {
      return getAllContactsCalled;
    }

    @Override
    public List<Contact> getAllContacts() {
      getAllContactsCalled = true;
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
  }
}
