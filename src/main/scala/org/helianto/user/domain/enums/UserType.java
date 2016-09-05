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

package org.helianto.user.domain.enums;

/**
 * User types. 
 * 
 * @author Mauricio Fernandes de Castro
 */
public enum UserType {
    
    SYSTEM('S'),
    ALL('A'),
    FUNCTION('F'),
    JOB('J'),
    THIRD_PARTY('Y'),
    INTERNAL('I');
    
    private char value;
    private UserType(char type) {
        this.value = type;
    }
    public char getValue() {
        return value;
    }
    
    /**
     * Selector.
     * 
     * @param value
     */
    public static UserType valueOf(char value) {
    	for (UserType userType: values()) {
    		if (userType.getValue()==value) {
    			return userType;
    		}
    	}
    	return UserType.INTERNAL;
    }

}
