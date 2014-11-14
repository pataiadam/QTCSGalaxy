package control;

import model.ModelStore;
import control.api.RestAPI;
import control.logic.AdamLogic;
import control.logic.Logic;

public class Main {

	public static void main(String[] args) {
		if (args.length == 3) {
			RestAPI galaxy = new RestAPI(args[0], args[1], args[2]);
			Logic logic = new AdamLogic();
			Control control = new Control(galaxy);
			ModelStore modelStrore = new ModelStore();

			while (true) {
				
				
				/** Ha egy helyben áll */
				try {
					Thread.sleep(600);
					modelStrore.refreshModel(galaxy);
					int ms=modelStrore.getSpaceShip().getArriveAfterMs();
					System.out.println("Úton még: "+ms+"ms");
					if (ms == -1) {
						logic.init(modelStrore);
						Thread.sleep(500);
						control.drop(logic.drop());
						Thread.sleep(500);
						control.pick(logic.pick());
						Thread.sleep(500);
						control.go(logic.go());
						Thread.sleep(500);
					} else if (modelStrore.getSpaceShip().getArriveAfterMs() >= 500) {
						Thread.sleep(500);
					}
				} catch (Exception e) {
					System.err.println("HOPPÁ");
				}
			}

		} else {
			System.out.println("You shall not pass!!! (QTCS)");
		}
		System.out.println("Client exited");
	}
}
