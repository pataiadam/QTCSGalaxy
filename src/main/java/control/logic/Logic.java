package control.logic;

import model.ModelStore;



public interface Logic {
	
	public void init(ModelStore modelStrore);
	public int[] drop();
	public int[] pick();
	public String go();
	
}
