/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */

package jus.aor.rmi.Common;

import java.io.Serializable;


/** Un hotel est caractérisé par son nom et sa localisation.*/

public class Hotel implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public String localisation; //localisation de l'hôtel
	public String name; //nom de l'hôtel
	
	/**
	 * Définition d'un hôtel par son nom et sa localisation.
	 * @param name le nom de l'hôtel
	 * @param localisation la localisation de l'hôtel
	 */
	public Hotel(String name, String localisation) { 
		this.name=name; this.localisation=localisation;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Hotel{"+name+","+localisation+"}";
	}
}
