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

import org.helianto.core.domain.enums.EntityNature;
import org.helianto.core.utils.Mergeable;

import javax.persistence.*;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

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
    uniqueConstraints = {@UniqueConstraint(columnNames={"contextName", "alias"})
			,@UniqueConstraint(columnNames={"cityId", "alias"})}
)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name="type",
    discriminatorType=DiscriminatorType.CHAR
)
@DiscriminatorValue("0")
public class Entity implements Mergeable<Entity>
{

    private static final long serialVersionUID = 1L;
    
    @Id @Column(length=32)
    private String id;
    
    @Version
    private int version;

	@Column(length=20)
	private String contextName = "";

	@Column(length=64)
    private String alias = "";
    
    @Column(length=20)
    private String pun = "";
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date installDate = new Date();
    
    private char entityType = 'C';
    
	@Enumerated(EnumType.STRING) @Column(length=20)
    private EntityNature nature = EntityNature.ORGANIZATION;
    
	@Column(length=128)
    private String customStyle = "";
    
	@Column(length=128)
    private String externalLogoUrl = "";
    
    private char activityState = 'A';
    
    @Column(length=128)
    private String entityName = "";
    
    @Column(length=128)
    private String entityDomain = "";

    @Column(length=12)
    private String stateCode = "";

    @Column(length=32)
    private String cityId = "";

    @Column(length=1024)
	private String entityDesc = "";

	@Column(length=20)
	private Locale locale = Locale.forLanguageTag("pt-BR");

