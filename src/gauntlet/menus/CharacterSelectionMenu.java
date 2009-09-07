package gauntlet.menus;

import gauntlet.Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import ssmith.awt.menu.AbstractMenu;
import ssmith.awt.menu.GLMenuItem;
import ssmith.awt.menu.GLMenuLabel;
import ssmith.awt.menu.GLMenuSelect;
import ssmith.image.ImageFunctions;

public class CharacterSelectionMenu extends AbstractMenu {

	private static final long serialVersionUID = 1L;

    private GLMenuLabel instr = new GLMenuLabel("(Use arrow keys and Enter)");
    private GLMenuLabel blank = new GLMenuLabel("");
    private GLMenuSelect warrior = new GLMenuSelect("Warrior");
    private GLMenuSelect wizard = new GLMenuSelect("Wizard");
    private GLMenuSelect elf = new GLMenuSelect("Elf");
    private GLMenuSelect dwarf = new GLMenuSelect("Dwarf");
    private GLMenuSelect exit = new GLMenuSelect("Quit");
    private Image background_img;
    
    protected Main main;

    public CharacterSelectionMenu(Main m, int x, int y) {
        super(x, y, Main.font_large, Color.CYAN, Color.YELLOW, Color.green.darker());
        main = m;
        this.addMenuItem(instr);
        this.addMenuItem(blank);
        this.addMenuItem(warrior);
        this.addMenuItem(wizard);
        this.addMenuItem(elf);
        this.addMenuItem(dwarf);
        this.addMenuItem(exit);
        
        background_img = ImageFunctions.scaleImage(main.getImage("pics/dungeon.jpg"), main.getWidth(), main.getHeight(), main);
    }

    public void draw(Graphics g) {
    	g.drawImage(background_img, 0, 0, main);
    	g.drawImage(main.logo, 40, 40, main);
        super.draw(g);
    }

    protected void menuItemSelected(GLMenuItem menu_item) {
        if (menu_item != exit) {
        	main.characterSelected(menu_item.menu_text);
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
