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

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;


/**
 * City.
 * 
 * @author Mauricio Fernandes de Castro
 */
@javax.persistence.Entity
@Table(name="core_city",
    uniqueConstraints = {@UniqueConstraint(columnNames={"contextName", "cityCode"})
    	,@UniqueConstraint(columnNames={"stateId", "cityCode"})}
)
public class City
	implements Serializable, Comparable<City> {

    private static final long serialVersionUID = 1L;
    
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    
    @Version
    private int version;
    
	@Column(length=20)
	private String contextName = "";

	@Column(length=12)
	private String cityCode = "";

	@Column(length=64)
    private String cityName = "";
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="stateId", nullable=true)
    private State state;
    
    @Transient
    public Integer stateId;
    
    private boolean capital;
    
    private char priority;

	/**
	 * Default constructor.
	 */
    public City() {
        super();
    }

    /**
     * Key constructor.
     * 
     * @param contextName
     * @param cityCode
     */
    public City(String contextName, String cityCode) {
    	this();
    	setContextName(contextName);
    	setCityCode(cityCode);
    }
    
    /**
     * State constructor.
     * 
     * @param state
     * @param cityCode
     */
    public City(State state, String cityCode) {
    	this(state.getContextName(), cityCode);
    	setState(state);
    }
    
    /**
     * Name constructor.
     * 
     * @param state
     * @param cityCode
     * @param cityName
     */
    public City(State state, String cityCode, String cityName) {
    	this(state, cityCode);
    	setCityName(cityName);
    }
    

    public int compareTo(City next) {
        if (getPriority()==next.getPriority()) {
            if (getCityCode()!=null && next.getCityCode()!=null) {
                return getCityCode().compareTo(next.getCityCode());
            }
            return 0;
        }
        return getPriority()-next.getPriority();
    }


    public int getId() {
        return this.id;
    }

    public int getVersion() {
        return this.version;
    }

    public String getContextName() {
        return this.contextName;
    }

    public String getCityCode() {
        return this.cityCode;
    }

    public String getCityName() {
        return this.cityName;
    }

    public State getState() {
        return this.state;
    }

    public Integer getStateId() {
        return this.stateId;
    }

    public boolean isCapital() {
        return this.capital;
    }

    public char getPriority() {
        return this.priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public void setCapital(boolean capital) {
        this.capital = capital;
    }

    public void setPriority(char priority) {
        this.priority = priority;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof City)) return false;
        final City other = (City) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.id != other.id) return false;
        if (this.version != other.version) return false;
        final Object this$contextName = this.contextName;
        final Object other$contextName = other.contextName;
        if (this$contextName == null ? other$contextName != null : !this$contextName.equals(other$contextName))
            return false;
        final Object this$cityCode = this.cityCode;
        final Object other$cityCode = other.cityCode;
        if (this$cityCode == null ? other$cityCode != null : !this$cityCode.equals(other$cityCode)) return false;
        final Object this$cityName = this.cityName;
        final Object other$cityName = other.cityName;
        if (this$cityName == null ? other$cityName != null : !this$cityName.equals(other$cityName)) return false;
        final Object this$state = this.state;
        final Object other$state = other.state;
        if (this$state == null ? other$state != null : !this$state.equals(other$state)) return false;
        final Object this$stateId = this.stateId;
        final Object other$stateId = other.stateId;
        if (this$stateId == null ? other$stateId != null : !this$stateId.equals(other$stateId)) return false;
        if (this.capital != other.capital) return false;
        if (this.priority != other.priority) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.id;
        result = result * PRIME + this.version;
        final Object $contextName = this.contextName;
        result = result * PRIME + ($contextName == null ? 0 : $contextName.hashCode());
        final Object $cityCode = this.cityCode;
        result = result * PRIME + ($cityCode == null ? 0 : $cityCode.hashCode());
        final Object $cityName = this.cityName;
        result = result * PRIME + ($cityName == null ? 0 : $cityName.hashCode());
        final Object $state = this.state;
        result = result * PRIME + ($state == null ? 0 : $state.hashCode());
        final Object $stateId = this.stateId;
        result = result * PRIME + ($stateId == null ? 0 : $stateId.hashCode());
        result = result * PRIME + (this.capital ? 79 : 97);
        result = result * PRIME + this.priority;
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof City;
    }

    public String toString() {
        return "org.helianto.core.domain.City(id=" + this.id + ", version=" + this.version + ", contextName=" + this.contextName + ", cityCode=" + this.cityCode + ", cityName=" + this.cityName + ", state=" + this.state + ", stateId=" + this.stateId + ", capital=" + this.capital + ", priority=" + this.priority + ")";
    }
}


