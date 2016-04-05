/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */

package jus.aor.mobilagent.LookForHotel;

import jus.aor.mobilagent.LookForHotel.Hotel;
import jus.aor.mobilagent.LookForHotel.Numero;
import jus.aor.mobilagent.LookForHotel._Annuaire;
import jus.aor.mobilagent.LookForHotel._Chaine;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import jus.aor.mobilagent.kernel.Agent;
import jus.aor.mobilagent.kernel._Action;
import jus.aor.mobilagent.kernel._Service;


/** Représente un client effectuant une requête lui permettant d'obtenir les numéros de téléphone des hôtels répondant à son critère de choix.*/
public class LookForHotel extends Agent{

	private static final long serialVersionUID = 1L;

	private int port = 1099;
	private int nbChaines = 4;
	
	private String localisation;
	private _Annuaire annuaire;
	private List<_Chaine> listChaines = new ArrayList<_Chaine>();
	private List<Hotel> listHotels = new ArrayList<Hotel>();
	private HashMap<String, Numero> listNumeros = new HashMap<String, Numero>();

	
	
	/**
	 * Définition de l'objet représentant l'interrogation.
	 * @param critère de localisation, nombre de chaines d'hotels
	 */
	public LookForHotel(String localisation){
		
		this.localisation = localisation;
		
		/* Mise en place d'un security manager : permet de charger dynamiquement certaines classes */
		//if (System.getSecurityManager() == null) {
		//	System.setSecurityManager(new SecurityManager());
	    //}
		
		/* Recuperation des objets distants : Chaines & Annuaire */
		
		Registry registre;
		
		try {
			// Recuperation des chaînes
			for(int i=1; i<=nbChaines; i++){
				registre = LocateRegistry.getRegistry(port+i);
				listChaines.add((_Chaine) registre.lookup("chaine"+i));
			}
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
		
		try {
			// Recuperation de l'annuaire
			registre = LocateRegistry.getRegistry(port+(nbChaines+1));
			annuaire = (_Annuaire) registre.lookup("annuaire");
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
		
	}
		
	
	/**
	 * Réalise une intérrogation
	 * @return 
	 * @return la durée de l'interrogation
	 * @throws RemoteException
	 */
	public long call() {
		// Initialisation du temps
		long begin = System.currentTimeMillis();
		// Calcul de la durée de traitement
		long duration = System.currentTimeMillis() - begin;
		return duration;
	}
	
	
	/**
	 * Lance la requête et calcul son temps d'exécution
	 * @param <localisation(optionnel)> <nombre de chaine d'hotels(optionnel)>
	 */
	public static void main(String[] args) {
		
		if(args.length != 1){
			System.out.println("Arguments attendus : <localisation>");
			System.exit(1);
		}
		LookForHotel lfh= new LookForHotel(args[0]);
		
		long duration = lfh.call();
		System.out.println("La requête à été exécutée en " + duration +" ms.\n");
	}
	
	
	
	
	/** Service : Recherche des numéros dans l'annuaire
	 * 
	 */
	protected _Action findTelephone = new _Action(){
		private static final long serialVersionUID = 1L;
		public void execute() {
			System.out.println("Recherche dans l'annuaire...");
			_Service<?> service = server.getService("Telephones");
			listNumeros = (HashMap<String, Numero>) service.call(listHotels);
		}
	};
	
	/** Service : recherche des hotels correspondant à la localisation
	 * 
	 */
	protected _Action findHotel = new _Action(){
		private static final long serialVersionUID = 1L;
		public void execute() {
			System.out.println("Recherche des hotels...");
			_Service<?> service = server.getService("Hotels");
			listHotels = (List<Hotel>) service.call(localisation);

		}
		
	};
	

}
