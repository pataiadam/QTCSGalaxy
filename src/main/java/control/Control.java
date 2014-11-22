package control;

import control.api.RestAPI;

public class Control {

	private RestAPI api;

	public Control(RestAPI api) {
		this.api = api;
	}

	public int[] drop(int[] drops) throws InterruptedException {
		String ret = "";
		int[] dropped = new int[3];
		int k=0;
		if (drops != null) {
			for (int i : drops) {
				if (i != 0) {
					ret += i + " ";
					String response=api.dropPackage(i);
					if(response.contains("PACKAGE_DROPPED")){
						dropped[k]=i;
						k++;
					}
				}
			}
		}
		if (Main.DEBUG)
			System.out.println("Dropped: [ " + ret + "]");
		return dropped;
	}

	public int[] pick(int[] picks) throws InterruptedException {
		String ret = "";
		if (picks != null) {
			for (int i : picks) {
				if (i != 0) {
					ret += i + " ";
					api.pickPackage(i);
				}
			}
		}
		if (Main.DEBUG)
			System.out.println("Picked: [ " + ret + "]");
		return picks;
	}

	public String go(String target) {
		String ret = "?";
		if (target != null) {
			ret = target;
			api.go(target);
		}
		if (Main.DEBUG)
			System.out.println("Go to: [ " + ret + " ]");
		return ret;
	}

}
