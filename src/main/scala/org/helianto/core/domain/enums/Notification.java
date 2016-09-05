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
 * Notification types.
 * 
 * @author Mauricio Fernandes de Castro
 */
public enum Notification {

    /**
     * Notify using principal field.
     */
    PRINCIPAL('P'),
    /**
     * Notify using e-mail field.
     */
    EMAIL('E'),
    /**
     * Email notification is automatic.
     * @deprecated
     */
    AUTOMATIC('A'),
    /**
     * Email notification only if requested.
     */
    BY_REQUEST('R');
    
    private char value;
    
    private Notification(char value) {
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
    public static Notification valueOf(char value) {
    	for (Notification notification: values()) {
    		if (notification.getValue()==value) {
    			return notification;
    		} 
    	}
    	return Notification.AUTOMATIC;
    }
    
}
