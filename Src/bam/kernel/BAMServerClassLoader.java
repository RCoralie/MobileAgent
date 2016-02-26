package bam.kernel;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class BAMServerClassLoader extends URLClassLoader {
	
	private Jar myjar;
	private HashMap<String, byte[]> MapClass;
		
	public BAMServerClassLoader(URL[] urls, ClassLoader classLoader) {
		super(urls,classLoader);
		MapClass = new HashMap<String, byte[]>();
	}
	
	public void addURL(URL url){
		// Creation d'un objet Jar à partir de l'url
		Jar jar = null;
		try {
			jar = new Jar(url.getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.myjar = jar;
		// Parcours de l'objet Iterable Jar (Jar implements Iterable<Map.Entry<String,byte[]>>)
		for(Map.Entry<String, byte[]> elem : myjar.classIterator()){ 
			// Ajoute les éléments du Jar dans notre HashMap de class (Les éléments du Jar sont contenu dans une HashMap)
			MapClass.put(elem.getKey(), elem.getValue());
		}
	}
	

	
	
	public String toString(){
		StringBuffer buf = new StringBuffer();
		// Parcours de la HashMap contenant les classes
		for(HashMap.Entry<String,byte[]> elem : MapClass.entrySet()) {
			buf.append(elem.getKey()); 
			buf.append("\n");
		}
		return "ServerClassLoader[\n"+buf.toString()+"]";
		
	}


}
