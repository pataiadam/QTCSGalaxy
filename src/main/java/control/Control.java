package control;

import control.api.RestAPI;

public class Control {

	private RestAPI api;

	public Control(RestAPI api) {
		this.api = api;
	}

	public String drop(int[] drops) throws InterruptedException {
		String ret = "";
		if (drops != null) {
			for (int i : drops) {
				if (i != 0) {
					ret+=i+" ";
					api.dropPackage(i);
				}
			}
		}
		return "Dropped: [ "+ret+"]";
	}

	public String pick(int[] picks) throws InterruptedException {
		String ret = "";
		if (picks != null) {
			for (int i : picks) {
				if (i != 0) {
					ret+=i+" ";
					api.pickPackage(i);
				}
			}
		}
		return "Picked: [ "+ret+"]";
	}

	public String go(String target) {
		String ret = "?";
		if (target != null) {
			ret=target;
			api.go(target);
		}
		return "Go to: [ "+ret+" ]";
	}

}
