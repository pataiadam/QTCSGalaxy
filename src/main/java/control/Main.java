package control;

import control.api.RestAPI;
import control.logic.AdamLogic;
import control.logic.Logic;




public class Main {

	public static void main(String[] args){
		if(args.length==3){
			RestAPI galaxy = new RestAPI(args[0], args[1], args[2]);
			Logic l = new AdamLogic();
			galaxy.ping();
		}else{
			System.out.println("You shall not pass!!! (QTCS)");
		}
		System.out.println("Client exited");
	}

}
