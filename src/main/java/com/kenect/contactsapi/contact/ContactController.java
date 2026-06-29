package com.kenect.contactsapi.contact;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ContactController {

	private final ContactService contactService;

	public ContactController(ContactService contactService) {
		this.contactService = contactService;
	}

	@GetMapping("/contacts")
	public List<Contact> getAllContacts() {
		return contactService.getAllContacts();
	}
}
