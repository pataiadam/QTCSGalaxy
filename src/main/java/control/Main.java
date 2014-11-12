package control;
import control.api.ImpRestAPI;




public class Main {

	public static void main(String[] args){
		ImpRestAPI galaxy = new ImpRestAPI(args[0], args[1], args[2]);
		System.out.println("Client exited");
	}

}
