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

package org.helianto.core.domain.enums;

/**
 * Gender
 * 
 * @author Mauricio Fernandes de Castro
 */
public enum Gender {
    
    NOT_SUPPLIED('N'),
    MALE('M'),
    FEMALE('F');
    
    private char value;
    
    private Gender(char value) {
        this.value = value;
    }
    
    public char getValue() {
        return value;
    }
    
    /**
     * Selector.
     * 
     * @param value
     */
    public static Gender valueOf(char value) {
    	for (Gender gender: values()) {
    		if (gender.getValue()==value) {
    			return gender;
    		} 
    	}
    	return Gender.NOT_SUPPLIED;
    }



}
