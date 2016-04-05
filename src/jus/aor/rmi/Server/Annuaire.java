package jus.aor.rmi.Server;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jus.aor.rmi.Common.Numero;
import jus.aor.rmi.Common._Annuaire;


public class Annuaire extends UnicastRemoteObject implements _Annuaire {

	private static final long serialVersionUID = 1L;
	private HashMap<String,Numero> annuaire = new HashMap<String, Numero>();
	
	
	/**
	 * Définit un annuaire téléphonique élémentaire associant le nom d'abonné avec son numéro de téléphone, à partir du fichier XML
	 * @param chemin vers fichier XML 
	 * @return annuaire téléphonique
	 * @throws RemoteException 
	 */	
	protected Annuaire(String path) throws RemoteException {	
		
		System.out.println(">Initialisation de l'annuaire...");
		
		/* Récupération de l'annuaire dans le fichier xml */
		DocumentBuilder docBuilder = null;
		Document doc=null;
		try {
			docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = docBuilder.parse(new File(path));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		String name, numero;
		NodeList list = doc.getElementsByTagName("Telephone");
		NamedNodeMap attrs;
		
		/* acquisition de toutes les entrées de l'annuaire */
		for(int i =0; i<list.getLength();i++) {
			attrs = list.item(i).getAttributes();
			name=attrs.getNamedItem("name").getNodeValue();
			numero=attrs.getNamedItem("numero").getNodeValue();
			annuaire.put(name, new Numero(numero));
		}
	}

	
	/**
	 * Restitue le numéro de téléphone de l'abonné
	 * @param le nom de l'abonné
	 * @return le numéro de télephone de l'abonné
	 * @throws RemoteException 
	 */
	public Numero get(String abonne) throws RemoteException {
		return annuaire.get(abonne);
	}

}
