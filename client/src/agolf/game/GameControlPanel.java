package agolf.game;

import agolf.GameApplet;
import agolf.GameContainer;
import com.aapeli.colorgui.Choicer;
import com.aapeli.colorgui.ColorButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

class GameControlPanel extends Panel implements ActionListener, ItemListener {

    private GameContainer gameContainer;
    private GamePlayerInfoPanel aGamePlayerInfoPanel__341;
    private int anInt342;
    private int anInt343;
    private int playerCount;
    private ColorButton buttonSkip;
    private ColorButton buttonNewGame;
    private ColorButton buttonBack;
    private Choicer choicerNames;
    private Checkbox checkboxMaxFps;
    private boolean skipButtonVisible;
    private static final String[] aStringArray351 = new String[5];


    protected GameControlPanel(GameContainer var1, GamePlayerInfoPanel var2, int var3, int var4) {
        this.gameContainer = var1;
        this.aGamePlayerInfoPanel__341 = var2;
        this.anInt342 = var3;
        this.anInt343 = var4;
        this.setSize(var3, var4);
        this.setLayout(null);
        this.skipButtonVisible = false;
    }

    public void addNotify() {
        super.addNotify();
        this.repaint();
    }

    public void paint(Graphics var1) {
        this.update(var1);
    }

    public void update(Graphics var1) {
        var1.setColor(GameApplet.colourGameBackground);
        var1.fillRect(0, 0, this.anInt342, this.anInt343);
    }

    public void actionPerformed(ActionEvent var1) {
        Object var2 = var1.getSource();
        if (var2 == this.buttonSkip) {
            if (this.gameContainer.gamePanel.method339(this.playerCount == 1)) {
                this.setVisible(false);
                this.remove(this.buttonSkip);
                this.setVisible(true);
                this.skipButtonVisible = false;
            }

        } else if (var2 == this.buttonNewGame) {
            this.buttonNewGame.removeActionListener(this);
            this.setVisible(false);
            this.remove(this.buttonNewGame);
            this.setVisible(true);
            this.aGamePlayerInfoPanel__341.readyForNewGameLocal();
            this.gameContainer.gamePanel.requestNewGame();
        } else {
            if (var2 == this.buttonBack) {
                this.buttonBack.removeActionListener(this);
                this.setVisible(false);
                this.remove(this.buttonBack);
                this.setVisible(true);
                this.gameContainer.gamePanel.method342();
            }

        }
    }

    public void itemStateChanged(ItemEvent var1) {
        if (var1.getSource() == this.choicerNames) {
            this.gameContainer.gamePanel.method348(this.choicerNames.getSelectedIndex());
        }

    }

    protected void setPlayerCount(int var1) {
        this.playerCount = var1;
    }

    protected void displaySkipButton() {
        this.setVisible(false);
        this.remove(this.buttonSkip);
        this.skipButtonVisible = false;
        if (this.playerCount > 1 || this.gameContainer.synchronizedTrackTestMode.get()) {
            this.add(this.buttonSkip);
            this.skipButtonVisible = true;
        }

        this.setVisible(true);
    }

    protected void method327(int var1) {
        this.setVisible(false);
        this.removeAll();
        this.skipButtonVisible = false;
        if (var1 == 2) {
            this.buttonNewGame = new ColorButton(this.gameContainer.textManager.getGame("GameControl_New"));
            this.buttonNewGame.setBounds(0, this.anInt343 - 55, this.anInt342, 25);
            this.buttonNewGame.setBackground(GameApplet.colourButtonGreen);
            this.buttonNewGame.setForeground(GameApplet.colourTextBlack);
            this.buttonNewGame.addActionListener(this);
            this.add(this.buttonNewGame);
        } else {
            this.buttonSkip = new ColorButton(this.gameContainer.textManager.getGame("GameControl_Skip"));
            this.buttonSkip.setBounds(0, this.anInt343 - 55, this.anInt342, 25);
            this.buttonSkip.setBackground(GameApplet.colourButtonBlue);
            this.buttonSkip.setForeground(GameApplet.colourTextBlack);
            this.buttonSkip.addActionListener(this);
            if (this.gameContainer.synchronizedTrackTestMode.get()) {
                this.checkboxMaxFps = new Checkbox("Max FPS", false);
                this.checkboxMaxFps.setBounds(0, this.anInt343 - 80, this.anInt342, 20);
                this.checkboxMaxFps.setBackground(GameApplet.colourGameBackground);
                this.checkboxMaxFps.setForeground(GameApplet.colourTextBlack);
                this.add(this.checkboxMaxFps);
            }

            if (this.playerCount > 1) {
                this.choicerNames = new Choicer();

                for (int var2 = 0; var2 < 4; ++var2) {
                    this.choicerNames.addItem(this.gameContainer.textManager.getGame("GameControl_Names" + var2));
                }

                this.choicerNames.select(this.playerCount <= 2 ? 0 : 3);
                this.choicerNames.setBounds(0, this.anInt343 - 80, this.anInt342, 20);
                this.choicerNames.addItemListener(this);
                this.add(this.choicerNames);
            }
        }

        this.buttonBack = new ColorButton(this.gameContainer.textManager.getGame("GameControl_Back"));
        this.buttonBack.setBounds(0, this.anInt343 - 25, this.anInt342, 25);
        this.buttonBack.setBackground(GameApplet.colourButtonYellow);
        this.buttonBack.setForeground(GameApplet.colourTextBlack);
        this.buttonBack.addActionListener(this);
        this.add(this.buttonBack);
        this.setVisible(true);
    }

    protected boolean maxFps() {
        return this.checkboxMaxFps.getState();
    }

    protected void method329() {
        this.buttonNewGame.removeActionListener(this);
        this.remove(this.buttonNewGame);
    }

    protected void method330() {
        if (this.playerCount != 1) {
            this.setVisible(false);
            this.remove(this.buttonBack);
            this.add(this.buttonBack);
            this.setVisible(true);
        }
    }

    protected void hideSkipButton() {
        if (this.skipButtonVisible) {
            this.setVisible(false);
            this.remove(this.buttonSkip);
            this.setVisible(true);
            this.skipButtonVisible = false;
        }
    }

    protected void showSkipButton() {
        if (!this.skipButtonVisible) {
            this.setVisible(false);
            this.add(this.buttonSkip);
            this.setVisible(true);
            this.skipButtonVisible = true;
        }
    }

    static {
        aStringArray351[0] = "GameControl_Names";
        aStringArray351[1] = "GameControl_Skip";
        aStringArray351[2] = "Max FPS";
        aStringArray351[3] = "GameControl_New";
        aStringArray351[4] = "GameControl_Back";
    }
}
