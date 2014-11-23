package control;

import model.ModelStore;
import control.api.RestAPI;
import control.logic.BrutalMohoLogic;
import control.logic.LaszloLogic;
import control.logic.Logic;

public class Main {

	public static boolean DEBUG = true;

	public static void main(String[] args) {
		if (args.length == 3) {
			RestAPI galaxy = new RestAPI(args[0], args[1], args[2]);
			Logic logic = new BrutalMohoLogic();
			Control control = new Control(galaxy);
			ModelStore modelStrore = new ModelStore();
			int[] dropped = null;
			while (true) {
				try {
					modelStrore.refreshModel(galaxy, dropped);
					long ms = (long)modelStrore.getSpaceShip().getArriveAfterMs();
					if (DEBUG) {
						System.out.println("Irány a "
								+ modelStrore.getSpaceShip()
										.getTargetPlanetName() + ", érkezés: "
								+ ms + "ms múlva");
						System.out.println("Csomagok száma: "
								+ modelStrore.getSpaceShip().getPackages()
										.size());
					}
					if (ms == -1) {
						logic.init(modelStrore);
						dropped = control.drop(logic.drop());
						control.pick(logic.pick());
						control.go(logic.go());
					} else {
						Thread.sleep(ms);
					}
				} catch (Exception e) {
					if (DEBUG) {
						System.err.println("HOPPÁ");
						e.printStackTrace();
					}
				}
			}

		} else {
			System.out.println("You shall not pass!!! (QTCS)");
		}
		System.out.println("Client exited");
	}
}
