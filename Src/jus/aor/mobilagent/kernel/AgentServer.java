package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;


/** Serveur recevant les agents mobiles avec leur contexte pour les exécuter sur le serveur **/

public class AgentServer extends Thread { // Runnable ou Thread ????

	static int port=10140; // un serveur fonctionne sur une machine bien définie et est lié à un numéro de port spécifique
	static ServerSocket ListenServer; // socket utilisé pour écouter et accepter les connexions des clients
	private String serverName;
	private HashMap<String,_Service<?>> serviceMap;
	private boolean alive = true;
	
	
	public AgentServer(int port, String name){
		this.port = port;
		this.serverName = name;
		serviceMap = new HashMap<String,_Service<?>>();
	}
	
	
	/* Accepte et exécute les agents mobiles qui arrivent sur le serveur*/
	public void run() {
		try{
			// Le serveur créer un socket pour écouter et accepter les connexions des clients 
			ListenServer = new ServerSocket(port);
			System.out.println("Le serveur " + serverName + " est lancé.\n");
			// Le serveur se met en attente qu'un agent mobile arrive pour l'éxecuter
			while(alive) {
				Socket ConnectClient = ListenServer.accept();
				System.out.println("L'agent mobile" + ConnectClient.getInetAddress() + " est connecté.\n");
				// On prépare le lecture
				InputStream is = ConnectClient.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
				// On récupère le Jar et crée un BAMAgentClassLoader
				Jar jar = (Jar) ois.readObject();
				BAMAgentClassLoader bamAgent = new BAMAgentClassLoader(new URL[]{}, this.getClass().getClassLoader());
				bamAgent.addClass(jar);
				// On récupère l'agent mobile et on le réinitialise sur ce serveur
				_Agent agent = (_Agent) ois.readObject();
				System.out.println("Run agentServer");
				agent.reInit(bamAgent, this, this.serverName);
				// On execute l'agent comme un nouveau thread
				Thread t = new Thread(agent);
				t.start();
				// Fermeture
				ois.close();
				ConnectClient.close();
			}
			ListenServer.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public String toString(){
		return serverName;
	}
	
	public void addService (String name,_Service<?> service) {
		this.serviceMap.put(name,service);
	}
	
	public _Service<?> getService(String name) {
		return serviceMap.get(name);
	}

	public URI site() {
		URI uri=null;
		try {
			uri= new URI("//localhost:"+port+"/");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return uri;	
	}

	
}
