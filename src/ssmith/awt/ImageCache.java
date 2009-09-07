package ssmith.awt;

import java.awt.*;
import java.io.*;
import java.util.*;

public class ImageCache extends Hashtable {

	private static final long serialVersionUID = 1L;

	private static Toolkit tk = Toolkit.getDefaultToolkit();
//	private Hashtable hashImages = new Hashtable();
	private MediaTracker mt;

	public ImageCache() {
		// For no mediatracker.
	}

	public ImageCache(Component c) {
		mt = new MediaTracker(c);
	}

	public Image getImage(String filename, boolean wait) {
		Image img = (Image) get(filename);
		if (img == null) {
			//System.out.println("Loading image "+filename);
			if (new File(filename).canRead() == false) {
				System.err.println("Cannot find " + filename);
				return null;
			}
			img = tk.getImage(filename);
			if (mt != null && wait == true) {
				mt.addImage(img, 1);
				try {
					mt.waitForID(1);
					//System.out.println("Waiting for "+filename);
				}
				catch (InterruptedException e) {
					System.err.println("Error loading images: " + e.getMessage());
				}
				mt.removeImage(img);
			}
			put(filename, img);
		}
		return img;
	}

	public Image getImage(String filename) {
		return this.getImage(filename, true);
	}

}

