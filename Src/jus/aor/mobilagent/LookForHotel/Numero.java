/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */

package jus.aor.mobilagent.LookForHotel;

import java.io.Serializable;


/** Un numéro de téléphone */

public class Numero implements Serializable{

	private static final long serialVersionUID = 1L;
	public String numero; //numéro de téléphone
	
	/**
	 * Construction d'un numéro de téléphone.
	 * @param numero le numéro
	 */
	public Numero(String numero) { 
		this.numero=numero;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() { 
		return numero;
	}
}
