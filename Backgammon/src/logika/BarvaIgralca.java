package logika;

import java.util.EnumMap;
import java.util.Map;

public class BarvaIgralca {
	
	private static Map<Igralec, Figura> pridobiBarvo = new EnumMap<Igralec, Figura>(Igralec.class);;
	
	static {
		pridobiBarvo.put(Igralec.BELI, Figura.BELA);
		pridobiBarvo.put(Igralec.CRNI, Figura.CRNA);
	}
	
	public static Figura barva(Igralec igralec) {
		return pridobiBarvo.get(igralec);
	}
}
