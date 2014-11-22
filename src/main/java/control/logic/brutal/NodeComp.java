package control.logic.brutal;

import java.util.Comparator;

public class NodeComp implements Comparator<Node> {

	public int compare(Node o1, Node o2) {
		if(o1.h==o2.h){
    		return 0;
    	}
		return o1.h<o2.h?1:-1;
	}

}
