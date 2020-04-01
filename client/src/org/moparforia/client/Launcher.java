package org.moparforia.client;

import agolf.AGolf;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Playforia
 * 28.5.2013
 */
public class Launcher extends JFrame {

    private final int GAME_WIDTH = 735;
    private final int GAME_HEIGHT = 525;
    private final int WINDOW_WIDTH = GAME_WIDTH + 20;
    private final int WINDOW_HEIGHT = GAME_HEIGHT + 40;
    private final int PORT = 4242;

    public static boolean debug() {
        return true;
    }

    public static boolean isUsingCustomServer() {
        return true;
    }

    public static void main(String[] args) {
        new Launcher(args.length > 0 ? args[0] : "127.0.0.1");
    }

    public Launcher(String server) {
        Applet game = new AGolf();
        game.setStub(new Stub(server));
        game.setSize(GAME_WIDTH, GAME_HEIGHT);
        game.init();
        game.start();
        add(game);

        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    class Stub implements AppletStub {
        private Map<String, String> params;

        public Stub(String server) {
            params = new HashMap<>();
            params.put("initmessage", "Loading game...");
            params.put("ld_page", "javascript:Playray.Notify.delegate({ jvm: { version: '%v', vendor: '%w', t1: '%r', t2: '%f' } })");
            params.put("image", "/appletloader_playforia.gif");
            params.put("host", server);
            params.put("server", server + ":" + PORT);
            params.put("locale", "en");
            params.put("lang", "en_US");
            params.put("sitename", "playray");
            params.put("quitpage", "http://www.playforia.com/games/");
            params.put("regremindshowtime", "3,8,15,25,50,100,1000");
            params.put("registerpage", "http://www.playforia.com/account/create/");
            params.put("creditpage", "http://www.playforia.com/shop/buy/");
            params.put("userinfopage", "http://www.playforia.com/community/user/");
            params.put("userinfotarget", "_blank");
            params.put("userlistpage", "javascript:Playray.GameFaceGallery('%n','#99FF99','agolf','%s')");
            params.put("guestautologin", "true");
            params.put("disableguestlobbychat", "true");
            params.put("json", "Playray.Notify.delegate(%o)");
            params.put("centerimage", "true");
            params.put("java_arguments", "-Xmx128m");
            params.put("localizationUrl", "");
            params.put("sharedLocalizationUrl", "");
        }

        public boolean isActive() {
            return true;
        }

        public URL getDocumentBase() {
            try {
                return new URL("http://" + getParameter("host") + "/AGolf/");
            } catch (Exception ex) {
                System.err.println("getdocumentbase exc eption");
                return null;
            }
        }

        public URL getCodeBase() {
            return getDocumentBase();
        }

        public String getParameter(String name) {
            if (!params.containsKey(name))
                return "";
            return params.get(name);
        }

        public AppletContext getAppletContext() {
            return null;
        }

        public void appletResize(int width, int height) {
        }
    }
}
