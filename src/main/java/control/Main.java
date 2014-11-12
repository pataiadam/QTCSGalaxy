package control;

import model.ModelStore;
import control.api.RestAPI;
import control.logic.AdamLogic;
import control.logic.Logic;

public class Main {

	public static void main(String[] args){
		if(args.length==3){
			RestAPI galaxy = new RestAPI(args[0], args[1], args[2]);
			galaxy.ping();
			Control control = new Control();
			ModelStore modelStrore = new ModelStore();
			
			modelStrore.refreshModel(galaxy);
		/*	
		 * Amíg van csomag és időnk:
		 * 		ha bolygón vagyunk:
		 * 			control.init(logic.init(minden)) ->megkapja a bolygókat
		 * 			control.drop(logic.drop(<űrhajó csomagjai>)) ->ledobunk csomagot
		 * 			control.pick(logic.pick(<bolygó csomagjai>)) ->felveszünk csomagot
		 * 			control.go(logic.go(<galaxis>)) -> elindulunk máshova
		 * 
		 * */
			
		}else{
			System.out.println("You shall not pass!!! (QTCS)");
		}
		System.out.println("Client exited");
	}

}
