package com.kenect.contactsapi.contact;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactController {

  private final ContactService contactService;

  public ContactController(ContactService contactService) {
    this.contactService = contactService;
  }

  @GetMapping("/contacts")
  public List<Contact> getContacts(
      @RequestParam(name = "page", required = false) Optional<Integer> requestedPageNumber) {
    int pageNumber = requestedPageNumber.orElse(1);
    return contactService.getContactsForPage(pageNumber);
  }
}
