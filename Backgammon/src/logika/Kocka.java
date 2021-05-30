package logika;

import java.util.Random;

public class Kocka {
	
	public final int STEVILO_PLOSKEV = 6;
	
	private int vrednost;
	
	private Random rand;
	
	public Kocka() {
		rand = new Random();
		vrziKocko();
	}
	
	public void vrziKocko() {
		this.vrednost = rand.nextInt(STEVILO_PLOSKEV) + 1;
	}
	
	public int vrniVrednost() {
		return vrednost;
	}
}
