package ssmith.util;

import java.util.ArrayList;

public class FIFOList extends ArrayList {
	
	private static final long serialVersionUID = 1L;
	
	private int max;
	
	public FIFOList(int mx) {
		super();
		this.max = mx;
	}
	
	public boolean add(Object o) {
		super.add(o);
		while (this.size() > max) {
			this.remove(0);
		}
		return true;
	}

}
