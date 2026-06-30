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
  void testGetAllContactsReturnsServiceData() {
    List<Contact> result = controller.getAllContacts();

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("Test User 1", result.get(0).name());
    assertEquals("KENECT_LABS", result.get(0).source());
  }

  @Test
  void testGetAllContactsReturnsEmptyListWhenServiceReturnsEmpty() {
    mockService.setReturnEmpty(true);

    List<Contact> result = controller.getAllContacts();

    assertNotNull(result);
    assertEquals(0, result.size());
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
  }
}
