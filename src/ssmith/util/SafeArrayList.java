package ssmith.util;

import java.util.ArrayList;

public class SafeArrayList extends ArrayList {
	
	private static final long serialVersionUID = 1L;
	
	private ArrayList new_objects, del_objects;
	
	public SafeArrayList() {
		super();
		new_objects = new ArrayList();
		del_objects = new ArrayList();
	}
	
	public void sync() {
		this.removeAll(del_objects);
		this.addAll(new_objects);
	}
	
	public boolean add(Object o) {
		this.new_objects.add(o);
		return true;
	}

	public boolean remove(Object o) {
		this.del_objects.add(o);
		return true;
	}

}
