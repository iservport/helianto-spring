package org.helianto.user.domain.enums;

/**
 * Association state definition.
 * 
 * @author mauriciofernandesdecastro
 */
public enum AssociationState {
	
	ACTIVE('A'),
	TEMPORARY('T'),
	INACTIVE('I'),
	CANCELLED('C');
	
	private AssociationState(char value) {
		this.value = value;
	}
	
	private char value;
	
	/** 
	 * Value assigned to this enum.
	 */
	public char getValue() {
		return value;
	}

}