	/**
     * Default constructor.
     */
    public Entity() {
    	super();
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * City constructor.
     * 
     * @param contextName the context name
     * @param alias teh entity alias
     * @param entityName the entity name
     * @param cityId id of the city
     * @param stateCode the state code
     * @param entityType entity type
     * @param pun public unique number, optional
     */
    public Entity(String contextName, String alias, String entityName, String cityId, String stateCode, char entityType, String pun) {
    	this();
        setContextName(Objects.requireNonNull(contextName));
        setAlias(Objects.requireNonNull(alias));
        setEntityName(entityName);
        setCityId(Objects.requireNonNull(cityId));
        setStateCode(stateCode);
        setEntityType(entityType);
        setPun(pun);
    }
    
 	/**
	 * Merger.
	 * 
	 * @param command
	 */
	public Entity merge(Entity command) {
		setPun(command.getPun());
		setInstallDate(command.getInstallDate());
		setEntityType(command.getEntityType());
		setNature(command.getNature());
		setCustomStyle(command.getCustomStyle());
		setExternalLogoUrl(command.getExternalLogoUrl());
		setActivityState(command.getActivityState());
		setEntityName(command.getEntityName());
		setEntityDomain(command.getEntityDomain());
        setCityId(command.getCityId());
        setEntityDesc(command.getEntityDesc());
        setLocale(command.getLocale());
		return this;
	}

    public Entity verify(String contextName) {
        if (this.contextName!=null && this.contextName.equals(contextName)) {
            return this;
        }
        throw new IllegalArgumentException("Invalid context!");
    }


    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Entity)) return false;
        final Entity other = (Entity) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        if (this.getVersion() != other.getVersion()) return false;
        final Object this$contextName = this.getContextName();
        final Object other$contextName = other.getContextName();
        if (this$contextName == null ? other$contextName != null : !this$contextName.equals(other$contextName))
            return false;
        final Object this$alias = this.getAlias();
        final Object other$alias = other.getAlias();
        if (this$alias == null ? other$alias != null : !this$alias.equals(other$alias)) return false;
        final Object this$entityCode = this.getPun();
        final Object other$entityCode = other.getPun();
        if (this$entityCode == null ? other$entityCode != null : !this$entityCode.equals(other$entityCode))
            return false;
        final Object this$installDate = this.getInstallDate();
        final Object other$installDate = other.getInstallDate();
        if (this$installDate == null ? other$installDate != null : !this$installDate.equals(other$installDate))
            return false;
        if (this.getEntityType() != other.getEntityType()) return false;
        final Object this$nature = this.getNature();
        final Object other$nature = other.getNature();
        if (this$nature == null ? other$nature != null : !this$nature.equals(other$nature)) return false;
        final Object this$customStyle = this.getCustomStyle();
        final Object other$customStyle = other.getCustomStyle();
        if (this$customStyle == null ? other$customStyle != null : !this$customStyle.equals(other$customStyle))
            return false;
        final Object this$externalLogoUrl = this.getExternalLogoUrl();
        final Object other$externalLogoUrl = other.getExternalLogoUrl();
        if (this$externalLogoUrl == null ? other$externalLogoUrl != null : !this$externalLogoUrl.equals(other$externalLogoUrl))
            return false;
        if (this.getActivityState() != other.getActivityState()) return false;
        final Object this$entityName = this.getEntityName();
        final Object other$entityName = other.getEntityName();
        if (this$entityName == null ? other$entityName != null : !this$entityName.equals(other$entityName))
            return false;
        final Object this$entityDomain = this.getEntityDomain();
        final Object other$entityDomain = other.getEntityDomain();
        if (this$entityDomain == null ? other$entityDomain != null : !this$entityDomain.equals(other$entityDomain))
            return false;
        final Object this$cityId = this.getCityId();
        final Object other$cityId = other.getCityId();
        if (this$cityId == null ? other$cityId != null : !this$cityId.equals(other$cityId)) return false;
        final Object this$entityDesc = this.getEntityDesc();
        final Object other$entityDesc = other.getEntityDesc();
        if (this$entityDesc == null ? other$entityDesc != null : !this$entityDesc.equals(other$entityDesc))
            return false;
        final Object this$locale = this.getLocale();
        final Object other$locale = other.getLocale();
        if (this$locale == null ? other$locale != null : !this$locale.equals(other$locale)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 0 : $id.hashCode());
        result = result * PRIME + this.getVersion();
        final Object $contextName = this.getContextName();
        result = result * PRIME + ($contextName == null ? 0 : $contextName.hashCode());
        final Object $alias = this.getAlias();
        result = result * PRIME + ($alias == null ? 0 : $alias.hashCode());
        final Object $entityCode = this.getPun();
        result = result * PRIME + ($entityCode == null ? 0 : $entityCode.hashCode());
        final Object $installDate = this.getInstallDate();
        result = result * PRIME + ($installDate == null ? 0 : $installDate.hashCode());
        result = result * PRIME + this.getEntityType();
        final Object $nature = this.getNature();
        result = result * PRIME + ($nature == null ? 0 : $nature.hashCode());
        final Object $customStyle = this.getCustomStyle();
        result = result * PRIME + ($customStyle == null ? 0 : $customStyle.hashCode());
        final Object $externalLogoUrl = this.getExternalLogoUrl();
        result = result * PRIME + ($externalLogoUrl == null ? 0 : $externalLogoUrl.hashCode());
        result = result * PRIME + this.getActivityState();
        final Object $entityName = this.getEntityName();
        result = result * PRIME + ($entityName == null ? 0 : $entityName.hashCode());
        final Object $entityDomain = this.getEntityDomain();
        result = result * PRIME + ($entityDomain == null ? 0 : $entityDomain.hashCode());
        final Object $cityId = this.getCityId();
        result = result * PRIME + ($cityId == null ? 0 : $cityId.hashCode());
        final Object $entityDesc = this.getEntityDesc();
        result = result * PRIME + ($entityDesc == null ? 0 : $entityDesc.hashCode());
        final Object $locale = this.getLocale();
        result = result * PRIME + ($locale == null ? 0 : $locale.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Entity;
    }

    public String toString() {
        return "org.helianto.core.domain.Entity(id=" + this.getId() + ", version=" + this.getVersion() + ", contextName=" + this.getContextName() + ", alias=" + this.getAlias() + ", pun=" + this.getPun() + ", installDate=" + this.getInstallDate() + ", entityType=" + this.getEntityType() + ", nature=" + this.getNature() + ", customStyle=" + this.getCustomStyle() + ", externalLogoUrl=" + this.getExternalLogoUrl() + ", activityState=" + this.getActivityState() + ", entityName=" + this.getEntityName() + ", entityDomain=" + this.getEntityDomain() + ", cityId=" + this.getCityId() + ", entityDesc=" + this.getEntityDesc() + ", locale=" + this.getLocale() + ")";
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

    public String getAlias() {
        return this.alias;
    }

    public String getPun() {
        return this.pun;
    }

    public Date getInstallDate() {
        return this.installDate;
    }

    public char getEntityType() {
        return this.entityType;
    }

    public EntityNature getNature() {
        return this.nature;
    }

    public String getCustomStyle() {
        return this.customStyle;
    }

    public String getExternalLogoUrl() {
        return this.externalLogoUrl;
    }

    public char getActivityState() {
        return this.activityState;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public String getEntityDomain() {
        return this.entityDomain;
    }

    public String getStateCode() {
        return this.stateCode;
    }

    public String getCityId() {
        return this.cityId;
    }

    public String getEntityDesc() {
        return this.entityDesc;
    }

    public Locale getLocale() {
        return this.locale;
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

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setPun(String pun) {
        this.pun = pun;
    }

    public void setInstallDate(Date installDate) {
        this.installDate = installDate;
    }

    public void setEntityType(char entityType) {
        this.entityType = entityType;
    }

    public void setNature(EntityNature nature) {
        this.nature = nature;
    }

    public void setCustomStyle(String customStyle) {
        this.customStyle = customStyle;
    }

    public void setExternalLogoUrl(String externalLogoUrl) {
        this.externalLogoUrl = externalLogoUrl;
    }

    public void setActivityState(char activityState) {
        this.activityState = activityState;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public void setEntityDomain(String entityDomain) {
        this.entityDomain = entityDomain;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public void setEntityDesc(String entityDesc) {
        this.entityDesc = entityDesc;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
