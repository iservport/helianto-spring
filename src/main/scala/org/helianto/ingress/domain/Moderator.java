package org.helianto.ingress.domain;

import javax.persistence.*;

@Entity
@Table(name="core_moderator",
        uniqueConstraints = {@UniqueConstraint(columnNames={"userId"})}
)
public class Moderator {

    @Id @Column(length=32)
    private String id;

    @Version
    private int version;

    @Column(length=32)
    private String userId;

    public Moderator() {
        super();
    }

    public Moderator(String userId) {
        this();
        this.userId = userId;
    }

    public String getId() {
        return this.id;
    }

    public int getVersion() {
        return this.version;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Moderator)) return false;
        final Moderator other = (Moderator) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        if (this.getVersion() != other.getVersion()) return false;
        final Object this$userId = this.getUserId();
        final Object other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !this$userId.equals(other$userId)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        result = result * PRIME + this.getVersion();
        final Object $userId = this.getUserId();
        result = result * PRIME + ($userId == null ? 43 : $userId.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Moderator;
    }

    public String toString() {
        return "org.helianto.ingress.domain.Moderator(id=" + this.getId() + ", version=" + this.getVersion() + ", userId=" + this.getUserId() + ")";
    }
}
