package org.moparforia.server.event;

import org.jboss.netty.channel.Channel;
import org.moparforia.server.Server;

import java.util.Random;

public class ClientConnectedEvent extends Event {

    private final Channel channel;

    public ClientConnectedEvent(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void process(Server server) {
        System.out.println("Client connected: " + channel);
        channel.write("h 1\nc io " + new Random().nextInt(1000000000) + "\nc crt 25\nc ctr\n");
    }

}
