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
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

/**
 * State of a union or federation.
 * 
 * @author Mauricio Fernandes de Castro
 */
@javax.persistence.Entity
@Table(name="core_state",
    uniqueConstraints = {@UniqueConstraint(columnNames={"contextName", "stateCode"})}
)
public class State  
	implements Serializable, Comparable<State> {

    private static final long serialVersionUID = 1L;
    
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

	@Column(length=20)
	private String contextName = "";

	@Column(length=12)
    private String stateCode = "";

	@Column(length=64)
	private String stateName = "";

	@Column(length=3)
	private String stateAlias = "";

	@Temporal(TemporalType.TIMESTAMP)
	private Date installDate;

	/**
     * ISO 3166-1 alpha 3 code
     */
	@Column(length=3)
    private String countryCode = "BRA";
    
    private char priority = 0;

	/**
	 * Empty constructor.
	 */
    public State() {
        super();
    }

    /**
     * Key constructor.
     * 
     * @param contextName
     * @param stateCode
     */
    public State(String contextName, String stateCode) {
        this(contextName, "BRA", stateCode);
    }

    /**
     * Country constructor.
     *
	 * @param contextName
     * @param countryCode
     * @param stateCode
     */
    public State(String contextName, String countryCode, String stateCode) {
        this(contextName, countryCode, stateCode, "", "");
    }

    /**
     * Name constructor.
     * 
	 * @param contextName
	 * @param countryCode
	 * @param stateCode
	 * @param stateName
     * @param stateAlias
     */
    public State(String contextName, String countryCode, String stateCode, String stateName, String stateAlias) {
    	this();
        setContextName(contextName);
        setStateCode(stateCode);
        setCountryCode(countryCode);
    	setStateName(stateName);
		setStateAlias(stateAlias);
    }

    /**
     * Merger.
     *
     * @param command
     */
    public State merge(State command) {
        setContextName(command.getContextName());
        setStateCode(command.getStateCode());
        setCountryCode(command.getCountryCode());
        setStateName(command.getStateName());
        setPriority(command.getPriority());
		setStateAlias(command.getStateAlias());
        return this;
    }

    public State validateAll() {
		if (Optional.ofNullable(this.contextName).orElse("").isEmpty()
                || Optional.ofNullable(this.stateCode).orElse("").isEmpty()
                || Optional.ofNullable(this.countryCode).orElse("").isEmpty()
                || Optional.ofNullable(this.stateName).orElse("").isEmpty()) {
            throw new IllegalArgumentException();
		}
		return this;
	}

	public int compareTo(State next) {
		if (getPriority()==next.getPriority()) {
			if (getStateCode()!=null && next.getStateCode()!=null) {
				return getStateCode().compareTo(next.getStateCode());
			}
			return 0;
		}
		return getPriority()-next.getPriority();
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

	public State refreshInstallDate() {
		setInstallDate(new Date());
		return this;
	}

	public int getId() {
		return this.id;
	}

	public String getContextName() {
		return this.contextName;
	}

	public String getStateCode() {
		return this.stateCode;
	}

	public String getStateName() {
		return this.stateName;
	}

	public String getStateAlias() {
		return this.stateAlias;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public char getPriority() {
		return this.priority;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public void setStateAlias(String stateAlias) {
		this.stateAlias = stateAlias;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public void setPriority(char priority) {
		this.priority = priority;
	}

	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof State)) return false;
		final State other = (State) o;
		if (!other.canEqual((Object) this)) return false;
		if (this.id != other.id) return false;
		final Object this$contextName = this.contextName;
		final Object other$contextName = other.contextName;
		if (this$contextName == null ? other$contextName != null : !this$contextName.equals(other$contextName))
			return false;
		final Object this$stateCode = this.stateCode;
		final Object other$stateCode = other.stateCode;
		if (this$stateCode == null ? other$stateCode != null : !this$stateCode.equals(other$stateCode)) return false;
		final Object this$stateName = this.stateName;
		final Object other$stateName = other.stateName;
		if (this$stateName == null ? other$stateName != null : !this$stateName.equals(other$stateName)) return false;
		final Object this$stateAlias = this.stateAlias;
		final Object other$stateAlias = other.stateAlias;
		if (this$stateAlias == null ? other$stateAlias != null : !this$stateAlias.equals(other$stateAlias))
			return false;
        // install date is not part of equals
		final Object this$countryCode = this.countryCode;
		final Object other$countryCode = other.countryCode;
		if (this$countryCode == null ? other$countryCode != null : !this$countryCode.equals(other$countryCode))
			return false;
		if (this.priority != other.priority) return false;
		return true;
	}

	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		result = result * PRIME + this.id;
		final Object $contextName = this.contextName;
		result = result * PRIME + ($contextName == null ? 0 : $contextName.hashCode());
		final Object $stateCode = this.stateCode;
		result = result * PRIME + ($stateCode == null ? 0 : $stateCode.hashCode());
		final Object $stateName = this.stateName;
		result = result * PRIME + ($stateName == null ? 0 : $stateName.hashCode());
		final Object $stateAlias = this.stateAlias;
		result = result * PRIME + ($stateAlias == null ? 0 : $stateAlias.hashCode());
		final Object $countryCode = this.countryCode;
		result = result * PRIME + ($countryCode == null ? 0 : $countryCode.hashCode());
		result = result * PRIME + this.priority;
		return result;
	}

	protected boolean canEqual(Object other) {
		return other instanceof State;
	}

	public String toString() {
		return "org.helianto.core.domain.State(id=" + this.id + ", contextName=" + this.contextName + ", stateCode=" + this.stateCode + ", stateName=" + this.stateName + ", stateAlias=" + this.stateAlias + ", installDate=" + this.getInstallDate() + ", countryCode=" + this.countryCode + ", priority=" + this.priority + ")";
	}
}


