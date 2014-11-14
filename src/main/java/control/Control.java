package control;

import control.api.RestAPI;

public class Control {

	private RestAPI api;

	public Control(RestAPI api) {
		this.api = api;
	}

	public void drop(int[] drops) throws InterruptedException {
		if (drops != null) {
			for (int i : drops) {
				api.dropPackage(i);
				Thread.sleep(500);
			}
		}
	}

	public void pick(int[] picks) throws InterruptedException {
		if (picks != null) {
			for (int i : picks) {
				api.pickPackage(i);
				Thread.sleep(500);
			}
		}
	}

	public void go(String target) {
		if (target != null) {
			api.go(target);
		}
	}

}
