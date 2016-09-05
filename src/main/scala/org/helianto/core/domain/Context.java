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
import java.util.Locale;

/**
 * The <code>Context</code> domain class represents a mandatory
 * parent entity to any Helianto based installation. Every domain
 * object is traceable to one (or a chain of) <code>Context</code>.
 * 
 * @author Mauricio Fernandes de Castro
 */
@Entity
@Table(name="core_context",
    uniqueConstraints = {@UniqueConstraint(columnNames={"contextName"})}
)
public class Context implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    
    @Column(length=20)
    private String contextName;
    
    private Locale locale;
    
    /**
     * Default constructor.
     */
    public Context() {
    	this("");
    }

    /** 
     * Key constructor.
     * 
     * @param contextName
     */
    public Context(String contextName) {
    	this(contextName, Locale.getDefault());
    }

    /** 
     * Locale constructor.
     * 
     * @param contextName
     * @param locale
     */
    public Context(String contextName, Locale locale) {
    	setContextName(contextName);
        setLocale(locale);
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

    /**
     * Locale.
     */
    public Locale getLocale() {
        return this.locale;
    }
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * toString
     * @return String
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
        buffer.append("contextName").append("='").append(getContextName()).append("' ");
        buffer.append("]");
      
        return buffer.toString();
    }

   /**
    * equals
    */
   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
         if ( (other == null ) ) return false;
         if ( !(other instanceof Context) ) return false;
         Context castOther = (Context) other;
         
         return (this.getContextName()==castOther.getContextName()) || ( this.getContextName()!=null && castOther.getContextName()!=null && this.getContextName().equals(castOther.getContextName()) );
   }
   
   /**
    * hashCode
    */
   public int hashCode() {
         int result = 17;
         result = 37 * result + ( getContextName() == null ? 0 : this.getContextName().hashCode() );
         return result;
   }   

}
