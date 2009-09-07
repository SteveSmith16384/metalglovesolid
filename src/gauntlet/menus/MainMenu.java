package gauntlet.menus;

import gauntlet.Main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import ssmith.awt.menu.AbstractMenu;
import ssmith.awt.menu.GLMenuItem;
import ssmith.awt.menu.GLMenuSelect;
import ssmith.awt.menu.GLMenuLabel;
import ssmith.image.ImageFunctions;

import java.awt.event.KeyEvent;

public final class MainMenu extends AbstractMenu {

	private static final long serialVersionUID = 1L;

    private GLMenuLabel instr = new GLMenuLabel("(Use arrow keys and Enter)");
    private GLMenuLabel blank = new GLMenuLabel("");
    private GLMenuSelect start_game = new GLMenuSelect("Start New Game");
    private GLMenuSelect controls = new GLMenuSelect("Controls");
    private GLMenuSelect exit = new GLMenuSelect("Quit");
    private Image background_img;
    
    protected Main main;

    public MainMenu(Main m, int x, int y) {
        super(x, y, Main.font_large, Color.CYAN, Color.YELLOW, Color.green.darker());
        main = m;
        this.addMenuItem(instr);
        this.addMenuItem(blank);
        this.addMenuItem(start_game);
        this.addMenuItem(controls);
        this.addMenuItem(exit);
        
        background_img = ImageFunctions.scaleImage(main.getImage("pics/dungeon.jpg"), main.getWidth(), main.getHeight(), main);
    }

    public void draw(Graphics g) {
    	g.drawImage(background_img, 0, 0, main);
    	g.drawImage(main.logo, 40, 40, main);
        super.draw(g);
    }

    protected void menuItemSelected(GLMenuItem menu_item) {
        if (menu_item == start_game) {
        	main.selectCharacter();
        } else if (menu_item == controls) {
            	main.showControls();
        } else if (menu_item == exit) {
            main.exit();
        }
    }

    public void receivedChar(int e) {
        if (e == KeyEvent.VK_ESCAPE) {
            main.exit();
        }
    }

}
