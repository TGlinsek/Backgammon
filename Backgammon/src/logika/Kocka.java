package logika;

import java.util.Random;

public class Kocka {
	
	public final int STEVILO_PLOSKEV = 6;
	
	private int vrednost;
	
	private Random rand;
	
	public Kocka() {
		rand = new Random();
		this.vrednost = vrziKocko();
	}
	
	public int vrziKocko() {
		return rand.nextInt(STEVILO_PLOSKEV) + 1;
	}
	
	public int vrniVrednost() {
		return vrednost;
	}
}
