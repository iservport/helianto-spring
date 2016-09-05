package org.helianto.core.domain.enums;



/** 
 * Appellations.
 * 
 * @author Mauricio Fernandes de Castro
 */
public enum Appellation {
    
    /**
     * Not supplied.
     */
    NOT_SUPPLIED('_'),
    /**
     * Miss.
     */
    MISS('1'),
    /**
     * Mister or Mistress. Further distinction depends on 
     * the gender.
     */
    MR_OR_MRS('2'),
    /**
     * Miss or Mistress
     */
    MS('3'),
    /**
     * Not supplied.
     */
    UNDEFINED('0');
    
    private char value;
    
    private Appellation(char value) {
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
    public static Appellation valueOf(char value) {
    	for (Appellation appellation: values()) {
    		if (appellation.getValue()==value) {
    			return appellation;
    		} 
    	}
    	return Appellation.NOT_SUPPLIED;
    }

}
