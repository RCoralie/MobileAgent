package RMI.Server;

import java.rmi.registry.LocateRegistry;

import RMI.Common._Annuaire;
import RMI.Common._Chaine;

public class Server {

	
	public static void main(String[] args) {
		
		int port = 1099;
		String nomService = "";
		int numService = 1;
		
		// Récupération des arguments
	    if (args.length!=3){
	      System.out.println("Arguments attendus : <port du registry> <nom du service(chaine/annuaire)> <numero du service> ");
	      System.exit(1);
	    }
	    try  {
	      port = Integer.parseInt(args[0]);
	      nomService = args[1];
	      numService = Integer.parseInt(args[2]);
	    }catch(Exception e) {
	      System.out.println("Arguments attendus : <port du registry> <nom du service(chaine/annuaire)> <numero du service>");
	      System.exit(1);
	    }
		
	    try {
	    
	    	// Mise en place d'un security manager : permet de charger dynamiquement certaines classes
			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new SecurityManager());
		    }
			
			// Lancement du registre de noms RMI
			LocateRegistry.createRegistry(port); 
			
			
			// Instanciation et enregistrement dans le registre RMI des objets de classes distantes 
			if(nomService.equals("annuaire")){
				_Annuaire nomAnnuaire = new Annuaire("DataStore/Annuaire.xml");
				java.rmi.Naming.bind("annuaire", nomAnnuaire);
				System.out.println("Enregistrement Annuaire sur le server reussi");
			}
			else if(nomService.equals("chaine")){
				_Chaine nomChaine = new Chaine("DataStore/Hotels" + numService+".xml");
				java.rmi.Naming.bind("chaine" + numService, nomChaine);
				System.out.println("Enregistrement Chaine " + numService + "sur le server reussi");
			}
			else{
				System.out.println("Erreur sur le nom de service : Annuaire / Chaine ");
				System.exit(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
