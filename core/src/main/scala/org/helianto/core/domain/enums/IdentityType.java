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
 * Identity types.
 * 
 * @author Mauricio Fernandes de Castro
 */
public enum IdentityType {
    
    /**
     * The identity principal may not be used as email.
     */
    NOT_ADDRESSABLE('N'),
    /**
     * Requires an organization email as principal.
     */
    ORGANIZATIONAL_EMAIL('O', true),
    /**
     * Requires a personal email as principal.
     */
    PERSONAL_EMAIL('P', true),
    // The following new types will replace the previous
    // old ones in future releases
    /**
     * The principal is any user supplied e-mail.
     */
    EMAIL('E', true),
    /**
     * The principal is a plain identity.
     */
    PLAIN('I'),
    /**
     * The principal is automatically assigned by the system.
     */
    SYSTEM_GENERATED('S');
    
    private char value;
    private boolean addressable;
    
    private IdentityType(char value) {
        this(value, false);
    }
    
    private IdentityType(char value, boolean addressable) {
        this.value = value;
        this.addressable = addressable;
    }
    
    /**
     * Identity type value.
     */
    public char getValue() {
        return this.value;
    }
    
    /**
     * True if can be used to send email.
     */
    public boolean isAddressable() {
    	return this.addressable;
    }
    
    /**
     * True if can be used to send email.
     * 
     * @param value
     */
    public static boolean isAddressable(char value) {
    	for (IdentityType identityType: values()) {
    		if (value==identityType.getValue() && identityType.isAddressable()) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Selector.
     * 
     * @param value
     */
    public static IdentityType valueOf(char value) {
    	for (IdentityType identityType: values()) {
    		if (identityType.getValue()==value) {
    			return identityType;
    		} 
    	}
    	return IdentityType.NOT_ADDRESSABLE;
    }

}
