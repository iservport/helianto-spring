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


/**
 * City.
 * 
 * @author Mauricio Fernandes de Castro
 */
@javax.persistence.Entity
@Table(name="core_city",
    uniqueConstraints = {@UniqueConstraint(columnNames={"contextName", "stateCode", "cityCode"})}
)
public class City
	implements Serializable, Comparable<City> {

    private static final long serialVersionUID = 1L;
    
    @Id @Column(length=32)
    private String id;
    
    @Version
    private int version;
    
	@Column(length=20)
	private String contextName = "";

    @Column(length=12)
    private String stateCode;

    @Transient
    private State state;

    @Column(length=12)
	private String cityCode = "";

	@Column(length=64)
    private String cityName = "";

    private boolean capital = false;
    
    private char priority = '0';

	/**
	 * Default constructor.
	 */
    public City() {
        super();
    }

    /**
     * State constructor.
     * 
     * @param contextName
     * @param stateCode
     * @param cityCode
     */
    public City(String contextName, String stateCode, String cityCode) {
        this();
        setContextName(contextName);
        setStateCode(stateCode);
        setCityCode(cityCode);
    }

    /**
     * Form constructor.
     *
     * @param contextName
     * @param stateCode
     * @param cityCode
     * @param cityName
     * @param capital
     */
    public City(String contextName, String stateCode, String cityCode, String cityName, boolean capital) {
        this(contextName, stateCode, cityCode);
        this.cityName = cityName;
        this.capital = capital;
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

    public State getState() {
        return this.state;
    }

    public City setState(State state) {
        this.state = state;
        return this;
    }

    public String getStateName() {
        if (getState()!=null) {
            return getState().getStateName();
        }
        return "";
    }

    public String getStateAlias() {
        if (getState()!=null) {
            return getState().getStateAlias();
        }
        return "";
    }

    public String getCountryCode() {
        if (getState()!=null) {
            return getState().getCountryCode();
        }
        return "";
    }

    public String getId() {
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

    public String getStateCode() {
        return this.stateCode;
    }

    public boolean isCapital() {
        return this.capital;
    }

    public char getPriority() {
        return this.priority;
    }

    public void setId(String id) {
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

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
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
        final Object this$id = this.id;
        final Object other$id = other.id;
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
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
        final Object this$stateCode = this.stateCode;
        final Object other$stateCode = other.stateCode;
        if (this$stateCode == null ? other$stateCode != null : !this$stateCode.equals(other$stateCode)) return false;
        if (this.capital != other.capital) return false;
        if (this.priority != other.priority) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.id;
        result = result * PRIME + ($id == null ? 0 : $id.hashCode());
        result = result * PRIME + this.version;
        final Object $contextName = this.contextName;
        result = result * PRIME + ($contextName == null ? 0 : $contextName.hashCode());
        final Object $cityCode = this.cityCode;
        result = result * PRIME + ($cityCode == null ? 0 : $cityCode.hashCode());
        final Object $cityName = this.cityName;
        result = result * PRIME + ($cityName == null ? 0 : $cityName.hashCode());
        final Object $stateCode = this.stateCode;
        result = result * PRIME + ($stateCode == null ? 0 : $stateCode.hashCode());
        result = result * PRIME + (this.capital ? 79 : 97);
        result = result * PRIME + this.priority;
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof City;
    }

    public String toString() {
        return "org.helianto.core.domain.City(id=" + this.id + ", version=" + this.version + ", contextName=" + this.contextName + ", cityCode=" + this.cityCode + ", cityName=" + this.cityName + ", stateCode=" + this.stateCode + ", capital=" + this.capital + ", priority=" + this.priority + ")";
    }
}


