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

package org.helianto.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.helianto.user.domain.enums.AssociationState;

import javax.persistence.*;
import java.util.Date;
/**
 * User group associations.
 * 
 * @author Mauricio Fernandes de Castro	
 */
@Entity
@Table(name="core_userassoc", 
    uniqueConstraints = {@UniqueConstraint(columnNames={"parentId", "childId"})}
)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name="type",
    discriminatorType=DiscriminatorType.CHAR
)
@DiscriminatorValue("A")
public class UserAssociation implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Version
    private int version;

    private int sequence;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="parentId", nullable=true)
    private UserGroup parent;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="childId", nullable=true)
    private UserGroup child;

    @Temporal(TemporalType.TIMESTAMP)
    private Date associationDate = new Date();
    
    private char resolution = AssociationState.ACTIVE.getValue();
    
    @Column(length=512)
    private String parsedContent;

    /** 
     * Default constructor.
     */
    public UserAssociation() {
    	super();
    }

    /** 
     * Parent constructor.
     * 
     * @param parent
     */
    public UserAssociation(UserGroup parent) {
    	this();
    	setParent(parent);
    }

    /** 
     * Child constructor.
     * 
     * @param parent
     * @param child
     */
    public UserAssociation(UserGroup parent, UserGroup child) {
    	this(parent);
    	setChild(child);
    }

    /**
     * Sequence.
     */
    public int getSequence() {
        return sequence;
    }
    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    /**
     * Parent.
     */
    public UserGroup getParent() {
        return parent;
    }
    public void setParent(UserGroup parent) {
        this.parent = parent;
    }

    /**
     * Child.
     */
    public UserGroup getChild() {
        return child;
    }
    public void setChild(UserGroup child) {
        this.child = child;
    }

    protected int compareChild(UserAssociation other) {
    	if (this.getChild()!=null && other.getChild()!=null) {
    		return this.getChild().compareTo((UserGroup) other.getChild());
    	}
    	return 0;
    }
       
    /**
     * Association resolution.
     */
    public char getResolution() {
		return resolution;
	}
    public void setResolution(char resolution) {
		this.resolution = resolution;
	}
    public void setResolutionAsEnum(AssociationState associationState) {
		this.resolution = associationState.getValue();
	}
    
    /**
     * Association date.
     */
    public Date getAssociationDate() {
		return associationDate;
	}
    public void setAssociationDate(Date associationDate) {
		this.associationDate = associationDate;
	}

    /**
     * Parsed content.
     */
    public String getParsedContent() {
		return parsedContent;
	}
    public void setParsedContent(String parsedContent) {
		this.parsedContent = parsedContent;
	}
    
	public String[] getParsedContentAsArray() {
		if (getParsedContent()!=null) {
			return getParsedContent().split(",");
		}
		return new String[] {};
	}
	public void setParsedContentAsArray(String[] parsedContentArray) {
		setParsedContent(parsedContentArray.toString().replace("[", "").replace("]", ""));
	}

    /**
     * equals
     */
    @SuppressWarnings("unchecked")
    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof UserAssociation)) return false;
        UserAssociation castOther = (UserAssociation) other;

        return ((this.getParent() == castOther.getParent()) || (this
                .getParent() != null
                && castOther.getParent() != null && this.getParent().equals(
                castOther.getParent())))
                && ((this.getChild() == castOther.getChild()) || (this
                .getChild() != null
                && castOther.getChild() != null && this.getChild()
                .equals(castOther.getChild())));
    }

    /**
     * hashCode
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + (int) this.getSequence();
        result = 37 * result
                + (getChild() == null ? 0 : this.getChild().hashCode());
        return result;
    }

}
