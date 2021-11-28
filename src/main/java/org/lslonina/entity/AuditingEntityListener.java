package org.lslonina.entity;

import java.time.Instant;

import javax.persistence.PrePersist;

public class AuditingEntityListener {

    @PrePersist
    void preCreate(AbstractEntity auditable) {
        Instant now = Instant.now();
        auditable.setCreationDate(now);
        auditable.setModificationDate(now);
    }

    void preUpdate(AbstractEntity auditable) {
        Instant now = Instant.now();
        auditable.setModificationDate(now);
    }
}
