package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BAMAgentClassLoader extends ClassLoader {
	
	private Jar myjar;
	HashMap<String, byte[]> MapClass = new HashMap<String, byte[]>();
	
	
	public BAMAgentClassLoader(URL[] urls, ClassLoader classLoader) {
		super(classLoader);
		MapClass = new HashMap<String, byte[]>();
	}


	public Jar getJar() {
		return myjar;
	}


	public void addClass(Jar jar) {
		this.myjar = jar;
		// Parcours de l'objet Iterable Jar (Jar implements Iterable<Map.Entry<String,byte[]>>)
		for(HashMap.Entry<String, byte[]> elem : myjar.classIterator()){ 
			// Ajoute les éléments du Jar dans notre HashMap de class (Les éléments du Jar sont contenu dans une HashMap)
			MapClass.put(elem.getKey(), elem.getValue());
		}
	}


	public void addURL(URL url) {
		// Creation d'un objet Jar à partir de l'url
		Jar jar = null;
		try {
			jar = new Jar(url.getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		addClass(jar);
	}


	public void addURL(String codeBase) {
		// Creation d'un objet Jar à partir du string
		Jar jar = null;
		try {
			jar = new Jar(codeBase);
		} catch (IOException e) {
			e.printStackTrace();
		}
		addClass(jar);
		
	}
	
	
		
}
	
	
