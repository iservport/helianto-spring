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
import org.helianto.core.domain.enums.ActivityState;
import org.helianto.core.domain.enums.EntityNature;

import javax.persistence.*;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 *              
 * <p>
 * Domain object to represent the logical namespace of a business
 * organization or individual and provide for proper isolation to 
 * other entities trying to access its related classes.
 * </p>
 * <p>
 * For example, if two equipment sets must be distinguished in 
 * logical spaces to avoid identity collision, they
 * must be associated to different entities. This is also applicable for many
 * other domain classes, like accounts, statements, parts, processes, etc.
 * The <code>Entity</code> is the root for many of such objects and allow
 * for the desirable isolation between two or more organizations, or even
 * smaller units within one organization. In other words, an <code>Entity</code>
 * 'controls' a whole group of domain object instances.
 * </p>
 * <p>
 * A real world entity usually has many related properties, like 
 * address or trade mark. An <code>Entity</code> here, though, is 
 * designed not to hold much information, namely only an unique name. That makes 
 * it flexible enough to be associated to virtually any real world 
 * entity, even individuals. 
 * </p>
 * <p>
 * A small footprint is also desirable for some serialization strategies
 * like Hibernate's (www.hibernate.org) non-lazy loading.
 * </p>
 * @author Mauricio Fernandes de Castro
 *              
 *      
 */
@javax.persistence.Entity
@Table(name="core_entity",
    uniqueConstraints = {@UniqueConstraint(columnNames={"operatorId", "alias"})}
)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name="type",
    discriminatorType=DiscriminatorType.CHAR
)
@DiscriminatorValue("0")
public class Entity
{

    private static final long serialVersionUID = 1L;
    
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    
    @Version
    private int version;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="operatorId", nullable=true)
    private Context context;
    
    @Column(length=64)
    private String alias = "";
    
    @Column(length=36)
    private String entityCode = "";
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date installDate = new Date();
    
    private char entityType = 'C';
    
	@Enumerated(EnumType.STRING) @Column(length=20)
    private EntityNature nature = EntityNature.ORGANIZATION;
    
	@Column(length=128)
    private String customStyle = "";
    
	@Column(length=1024)
    private String summary = "";
    
	@Transient
	private Identity manager;
    
	@Column(length=128)
    private String externalLogoUrl = "";
    
    private char activityState = 'A';
    
    @Column(length=128)
    private String entityName = "";
    
    @Column(length=128)
    private String entityDomain = "";
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="cityId")
    private City city;
    
    @Transient
    private int cityId;
    
    /**
     * Default constructor.
     */
    public Entity() {
    	super();
    }

    /** 
     * Context constructor.
     * 
     * @param context
     * @deprecated
     */
    public Entity(Context context) {
    	this();
    	setContext(context);
    }

    /** 
     * Key constructor.
     * 
     * @param context
     * @param alias
     * @deprecated
     */
    public Entity(Context context, String alias) {
    	this(context);
    	setAlias(alias);
   }

    /**
     * City constructor.
     * 
     * @param city
     * @param alias
     */
    public Entity(City city, String alias) {
    	this();
    	setCity(Objects.requireNonNull(city, "A city is required"));
    	setContext(city.getContext());
    	setAlias(alias);
    }
    
	public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Version.
     */
    public int getVersion() {
        return this.version;
    }
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * Context, lazy loaded.
     */
    public Context getContext() {
        return this.context;
    }
    public void setContext(Context context) {
        this.context = context;
    }
    
    public int getContextId() {
    	if (getContext()!=null) {
    		return getContext().getId();
    	}
    	return 0;
    }
    
    public Locale getLocale() {
    	// TODO create locale field.
    	return Locale.getDefault();
    }

    /**
     * Alias.
     */
    public String getAlias() {
        return this.alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    /**
     * Entity code.
     */
    public String getEntityCode() {
		return entityCode;
	}
    public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}
    
    /**
     * Date of installation.
     */
    public Date getInstallDate() {
		return installDate;
	}
    public void setInstallDate(Date installDate) {
		this.installDate = installDate;
	}
    
	/**
	 * <<Transient>> True if install date is not null.
	 */
