package com.compass;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContactDuplicateAnalyzer {
    private static final Logger logger = Logger.getLogger(ContactDuplicateAnalyzer.class.getName());

    public static String evaluateContactSimilarity(Contact entry1, Contact entry2) {
        logger.log(Level.INFO, "Evaluating similarity between Contact {0} and Contact {1}", new Object[]{entry1.getId(), entry2.getId()});

        var similarityScore = 0;

        // Matching first name
        if (entry1.getFirstName().equalsIgnoreCase(entry2.getFirstName())) {
            similarityScore += 2;
            logger.log(Level.FINE, "First names match for Contact {0} and Contact {1}", new Object[]{entry1.getId(), entry2.getId()});
        } else if (entry1.getFirstName().charAt(0) == entry2.getFirstName().charAt(0)) {
            similarityScore += 1;
            logger.log(Level.FINE, "First name initials match for Contact {0} and Contact {1}", new Object[]{entry1.getId(), entry2.getId()});
        }

        // Matching last name
        if (entry1.getLastName().equalsIgnoreCase(entry2.getLastName())) {
            similarityScore += 2;
            logger.log(Level.FINE, "Last names match for Contact {0} and Contact {1}", new Object[]{entry1.getId(), entry2.getId()});
        }

        // Matching email
        if (entry1.getEmail().equalsIgnoreCase(entry2.getEmail())) {
            similarityScore += 5;
            logger.log(Level.FINE, "Emails match for Contact {0} and Contact {1}", new Object[]{entry1.getId(), entry2.getId()});
        }

        // Matching address
        if (entry1.getAddress() != null && entry1.getAddress().equalsIgnoreCase(entry2.getAddress())) {
            similarityScore += 3;
            logger.log(Level.FINE, "Addresses match for Contact {0} and Contact {1}", new Object[]{entry1.getId(), entry2.getId()});
        }

        // Matching zipcode
        if (entry1.getZipCode() != null && entry2.getZipCode() != null && entry1.getZipCode().equals(entry2.getZipCode())) {
            similarityScore += 1;
            logger.log(Level.FINE, "Postal codes match for Contact {0} and Contact {1}", new Object[]{entry1.getId(), entry2.getId()});
        }

        // Log the calculated score
        logger.log(Level.INFO, "Total similarity score for Contact {0} and Contact {1} is: {2}", new Object[]{entry1.getId(), entry2.getId(), similarityScore});

        // Determining similarity level based on score
        var similarityLevel = "None";
        if (similarityScore >= 8) {
            similarityLevel = "High";
        } else if (similarityScore >= 5) {
            similarityLevel = "Medium";
        } else if (similarityScore >= 2) {
            similarityLevel = "Low";
        }

        logger.log(Level.INFO, "Similarity between Contact {0} and Contact {1} is: {2}", new Object[]{entry1.getId(), entry2.getId(), similarityLevel});
        return similarityLevel;
    }

    public static List<ContactMatchResult> identifyPotentialDuplicates(List<Contact> contactList) {
        List<ContactMatchResult> matchResults = new ArrayList<>();
        logger.log(Level.INFO, "Starting duplicate search in a contact list of {0} entries.", contactList.size());

        for (int i = 0; i < contactList.size(); i++) {
            for (int j = i + 1; j < contactList.size(); j++) {
                var entry1 = contactList.get(i);
                var entry2 = contactList.get(j);
                var similarityLevel = evaluateContactSimilarity(entry1, entry2);

                if (!similarityLevel.equals("None")) {
                    matchResults.add(new ContactMatchResult(entry1.getId(), entry2.getId(), similarityLevel));
                    logger.log(Level.INFO, "Potential duplicate found: Contact {0} and Contact {1} with similarity: {2}", //
                            new Object[]{entry1.getId(), entry2.getId(), similarityLevel});
                }
            }
        }

        logger.log(Level.INFO, "Duplicate search completed. Found {0} potential matches.", matchResults.size());
        return matchResults;
    }

    public static List<Contact> loadContactsFromCsv(String csvFilePath) throws IOException {
        List<Contact> contactList = new ArrayList<>();
        logger.log(Level.INFO, "Loading contacts from CSV file: {0}", csvFilePath);

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            // Skip the header line
            var headerLine = reader.readLine();
            if (headerLine == null) {
                logger.warning("CSV file is empty or missing a header.");
                return contactList;
            }

            // Reading each contact entry line-by-line
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 6) {
                    var contactId = Long.parseLong(fields[0].trim());
                    var firstName = fields[1].trim();
                    var lastName = fields[2].trim();
                    var email = fields[3].trim();
                    var postalCode = fields[4].trim();
                    var address = fields[5].trim();

                    contactList.add(new Contact(contactId, firstName, lastName, email, address, postalCode));
                }
            }
        }

        logger.log(Level.INFO, "Finished loading contacts. Total contacts: {0}", contactList.size());
        return contactList;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            logger.log(Level.SEVERE, "Usage: java ContactDuplicateAnalyzer <csv-file-path>");
            return;
        }

        String csvFilePath = args[0];
        try {
            // Set logger level to INFO
            Logger.getLogger("").getHandlers()[0].setLevel(Level.INFO);
            logger.setLevel(Level.INFO);

            // Load contacts from CSV file
            List<Contact> contacts = loadContactsFromCsv(csvFilePath);

            // Identify potential duplicates
            List<ContactMatchResult> duplicateMatches = identifyPotentialDuplicates(contacts);

            // Output the results
            duplicateMatches.forEach(match -> logger.log(Level.INFO, match.toString()));


        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading CSV file: {0}", e.getMessage());
        }
    }
}
