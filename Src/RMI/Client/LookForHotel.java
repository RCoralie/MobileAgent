/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */

package RMI.Client;

import RMI.Common.Hotel;
import RMI.Common.Numero;
import RMI.Common._Annuaire;
import RMI.Common._Chaine;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Représente un client effectuant une requête lui permettant d'obtenir les numéros de téléphone des hôtels répondant à son critère de choix.
 */

public class LookForHotel{

	private int port = 1099;
	private int nbChaines = 4;
	
	private String localisation;
	private _Annuaire annuaire;
	private List<_Chaine> listChaines = new ArrayList<_Chaine>();
	private List<Hotel> listHotels = new ArrayList<Hotel>();
	private HashMap<String, Numero> listNumeros = new HashMap<String, Numero>();

	
	
	/**
	 * Définition de l'objet représentant l'interrogation.
	 * @param args les arguments n'en comportant qu'un seul qui indique le critère de localisation
	 */
	public LookForHotel(String... args){
		
		/* Recuperation de la localisation */
		if(args.length != 1){
			System.out.println("Arguments invalides : <localisation>");
			System.exit(1);
		}
		this.localisation = args[0];
		
		/* Recuperation des objets distants : Chaines & Annuaire */
		
		Registry registre;
		
		// Recuperation des chaînes
		try {
			for(int i=1; i<=nbChaines; i++){
				registre = LocateRegistry.getRegistry(port+i);
				listChaines.add((_Chaine) registre.lookup("chaine"+i));
			}
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
		
		// Recuperation de l'annuaire
		try {
			registre = LocateRegistry.getRegistry(port+(nbChaines+1));
			annuaire = (_Annuaire) registre.lookup("annuaire");
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
		
	}
		
	
	/**
	 * Réalise une intérrogation
	 * @return la durée de l'interrogation
	 * @throws RemoteException
	 */
	public long call() {
		
		// Initialisation du temps
		long begin = System.currentTimeMillis();
		
		try {
			// Recuperation de tous les hotels correspondant à la localisation (de toutes les chaines)
			for(int i=0; i<listChaines.size(); i++){
				List<Hotel> lh = listChaines.get(i).get(localisation);
				listHotels.addAll(lh);
			}
			System.out.println("Il y a " + listHotels.size() + " Hotels dans "+ localisation);
				
			// Recherche des numéros de telephones pour chaque hotel de la liste
			for (Hotel h : listHotels){
				listNumeros.put(h.name, annuaire.get(h.name));
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		// Calcul de la durée de traitement
		long duration = System.currentTimeMillis() - begin;
		return duration;
		
	}

}
