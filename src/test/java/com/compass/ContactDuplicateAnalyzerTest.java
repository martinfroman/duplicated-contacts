package com.compass;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ContactDuplicateAnalyzerTest {

    @Test
    void testHighAccuracyMatch() {
        List<Contact> contacts = Arrays.asList( //
                new Contact(1001L, "C", "F", "mollis.lectus.pede@outlook.net", "449-6990 Tellus. Rd.", "39746"), //
                new Contact(1002L, "C", "French", "mollis.lectus.pede@outlook.net", "449-6990 Tellus. Rd.", "39746") //
        );
        List<ContactMatchResult> results = ContactDuplicateAnalyzer.identifyPotentialDuplicates(contacts);
        assertEquals(1, results.size());
        assertEquals("High", results.get(0).getMatchAccuracy());
    }

    @Test
    void testLowAccuracyMatch() {
        List<Contact> contacts = Arrays.asList( //
                new Contact(1001L, "C", "F", "mollis.lectus.pede@outlook.net", "449-6990 Tellus. Rd.", "39746"), //
                new Contact(1003L, "Ciara", "F", "non.lacinia.at@zoho.ca", null, "39746") //
        );
        List<ContactMatchResult> results = ContactDuplicateAnalyzer.identifyPotentialDuplicates(contacts);
        assertEquals(1, results.size());
        assertEquals("Low", results.get(0).getMatchAccuracy());
    }

    @Test
    void testNoMatch() {
        List<Contact> contacts = Arrays.asList( //
                new Contact(1001L, "C", "F", "email1@test.com", "123 Road", "12345"), //
                new Contact(1002L, "John", "Doe", "email2@test.com", "456 Road", "67890") //
        );
        List<ContactMatchResult> results = ContactDuplicateAnalyzer.identifyPotentialDuplicates(contacts);
        assertEquals(0, results.size());
    }
}

