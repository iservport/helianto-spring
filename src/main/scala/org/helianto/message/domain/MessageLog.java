/* Copyright 2005 I Serv Consultoria Empresarial Ltda.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.helianto.message.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Log of messages.
 *
 * @author Mauricio Fernandes de Castro
 */
@javax.persistence.Entity
@Table(name="msg_log")
public class MessageLog implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id @Column(length=32)
    private String id;

    @Column(length=32)
    private String userId;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastEvent;
    
    @Column(length=20)
    private String service;

    @Lob
    private String body = "";

    private Integer statusCode;

    /**
     * Default constructor.
     */
    public MessageLog() {
    	super();
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
        this.lastEvent = new Date();
    }

    /**
     * Service constructor.
     *
     * @param id
     * @param service
     * @param body
     * @param statusCode
     */
    public MessageLog(String id, String service, String body, int statusCode) {
        super();
        this.id = id;
        this.service = service;
        this.body = body;
        this.statusCode = statusCode;
    }

    /**
     * Message constructor.
     *
     * @param message
     * @param body
     * @param statusCode
     */
    public MessageLog(Message message, String body, int statusCode) {
        this(message.getId(), message.getServicePath(), body, statusCode);
    }

    public String getId() {
        return this.id;
    }

    public String getUserId() {
        return this.userId;
    }

    public Date getLastEvent() {
        return this.lastEvent;
    }

    public String getService() {
        return this.service;
    }

    public String getBody() {
        return this.body;
    }

    public Integer getStatusCode() {
        return this.statusCode;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setLastEvent(Date lastEvent) {
        this.lastEvent = lastEvent;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof MessageLog)) return false;
        final MessageLog other = (MessageLog) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$userId = this.getUserId();
        final Object other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !this$userId.equals(other$userId)) return false;
        final Object this$lastEvent = this.getLastEvent();
        final Object other$lastEvent = other.getLastEvent();
        if (this$lastEvent == null ? other$lastEvent != null : !this$lastEvent.equals(other$lastEvent)) return false;
        final Object this$service = this.getService();
        final Object other$service = other.getService();
        if (this$service == null ? other$service != null : !this$service.equals(other$service)) return false;
        final Object this$body = this.getBody();
        final Object other$body = other.getBody();
        if (this$body == null ? other$body != null : !this$body.equals(other$body)) return false;
        final Object this$statusCode = this.getStatusCode();
        final Object other$statusCode = other.getStatusCode();
        if (this$statusCode == null ? other$statusCode != null : !this$statusCode.equals(other$statusCode))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $userId = this.getUserId();
        result = result * PRIME + ($userId == null ? 43 : $userId.hashCode());
        final Object $lastEvent = this.getLastEvent();
        result = result * PRIME + ($lastEvent == null ? 43 : $lastEvent.hashCode());
        final Object $service = this.getService();
        result = result * PRIME + ($service == null ? 43 : $service.hashCode());
        final Object $body = this.getBody();
        result = result * PRIME + ($body == null ? 43 : $body.hashCode());
        final Object $statusCode = this.getStatusCode();
        result = result * PRIME + ($statusCode == null ? 43 : $statusCode.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof MessageLog;
    }

    public String toString() {
        return "org.helianto.message.domain.MessageLog(id=" + this.getId() + ", userId=" + this.getUserId() + ", lastEvent=" + this.getLastEvent() + ", service=" + this.getService() + ", body=" + this.getBody() + ", statusCode=" + this.getStatusCode() + ")";
    }
}
