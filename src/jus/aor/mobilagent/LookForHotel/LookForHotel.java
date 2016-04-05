package jus.aor.mobilagent.LookForHotel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import jus.aor.mobilagent.kernel.Agent;
import jus.aor.mobilagent.kernel.Starter;
import jus.aor.mobilagent.kernel._Action;
import jus.aor.mobilagent.kernel._Service;
import jus.aor.rmi.Common.Hotel;
import jus.aor.rmi.Common.Numero;

public class LookForHotel extends Agent {

	private static final long serialVersionUID = 1L;

	private String localisation;
	private _Annuaire annuaire;
	private List<_Chaine> listChaines = new ArrayList<_Chaine>();
	private List<Hotel> listHotels = new ArrayList<Hotel>();
	private HashMap<Hotel, Numero> listNumeros = new HashMap<Hotel, Numero>();

	protected _Action findHotels = new _Action() {
		private static final long serialVersionUID = 1L;

		@Override
		public void execute() {
			//Starter.getLogger().log(Level.INFO, "Searching for hotels");
			System.out.println("Searching for hotels");
			_Service<?> _service = LookForHotel.this.server.getService("Hotels");
			@SuppressWarnings("unchecked")
			//Collection<Hotel> _hotel = (Collection<Hotel>) _service.call(new Object[] { LookForHotel.this.localisation });
			Collection<Hotel> _hotel = (Collection<Hotel>) _service.call(localisation);
			LookForHotel.this.listHotels.addAll(_hotel);
			//Starter.getLogger().log(Level.INFO, _hotel.size() + " hotels found in " + LookForHotel.this.localisation);
		}
	};

	protected _Action findNumbers = new _Action() {
		private static final long serialVersionUID = 1L;

		@Override
		public void execute() {
			//Starter.getLogger().log(Level.INFO, "Searching for hotels' number");
			_Service<?> _service = LookForHotel.this.server.getService("Numbers");
			for (Hotel h : LookForHotel.this.listHotels) {
				Numero num = (Numero) _service.call(new Object[] { h.name });
				LookForHotel.this.listNumeros.put(h, num);
			}
		}
	};

	public LookForHotel(Object... args) {
		this.localisation = (String) args[0];
		this.listHotels = new ArrayList<>();
		this.listNumeros = new HashMap<>();
	}

}