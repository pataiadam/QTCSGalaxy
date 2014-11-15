package control;

import model.ModelStore;
import control.api.RestAPI;
import control.logic.Logic;
import control.logic.MohoLogic;

public class Main {

	public static void main(String[] args) {
		if (args.length == 3) {
			RestAPI galaxy = new RestAPI(args[0], args[1], args[2]);
			Logic logic = new MohoLogic();
			Control control = new Control(galaxy);
			ModelStore modelStrore = new ModelStore();
			
			while (true) {
				try {
					modelStrore.refreshModel(galaxy);
					int ms=modelStrore.getSpaceShip().getArriveAfterMs();
					System.out.println("Úton még: "+ms+"ms");
					if (ms == -1) {
						logic.init(modelStrore);
						System.out.println(control.drop(logic.drop()));
						System.out.println(control.pick(logic.pick()));
						System.out.println(control.go(logic.go()));
					} else if (modelStrore.getSpaceShip().getArriveAfterMs() >= 500) {
						//ez csak azért, hogy ne írjon annyit a konzolra
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
