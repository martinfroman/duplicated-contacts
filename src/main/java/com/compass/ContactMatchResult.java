package com.compass;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ContactMatchResult {
    private final Long primaryContactId;
    private final Long potentialDuplicateId;
    private final String matchAccuracy;

    public ContactMatchResult(Long primaryContactId, Long potentialDuplicateId, String matchAccuracy) {
        this.primaryContactId = primaryContactId;
        this.potentialDuplicateId = potentialDuplicateId;
        this.matchAccuracy = matchAccuracy;
    }

    @Override
    public String toString() {
        return String.format("Primary Contact: %d, Potential Duplicate: %d, Accuracy: %s", primaryContactId, potentialDuplicateId, matchAccuracy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactMatchResult that = (ContactMatchResult) o;
        return Objects.equals(primaryContactId, that.primaryContactId) && Objects.equals(potentialDuplicateId, that.potentialDuplicateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(primaryContactId, potentialDuplicateId);
    }
}
