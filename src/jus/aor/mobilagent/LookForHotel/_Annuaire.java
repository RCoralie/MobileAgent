package jus.aor.mobilagent.LookForHotel;


/**
 * Définit un annuaire téléphonique élémentaire permettant, étant donnée un abonné, d'obtenir son numéro de téléphone.
 */

public interface _Annuaire{
	
	/**
	 * restitue le numéro de téléphone de l'abonné
	 * @param abonne l'abonné
	 * @return le numéro de télephone de l'abonné
	 * @throws RemoteException 
	 */
	public Numero get(String abonne);
}