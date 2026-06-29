package com.kenect.contactsapi.contact;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

	public List<Contact> getAllContacts() {
		return List.of(
				new Contact(1L, "Mrs. Willian Bradtke", "jerold@example.net", "KENECT_LABS", "2020-06-24T19:37:16.688Z", "2020-06-24T19:37:16.119Z"),
				new Contact(2L, "John Doe", "johndoe@example.net", "KENECT_LABS", "2021-02-10T11:10:09.987Z", "2022-05-05T15:27:17.547Z")
		);
	}
}
