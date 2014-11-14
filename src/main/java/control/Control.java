package control;

import control.api.RestAPI;

public class Control {

	private RestAPI api;

	public Control(RestAPI api) {
		this.api = api;
	}

	public void drop(int[] drops) {
		if (drops != null) {
			for (int i : drops)
				api.dropPackage(i);
		}
	}

	public void pick(int[] picks) {
		if (picks != null) {
			for (int i : picks)
				api.pickPackage(i);
		}
	}

	public void go(String target) {
		if (target != null) {
			api.go(target);
		}
	}

}
