package jus.aor.mobilagent.hello;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import jus.aor.mobilagent.kernel.Agent;
import jus.aor.mobilagent.kernel._Action;

/**
 * Classe de test élémentaire pour le bus à agents mobiles
 * @author  Morat
 */
public class Hello extends Agent{

	private static final long serialVersionUID = 1L;
	private List<String> helloList = new LinkedList<String>();
	
	/**
	  * Construction d'un agent de type hello.
	  * @param args aucun argument n'est requis
	  */
	 public Hello(Object... args) {
		 super();
	 }
	 
	 
	 
	 /**
	 * Action à entreprendre sur les serveurs visités  
	 */
	protected _Action doIt = new _Action(){
		private static final long serialVersionUID = 1L;
		public void execute() {
			Date date = new Date();
	        DateFormat dt = new SimpleDateFormat("hh:mm:ss:SSS");
			helloList.add(getServerName()+ " : " +dt.format(date) + "\n");
			System.out.println("Hello!");
		}
	};
	 
	 /**
	 * Action à entreprendre sur le serveur de retour
	 */
	protected _Action retour = new _Action(){
		private static final long serialVersionUID = 1L;
		public void execute() {
			for(String temp : helloList){
				System.out.println(temp);
			}
		}			
	};
}