//	@Transient
	public boolean isInstalled() {
		return getInstallDate()!=null;
	}
	
	public char getEntityType() {
		return entityType;
	}
	public void setEntityType(char entityType) {
		this.entityType = entityType;
	}
	
	/**
	 * Entity nature
	 */
	public EntityNature getNature() {
		return nature;
	}
	public void setNature(EntityNature nature) {
		this.nature = nature;
	}
	
	/**
	 * Custom style.
	 */
	public String getCustomStyle() {
		return customStyle;
	}
	public void setCustomStyle(String customStyle) {
		this.customStyle = customStyle;
	}

	/**
	 * Summary.
	 */
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
    /**
     * <<Transient>> Convenient to hold the manager during installation
	 * 
	 * <p>
	 * Entity installation requires many steps. Please, check
	 * service layer for installation procedures.
	 * <p>
     */
    public Identity getManager() {
		return manager;
	}
    public void setManager(Identity manager) {
		this.manager = manager;
	}
    
    /**
     * Link to an external logo (like http://mysite/img/log).
     */
    public String getExternalLogoUrl() {
		return externalLogoUrl;
	}
    public void setExternalLogoUrl(String externalLogoUrl) {
		this.externalLogoUrl = externalLogoUrl;
	}
    
    /**
     * Activity state.
     */
    public char getActivityState() {
		return activityState;
	}
    public void setActivityState(char activityState) {
		this.activityState = activityState;
	}
    public void setActivityStateAsEnum(ActivityState activityState) {
		this.activityState = activityState.getValue();
	}
    
    /**
     * Domain associated with the entity. e.g. helianto.org.
     */
    public String getEntityDomain() {
		return entityDomain;
	}
    public void setEntityDomain(String entityDomain) {
		this.entityDomain = entityDomain;
	}
    
    /**
     * Entity name.
     */
    public String getEntityName() {
		return entityName;
	}
    public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
    
    /**
     * City.
     */
    public City getCity() {
		return city;
	}
    public void setCity(City city) {
		this.city = city;
	}
    
    /**
     * <<Transient>> city id.
     */
    public int getCityId() {
    	if (getCity()!=null) {
    		return getCity().getId();
    	}
		return cityId;
	}
    public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	/**
	 * Merger.
	 * 
	 * @param command
	 */
	public Entity merge(Entity command) {
		setEntityCode(command.getEntityCode());
		setInstallDate(command.getInstallDate());
		setEntityType(command.getEntityType());
		setNature(command.getNature());
		setCustomStyle(command.getCustomStyle());
		setSummary(command.getSummary());
		setManager(command.getManager());
		setExternalLogoUrl(command.getExternalLogoUrl());
		setActivityState(command.getActivityState());
		setEntityName(command.getEntityName());
		setEntityDomain(command.getEntityDomain());
		return this;
	}

	/**
     * toString
     * @return String
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
        buffer.append("alias").append("='").append(getAlias()).append("' ");
        buffer.append("]");
      
        return buffer.toString();
    }

   /**
    * equals
    */
   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
         if ( (other == null ) ) return false;
         if ( !(other instanceof Entity) ) return false;
         Entity castOther = (Entity) other; 
         
         return ((this.getContext()==castOther.getContext()) || ( this.getContext()!=null && castOther.getContext()!=null && this.getContext().equals(castOther.getContext()) ))
             && ((this.getAlias()==castOther.getAlias()) || ( this.getAlias()!=null && castOther.getAlias()!=null && this.getAlias().equals(castOther.getAlias()) ));
   }
   
   /**
    * hashCode
    */
   public int hashCode() {
         int result = 17;
         result = 37 * result + ( getAlias() == null ? 0 : this.getAlias().hashCode() );
         return result;
   }   

}
