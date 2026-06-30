package com.kenect.contactsapi.contact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Service
public class ContactService {

  private static final String SOURCE = "KENECT_LABS";

  private final RestClient restClient;
  private final String contactsPath;

  public ContactService(
      @Value("${kenect.api.base-url}") String baseUrl,
      @Value("${kenect.api.contacts-path}") String contactsPath,
      @Value("${kenect.api.token}") String token) {
    this.contactsPath = contactsPath;
    this.restClient =
        RestClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .build();
  }

  public List<Contact> getAllContacts() {
    ResponseEntity<KenectContact[]> firstPageResponse = fetchPage(1);

    KenectContact[] firstPageContacts = firstPageResponse.getBody();
    if (firstPageContacts == null) {
      return List.of();
    }

    List<KenectContact> allContacts = new ArrayList<>(Arrays.asList(firstPageContacts));
    int totalPages = resolveTotalPages(firstPageResponse.getHeaders());
    for (int page = 2; page <= totalPages; page++) {
      ResponseEntity<KenectContact[]> pageResponse = fetchPage(page);

      KenectContact[] pageContacts = pageResponse.getBody();
      if (pageContacts != null) {
        allContacts.addAll(Arrays.asList(pageContacts));
      }
    }

    return allContacts.stream()
        .map(
            contact ->
                new Contact(
                    contact.id(),
                    contact.name(),
                    contact.email(),
                    SOURCE,
                    contact.createdAt(),
                    contact.updatedAt()))
        .toList();
  }

  private ResponseEntity<KenectContact[]> fetchPage(int page) {
    try {
      return restClient
          .get()
          .uri(uriBuilder -> uriBuilder.path(contactsPath).queryParam("page", page).build())
          .retrieve()
          .toEntity(KenectContact[].class);
    } catch (RestClientResponseException exception) {
      throw new ContactUpstreamException("Kenect API returned an error response", exception);
    } catch (ResourceAccessException exception) {
      throw new ContactUpstreamException("Kenect API is unreachable", exception);
    }
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
      Long id, String name, String email, String createdAt, String updatedAt) {}
}
