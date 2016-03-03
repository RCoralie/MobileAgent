/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Définit la feuille de route que l'agent va suivre
 * @author  Morat
 */
class Route implements Iterable<Etape>, Serializable{
	
	private static final long serialVersionUID = 9081294824590167316L;
	
	/** la liste des étapes à parcourir autres que la dernière */
	protected List<Etape> route;
	
	/** la dernière étape de la feuille de route de l'agent qui désigne le serveur de départ. */
	protected Etape retour;
	
	/** Indique si la feuille de route est épuisée ou non. */
	protected boolean hasNext;

	
	/**
	 * Construction d'une route.
	 * @param retour : le server initial et le server final
	 */
	public Route(Etape retour) {
		route = new LinkedList<Etape>();
		this.retour = retour;
		hasNext=true;
		route.add(route.size(),retour);
		System.out.println("Construction de la feuille de route");
		System.out.println("Taille de la feuille de route : " + route.size());
	}
	
	
	/**
	 * Ajoute une étape en fin de route.
	 * @param e l'étape à ajouter
	 */
	public void add(Etape e) { 
		route.add(route.size(),e);
		System.out.println("Ajout d'une étape :  " + e.getEtape());
		System.out.println("Taille de la feuille de route : " + route.size());
	}

	
	/**
	 * Restitue la prochaine étape ou la dernière qui est la base de départ.
	 * @return la prochaine étape.
	 */
	Etape get() throws NoSuchElementException {
		//A COMPLETER :
		if(hasNext()) { // si il y a des éléments dans la liste on retourne le premier (= prochaine étape)
			return route.get(0);
		}
		else{ // s'il n'y a plus d'élément dans la liste, on retourne retour (= dernière étape)
			return retour;
		}
	}

	
	/**
	 * Restitue la prochaine étape et élimine de la route ou la dernière qui est la base de départ.
	 * @return la prochaine étape.
	 */
	Etape next() throws NoSuchElementException {
		// A COMPLETER
		Etape next = route.get(0); // on recupère le premier élément de la liste (= prochaine étape)
		route.remove(0); // on supprime le premier élément de la liste (puisque l'on va effetuer l'étape)
		if(route.size()==0){
			hasNext = false;
		}
		return next;
	}
	
	
	/**
	 * Il y a-t-il encore une étape à parcourir.
	 * @return vrai si une étape est possible.
	 */
	public boolean hasNext() { 
		return hasNext;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Etape> iterator(){return route.iterator();}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return route.toString().replaceAll(", ","->");
	}
}
