package control.logic;

public class LaszloLogic implements Logic {

	/**
	 * Ez fut le legelőször, ekkor kapod meg a galaxis jelenlegi állapotát
	 * 
	 * @param megkapod
	 *            a galaxist ArrayList<Planet>
	 */
	public void init() {

	}

	/**
	 * Lepakolja a csomago(ka)t a hajóról.
	 * 
	 * @param Az
	 *            űrhajón lévő package-ek :list
	 * @return a leszállítani kívánt csomagok id-jei
	 */
	public int[] drop() {
		return null;
	}

	/**
	 * Felveszi a bolygóról a csomagokat
	 * 
	 * @param Az
	 *            bolygón lévő package-ek :list
	 * @return a felvenni kívánt csomagok id-jei
	 */
	public int[] pick() {
		return null;
	}

	/**
	 * Elindul egy adott bolygóra
	 * 
	 * @return a célbolygó neve
	 */
	public String go() {
		// TODO Auto-generated method stub
		return null;
	}
}
