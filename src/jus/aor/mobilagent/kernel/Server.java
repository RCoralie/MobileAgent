/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jdk.nashorn.internal.ir.CatchNode;
import jus.aor.mobilagent.kernel.BAMAgentClassLoader;
import jus.aor.mobilagent.kernel._Agent;

/** Le serveur principal permettant le lancement d'un serveur d'agents mobiles et les fonctions permettant de déployer des services et des agents.
 *  > Créé un AgentServer
 *  > Charge les services sur cet AgentServer
 *  > Lance les agents mobiles sur cet AgentServer
 */
public final class Server implements _Server {
	
	protected String name; /** le nom logique du serveur */
	protected int port=10140; /** le port où sera ataché le service du bus à agents mobiles. Pafr défaut on prendra le port 10140 */
	protected AgentServer agentServer; /** le server d'agent démarré sur ce noeud */

	protected String loggerName; /** le nom du logger */
	protected Logger logger=null; /** le logger de ce serveur */
	
	
	/**
	 * Démarre un serveur de type mobilagent 
	 * @param port le port d'écoute du serveur d'agent 
	 * @param name le nom du serveur
	 */
	public Server(final int port, final String name){
		this.name=name;
		try {
			this.port=port;
			/* mise en place du logger pour tracer l'application */
			loggerName = "jus/aor/mobilagent/"+InetAddress.getLocalHost().getHostName()+"/"+this.name;
			logger=Logger.getLogger(loggerName);
			/* **TODO** : Démarrage du server d'agents mobiles attaché à cette machine */ 
			agentServer = new AgentServer(this.port, this.name); // instanciation du server d'agents
			Thread.sleep(1000); //temporisation de mise en place du server d'agents
			agentServer.start(); //lancement du serveur
		}catch(Exception ex){
			logger.log(Level.FINE," Erreur durant le lancement du serveur"+this,ex);
			return;
		}
	}
	
	
	/**
	 * Ajoute le service caractérisé par les arguments
	 * @param name nom du service
	 * @param classeName classe du service
	 * @param codeBase codebase du service
	 * @param args arguments de construction du service
	 */
	public final void addService(String name, String classeName, String codeBase, Object... args) {
		try {
			// On récupère le ServerClassLoader de Server et on ajoute le codeBase du service dedans
			BAMServerClassLoader serverClass = new BAMServerClassLoader(new URL[]{},this.getClass().getClassLoader());
			serverClass.addURL(new URL(codeBase));
			// On récupère la classe du service dans le ServerClassLoader
			Class<_Service> serviceClasse = (Class<_Service>)Class.forName(classeName,true,serverClass);
			// On récupère le constructeur du service dans sa classe et on instancie ainsi un nouveau service
			_Service<?> service = (_Service<?>) serviceClasse.getConstructor(Object[].class).newInstance(new Object[]{args});
			// On ajoute le service dans l'agentServer (serveur qui accepte et traite les agents mobiles)
			agentServer.addService(name, service);
			//serverClass.close();
				
		}catch(Exception ex){
			logger.log(Level.FINE," Erreur durant le lancement du serveur"+this,ex);
			return;
		}
	}
	/**
	 * deploie l'agent caractérisé par les arguments sur le serveur
	 * @param classeName classe du service
	 * @param args arguments de construction de l'agent
	 * @param codeBase codebase du service
	 * @param etapeAddress la liste des adresse des étapes
	 * @param etapeAction la liste des actions des étapes
	 */
	public final void deployAgent(String classeName, Object[] args, String codeBase, List<String> etapeAddress, List<String> etapeAction) {
		try {
			// On récupère le ServerClassLoader de Server et on ajoute le codeBase de l'agent dedans
			BAMAgentClassLoader agentClass = new BAMAgentClassLoader(new URL[]{},this.getClass().getClassLoader());
			agentClass.addURL(codeBase);
			// On récupère la classe de l'agent dans le ServerClassLoader
			Class<_Agent> agentClasse = (Class<_Agent>)Class.forName(classeName,true,agentClass);
			// On récupère le constructeur de l'agent dans sa classe et on créer ainsi un nouvel agent
			_Agent agent = (_Agent) agentClasse.getConstructor(Object[].class).newInstance(new Object[]{args});
			agent.init(agentClass, this.agentServer, this.name);// On initialise l'agent sur ce serveur de départ
			// Remplissage de la feuille de route de l'agent
			//etapeAction.add(etapeAction.size(), "retour");
			//etapeAddress.add(etapeAddress.size(),"mobilagent://localhost:" + this.port+"/"); /*TODO : On ne part pas du premier server !!! etapeAddress.get(0)*/
			System.out.println("Nombre d'étapes :" + etapeAddress.size());
			for (int i=0; i<etapeAction.size(); i++){
				// on récupère l'action de l'étape i
				Field field = agentClasse.getDeclaredField(etapeAction.get(i));
				field.setAccessible(true);
				_Action action = (_Action) field.get(agent);
				// on ajoute l'étape (serveur + action à éxecuter sur le serveur)
				agent.addEtape(new Etape(new URI(etapeAddress.get(i)),action ));
				System.out.println("Ajout de l'étape " + etapeAction.get(i) + " sur " + etapeAddress.get(i) + " -- OK");
			}
			startAgent(agent, agentClass);
		}catch(Exception ex){
			logger.log(Level.FINE," Erreur durant le lancement du serveur"+this,ex);
			return;
		}
	}
	
	
	
	/**
	 * Primitive permettant de "mover" un agent sur ce serveur en vue de son exécution immédiate.
	 * @param agent l'agent devant être exécuté
	 * @param loader le loader à utiliser pour charger les classes.
	 * @throws Exception
	 */
	protected void startAgent(_Agent agent, BAMAgentClassLoader loader) throws Exception {
		// **TODO**
		/*Thread t = new Thread(agent);
		t.start();*/
		try {
			System.out.println("Move initial");
			// Creation du socket agent
			Socket ConnectServer;
			ConnectServer = new Socket(this.agentServer.site().getHost(),this.port);
			// Recupération du Jar qui doit transiter avec l'agent
			Jar myjar = loader.getJar();
			// Preparation de l'envoie
			OutputStream os = ConnectServer.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			// Envoi du jar au serveur par serialization
			oos.writeObject(myjar);
			// Envoie de l'agent au serveur par serialization
			oos.writeObject(agent);
			// Fermeture
			os.close();
			oos.close();
			ConnectServer.close();
			System.out.println("Fin move initial");
		} catch (NoSuchElementException | IOException e) {
			System.out.println("Le serveur n'est pas joignable, on passe au suivant \n");
		}
	}
}
