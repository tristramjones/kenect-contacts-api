package com.kenect.contactsapi.contact;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@Service
public class ContactService {

	private static final String SOURCE = "KENECT_LABS";

	private final RestClient restClient;
	private final String contactsPath;

	public ContactService(
			@Value("${kenect.api.base-url}") String baseUrl,
			@Value("${kenect.api.contacts-path}") String contactsPath,
			@Value("${kenect.api.token}") String token
	) {
		this.contactsPath = contactsPath;
		this.restClient = RestClient.builder()
				.baseUrl(baseUrl)
				.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.build();
	}

	public List<Contact> getAllContacts() {
		KenectContact[] contacts = restClient.get()
				.uri(uriBuilder -> uriBuilder.path(contactsPath).queryParam("page", 1).queryParam("pageSize", 20).build())
				.retrieve()
				.body(KenectContact[].class);

		if (contacts == null) {
			return List.of();
		}

		return Arrays.stream(contacts)
				.map(contact -> new Contact(
						contact.id(),
						contact.name(),
						contact.email(),
						SOURCE,
						contact.createdAt(),
						contact.updatedAt()
				))
				.toList();
	}

	private record KenectContact(
			Long id,
			String name,
			String email,
			String createdAt,
			String updatedAt
	) {
	}
}
