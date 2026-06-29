package com.kenect.contactsapi.contact;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
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
		ResponseEntity<KenectContact[]> firstPageResponse = restClient.get()
				.uri(uriBuilder -> uriBuilder.path(contactsPath).queryParam("page", 1).build())
				.retrieve()
				.toEntity(KenectContact[].class);

		KenectContact[] firstPageContacts = firstPageResponse.getBody();
		if (firstPageContacts == null) {
			return List.of();
		}

		List<KenectContact> allContacts = new ArrayList<>(Arrays.asList(firstPageContacts));
		int totalPages = resolveTotalPages(firstPageResponse.getHeaders());
		for (int page = 2; page <= totalPages; page++) {
			ResponseEntity<KenectContact[]> pageResponse = restClient.get()
					.uri(uriBuilder -> uriBuilder.path(contactsPath).queryParam("page", page).build())
					.retrieve()
					.toEntity(KenectContact[].class);

			KenectContact[] pageContacts = pageResponse.getBody();
			if (pageContacts != null) {
				allContacts.addAll(Arrays.asList(pageContacts));
			}
		}

		return allContacts.stream()
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

	private int resolveTotalPages(HttpHeaders headers) {
		String totalPagesHeader = headers.getFirst("Total-Pages");
		if (totalPagesHeader == null || totalPagesHeader.isBlank()) {
			return 1;
		}

		try {
			return Integer.parseInt(totalPagesHeader);
		} catch (NumberFormatException exception) {
			return 1;
		}
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
