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

import org.helianto.core.domain.enums.Appellation;
import org.helianto.core.domain.enums.Gender;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;


/**
 * Personal data, if any.
 * 
 * @author Mauricio Fernandes de Castro
 */
@Embeddable
public class PersonalData implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Column(length=64)
    private String firstName = "";
    
    @Column(length=64)
    private String lastName = "";
    
    private char gender = Gender.NOT_SUPPLIED.getValue();
    
    private char appellation = Appellation.NOT_SUPPLIED.getValue();
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDate = new Date(0l);
    
    @Column(length=128)
	private String imageUrl = "";
	
    /** 
     * Default constructor.
     */
    public PersonalData() {
    	super();
    }
    
    /**
     * Name constructor.
     * 
     * @param firstName
     * @param lastName
     */
    public PersonalData(String firstName, String lastName) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Name and image constructor.
     *
     * @param firstName
     * @param lastName
     * @param imageUrl
     */
    public PersonalData(String firstName, String lastName, String imageUrl) {
        this(firstName, lastName);
        this.imageUrl = imageUrl;
    }

    /**
     * Convenience constructor.
     * 
     * @param firstName
     * @param lastName
     * @param gender
     * @param appellation
     * @param birthDate
     * @param imageUrl
     */
    public PersonalData(String firstName, String lastName, char gender,
			char appellation, Date birthDate, String imageUrl) {
		this(firstName, lastName, imageUrl);
		this.gender = gender;
		this.appellation = appellation;
		this.birthDate = birthDate;
	}

	/**
     * First name.
     */
    public String getFirstName() {
        return this.firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Last name.
     */
    public String getLastName() {
        return this.lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gender.
     */
    public char getGender() {
        return this.gender;
    }
    public void setGender(char gender) {
        this.gender = gender;
    }
    public void setGenderAsEnum(Gender gender) {
        this.gender = gender.getValue();
    }

    /**
     * Appellation.
     */
    public char getAppellation() {
        return this.appellation;
    }
    public void setAppellation(char appellation) {
        this.appellation = appellation;
    }
    
    /**
     * Birth date.
     */
    public Date getBirthDate() {
		return birthDate;
	}
    public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
    
    /**
     * Image url.
     */
    public String getImageUrl() {
		return imageUrl;
	}
    public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
    
}
