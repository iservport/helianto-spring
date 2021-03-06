package org.helianto.ingress.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@MappedSuperclass
public class AbstractRegistration {

    @Column(length=64)
    private String remoteAddress;

    @Temporal(TemporalType.TIMESTAMP)
    private Date issueDate;

    private Date lastConfirmed;

    public String getRemoteAddress() {
        return remoteAddress;
    }
    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public Date getLastConfirmed() {
        return lastConfirmed;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public void setLastConfirmed(Date lastConfirmed) {
        this.lastConfirmed = lastConfirmed;
    }

    public boolean isConfirmed() {
        return getLastConfirmed()!=null;
    }

    public Instant getValidTo(int days) {
        return Optional.ofNullable(getIssueDate()).orElse(new Date()).toInstant().minus(days, ChronoUnit.DAYS);
    }

    public boolean isValidTo(int days) {
        return days >0 && Instant.now().isAfter(getValidTo(days));
    }

    public void merge(AbstractRegistration command) {
        setIssueDate(command.getIssueDate());
        setLastConfirmed(command.getLastConfirmed());
    }

    protected AbstractRegistration merge(String remoteAddress) {
        setRemoteAddress(remoteAddress);
        return this;
    }

}
