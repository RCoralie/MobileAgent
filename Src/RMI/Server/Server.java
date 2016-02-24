package RMI.Server;

import RMI.Common._Annuaire;
import RMI.Common._Chaine;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;



public class Server {

	
	public static void main(String[] args) {
		
		int port = 1099;
		int nbChaines = 4;
		
		// Récupération des arguments
	    if (args.length==1){
		    try  {
		      port = Integer.parseInt(args[0]);
		    }catch(Exception e) {
		      System.out.println("Arguments attendus : <port du registry (1099 par défault)> ");
		      System.exit(1);
		    }
	    }
		
	    
	    // Mise en place d'un security manager : permet de charger dynamiquement certaines classes
		//if (System.getSecurityManager() == null) {
		//	System.setSecurityManager(new SecurityManager());
	    //}
			
			
		// Instanciation et enregistrement dans les registres RMI des objets de classes distantes 
			
	    try{
	    	
			Registry registre;
			
			// Chaînes
			for(int i=1; i<=nbChaines; i++){
				registre = LocateRegistry.createRegistry(port+i); // Lancement du registre de nom RMI
				_Chaine myChaine = new Chaine("DataStore/Hotels" + i+".xml");
				registre.bind("chaine" + i, myChaine); // Enregistrement de l'objet chaine dans le registre RMI
				System.out.println("Enregistrement Chaine" + i + " sur le server reussi\n");
			}
			
			// Annuaire
			registre = LocateRegistry.createRegistry(port+(nbChaines+1)); // Lancement du registre de nom RMI
			_Annuaire myAnnuaire = new Annuaire("DataStore/Annuaire.xml");
			registre.bind("annuaire", myAnnuaire); // Enregistrement de l'objet annauaire dans le registre RMI
			System.out.println("Enregistrement Annuaire sur le server reussi\n");
		
	    } catch (Exception e) {
			e.printStackTrace();
		}
		
	}	
	
			
	
}
