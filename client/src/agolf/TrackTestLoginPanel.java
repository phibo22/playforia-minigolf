package agolf;

import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;

class TrackTestLoginPanel extends Panel implements ActionListener, KeyListener {

    private GameApplet gameApplet;
    private int width;
    private int height;
    private TextField textFieldName;
    private Button buttonOk;
    private Label labelError;
    private Pattern pattern;
    private Label labelName;
    private Label labelName2;

    protected TrackTestLoginPanel(GameApplet gameApplet, int width, int height) {
        this.gameApplet = gameApplet;
        this.width = width;
        this.height = height;
        this.setSize(width, height);
        pattern = Pattern.compile("[^a-zA-Z0-9 ]");
        this.create();
    }

    public void addNotify() {
        super.addNotify();
        this.repaint();
    }

    public void paint(Graphics var1) {
        this.update(var1);
    }

    public void update(Graphics g) {
        g.setColor(GameApplet.colourGameBackground);
        g.fillRect(0, 0, this.width, this.height);
    }

    public void actionPerformed(ActionEvent evt) {
        String username = this.textFieldName.getText().trim();
        this.gameApplet.trackTestLogin(username, "");
    }

    public void keyPressed(KeyEvent evt) {
        if(evt.getKeyCode() == KeyEvent.VK_ENTER && buttonOk.isEnabled()) {
            actionPerformed(null);
            return;
        }
        boolean found = pattern.matcher(textFieldName.getText()).find() || textFieldName.getText().trim().equals("");
        if(found) {
            labelError.setVisible(true);
            buttonOk.setEnabled(false);
        } else {
            labelError.setVisible(false);
            buttonOk.setEnabled(true);
        }
    }

    private void create() {
        this.setLayout(null);
        this.textFieldName = new TextField("");//("(name)");
        this.textFieldName.setBounds(this.width / 2 - 75, this.height / 2 - 60, 150, 25);
        this.textFieldName.setBackground(Color.white);
        this.textFieldName.setForeground(Color.black);
        textFieldName.addKeyListener(this);
        this.add(this.textFieldName);
        textFieldName.requestFocus();
        this.buttonOk = new Button("OK");
        this.buttonOk.setBounds(this.width / 2 - 75, this.height / 2 + 50, 75, 25);
        this.buttonOk.addActionListener(this);
        this.add(this.buttonOk);
        labelError = new Label("Only spaces, alphabetical and numerical characters are allowed");
        labelError.setBounds(width / 2 - 75, height / 2 - 35, 400, 25);
        labelError.setForeground(Color.red);
        labelError.setVisible(false);
        add(labelError);
        labelName = new Label("Name:");
        labelName.setBounds(width / 2 - 150, height / 2 - 60, 75, 25);
        add(labelName);
        labelName2 = new Label("(optional)");
        labelName2.setBounds(width / 2 + 80, height / 2 - 60, 75, 25);
        labelName2.setForeground(Color.red);
        add(labelName2);
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}
