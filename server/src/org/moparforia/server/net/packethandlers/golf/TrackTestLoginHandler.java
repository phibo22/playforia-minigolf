package org.moparforia.server.net.packethandlers.golf;

import org.moparforia.server.Server;
import org.moparforia.server.db.Database;
import org.moparforia.server.game.Player;
import org.moparforia.server.net.Packet;
import org.moparforia.server.net.PacketHandler;
import org.moparforia.server.net.PacketType;
import org.moparforia.shared.Tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Playforia
 * 13.6.2013
 */
public class TrackTestLoginHandler implements PacketHandler {
    Pattern namePattern;

    public TrackTestLoginHandler() {
        namePattern = Pattern.compile("[^ -~]");
    }

    @Override
    public PacketType getType() {
        return PacketType.DATA;
    }

    @Override
    public Pattern getPattern() {
        return Pattern.compile("ttlogin\\t(.*)\\t(.*)");
    }

    @Override
    public boolean handle(Server server, Packet packet, Matcher message) {
        String username = message.group(1);
        username = username.trim();
        if (namePattern.matcher(username).find()) {
            return false;//todo disconnect client
        }
        String password = message.group(2);
        //todo load player from db?

        if ("".equals(username)) {
            username = "~anonym-" + (int) (Math.random() * 10000);
        }

        Player player = (Player) packet.getChannel().getAttachment();
        player.setNick(username);
        player.setEmailVerified(true);
        player.setRegistered(false);
        packet.getChannel().write(new Packet(PacketType.DATA, Tools.tabularize("basicinfo", player.isEmailVerified(), player.getAccessLevel(), "t", "f")));
        packet.getChannel().write(new Packet(PacketType.DATA, Tools.tabularize("status", "lobbyselect", 300)));
        return true;
    }
}
