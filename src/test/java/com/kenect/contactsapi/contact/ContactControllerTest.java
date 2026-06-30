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
  void testGetContactsWithoutPageDefaultsToFirstPage() {
    List<Contact> result = controller.getContacts(Optional.empty());

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("Page 1 User 1", result.get(0).name());
    assertEquals("KENECT_LABS", result.get(0).source());
    assertEquals(1, mockService.getLastRequestedPageNumber());
  }

  @Test
  void testGetContactsWithoutPageReturnsEmptyListWhenServiceReturnsEmpty() {
    mockService.setReturnEmpty(true);

    List<Contact> result = controller.getContacts(Optional.empty());

    assertNotNull(result);
    assertEquals(0, result.size());
    assertEquals(1, mockService.getLastRequestedPageNumber());
  }

  @Test
  void testGetContactsWithPageReturnsSpecificPage() {
    List<Contact> result = controller.getContacts(Optional.of(2));

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("Page 2 User 1", result.get(0).name());
    assertEquals("KENECT_LABS", result.get(0).source());
    assertEquals(2, mockService.getLastRequestedPageNumber());
  }

  private static class MockContactService extends ContactService {
    private boolean returnEmpty = false;
    private Integer lastRequestedPageNumber;

    private MockContactService() {
      super("http://localhost", "/test", "token");
    }

    public void setReturnEmpty(boolean returnEmpty) {
      this.returnEmpty = returnEmpty;
    }

    public Integer getLastRequestedPageNumber() {
      return lastRequestedPageNumber;
    }

    @Override
    public List<Contact> getContactsForPage(int pageNumber) {
      lastRequestedPageNumber = pageNumber;
      if (returnEmpty) {
        return List.of();
      }

      return List.of(
          new Contact(
              (long) ((pageNumber - 1) * 2 + 1),
              "Page " + pageNumber + " User 1",
              "page" + pageNumber + "user1@example.com",
              "KENECT_LABS",
              "2020-01-03T00:00:00.000Z",
              "2020-01-03T00:00:00.000Z"),
          new Contact(
              (long) ((pageNumber - 1) * 2 + 2),
              "Page " + pageNumber + " User 2",
              "page" + pageNumber + "user2@example.com",
              "KENECT_LABS",
              "2020-01-04T00:00:00.000Z",
              "2020-01-04T00:00:00.000Z"));
    }
  }
}
