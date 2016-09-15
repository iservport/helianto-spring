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

package org.helianto.core.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * The <code>Context</code> domain class represents a mandatory
 * parent entity to any Helianto based installation. Every domain
 * object is traceable to one (or a chain of) <code>Context</code>.
 * 
 * @author Mauricio Fernandes de Castro
 */
@javax.persistence.Entity
@Table(name="core_context",
    uniqueConstraints = {@UniqueConstraint(columnNames={"contextName"})}
)
public class Context implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column(length=20)
    private String contextName;

    @Temporal(TemporalType.TIMESTAMP)
    private Date installDate;

    /**
     * Default constructor.
     */
    public Context() {
    	super();
    }

    /** 
     * Key constructor.
     * 
     * @param contextName
     */
    public Context(String contextName) {
    	this();
        setContextName(contextName);
    }

    /**
     * Primary key.
     */
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Context name.
     */
    public String getContextName() {
        return this.contextName;
    }
    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public Date getInstallDate() {
        if (this.installDate==null) {
            return new Date();
        }
        return this.installDate;
    }

    public void setInstallDate(Date installDate) {
        this.installDate = installDate;
    }

    public Context refreshInstallDate() {
        setInstallDate(new Date());
        return this;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Context)) return false;
        final Context other = (Context) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getId() != other.getId()) return false;
        final Object this$contextName = this.getContextName();
        final Object other$contextName = other.getContextName();
        if (this$contextName == null ? other$contextName != null : !this$contextName.equals(other$contextName))
            return false;
        final Object this$installDate = this.installDate;
        final Object other$installDate = other.installDate;
        if (this$installDate == null ? other$installDate != null : !this$installDate.equals(other$installDate))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getId();
        final Object $contextName = this.getContextName();
        result = result * PRIME + ($contextName == null ? 0 : $contextName.hashCode());
        final Object $installDate = this.installDate;
        result = result * PRIME + ($installDate == null ? 0 : $installDate.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Context;
    }

    public String toString() {
        return "org.helianto.core.domain.Context(id=" + this.getId() + ", contextName=" + this.getContextName() + ", installDate=" + this.installDate + ")";
    }
}
