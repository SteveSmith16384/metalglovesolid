package gauntlet.menus;

import gauntlet.Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import ssmith.awt.menu.AbstractMenu;
import ssmith.awt.menu.GLMenuItem;
import ssmith.awt.menu.GLMenuLabel;
import ssmith.awt.menu.GLMenuSelect;
import ssmith.image.ImageFunctions;

public class ControlsMenu extends AbstractMenu {

	private static final long serialVersionUID = 1L;

    private GLMenuLabel keys1 = new GLMenuLabel("Use Arrow Keys to move");
    private GLMenuLabel keys2 = new GLMenuLabel("Ctrl to Shoot");
    private GLMenuLabel keys3 = new GLMenuLabel("A - Show Attributes");
    private GLMenuLabel keys4 = new GLMenuLabel("S - Show Spellbook");
    private GLMenuLabel keys5 = new GLMenuLabel("0..9 - Cast Spell");
    private GLMenuLabel keys6 = new GLMenuLabel("R - Restart (when dead)");
    private GLMenuLabel keys7 = new GLMenuLabel("P - Pause");
    private GLMenuSelect back = new GLMenuSelect("Back");
    private Image background_img;

    protected Main main;

    public ControlsMenu(Main m, int x, int y) {
        super(x, y, Main.font_large, Color.CYAN, Color.YELLOW, Color.green.darker());
        main = m;
        this.addMenuItem(keys1);
        this.addMenuItem(keys2);
        this.addMenuItem(keys3);
        this.addMenuItem(keys4);
        this.addMenuItem(keys5);
        this.addMenuItem(keys6);
        this.addMenuItem(keys7);
        this.addMenuItem(back);

        background_img = ImageFunctions.scaleImage(main.getImage("pics/dungeon.jpg"), main.getWidth(), main.getHeight(), main);
    }

    public void draw(Graphics g) {
    	g.drawImage(background_img, 0, 0, main);
    	g.drawImage(main.logo, 40, 40, main);
        super.draw(g);
    }

    protected void menuItemSelected(GLMenuItem menu_item) {
		if (menu_item == back) {
			main.showMainMenu();
		}
	}

}
