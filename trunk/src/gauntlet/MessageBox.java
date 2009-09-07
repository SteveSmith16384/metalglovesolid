package gauntlet;

import java.awt.Color;
import java.awt.Graphics;

public class MessageBox {

	private static final int DURATION = 2000;

	private String text = "";
	private long show_until;

	public MessageBox() {
		super();
	}

	public void setText(String _text) {
		text = _text;
		show_until = System.currentTimeMillis() + DURATION;
	}

	public void paint(Graphics g, int x, int y) {
		if (text.length() > 0) {
			g.setFont(Main.font_normal);
			g.setColor(Color.yellow);
			g.drawString(text, x, y);
			
			if (System.currentTimeMillis() > show_until) {
				this.text = "";
			}
		}
	}
}
