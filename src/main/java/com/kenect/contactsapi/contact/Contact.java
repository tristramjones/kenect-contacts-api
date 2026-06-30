package com.kenect.contactsapi.contact;

public record Contact(
    Long id, String name, String email, String source, String createdAt, String updatedAt) {}
