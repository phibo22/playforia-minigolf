package org.moparforia.server;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.HashedWheelTimer;
import org.moparforia.server.event.Event;
import org.moparforia.server.game.Lobby;
import org.moparforia.server.game.LobbyType;
import org.moparforia.server.game.Player;
import org.moparforia.server.net.*;
import org.moparforia.server.track.TrackManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server implements Runnable {

    public static void main(String[] args) {
        new Server().start();
    }

    private HashMap<Integer, Player> players = new HashMap<Integer, Player>();
    private ConcurrentLinkedQueue<Event> events = new ConcurrentLinkedQueue<Event>();
    private HashMap<PacketType, ArrayList<PacketHandler>> packetHandlers = new HashMap<PacketType, ArrayList<PacketHandler>>();
    private HashMap<LobbyType, Lobby> lobbies = new HashMap<LobbyType, Lobby>();

    private int playerIdCounter;
    private int gameIdCounter;

    public Server() {
        for (LobbyType lt : LobbyType.values()) {
            lobbies.put(lt, new Lobby(lt));
        }
    }

    public int getNextPlayerId() {
        return playerIdCounter++;
    }

    public int getNextGameId() {
        return gameIdCounter++;
    }

    public Lobby getLobby(LobbyType id) {
        if (lobbies.containsKey(id))
            return lobbies.get(id);
        return null;
    }

    /**
     * This is the only method that should be called from another thread (ie, the ClientChannelHandler)
     *
     * @param evt
     */
    public void addEvent(Event evt) {
        events.add(evt);
    }

    public ArrayList<PacketHandler> getPacketHandlers(PacketType type) {
        return packetHandlers.get(type);
    }

    public HashMap<Integer, Player> getPlayers() {
        return players;
    }

    public boolean hasPlayer(int id) {
        return players.containsKey(id);
    }

    public Player getPlayer(int id) {
        return players.get(id);
    }

    public void addPlayer(Player p) {
        if (!players.containsValue(p))
            players.put(p.getId(), p);
    }

    public void start() {
        //TrackStore.LoadTracks(); // gr8 piece of engineering right here!
        try {
            new TrackManager().load();
        } catch (IOException e) {
            System.err.println("Unable to load tracks: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        packetHandlers = PacketHandlerFactoryGeneratorClassHelperImplementationDecorator.getPacketHandlers();
        System.out.println("Loaded " + packetHandlers.size() + " packet handler type(s)");

        ChannelFactory factory = new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool());

        ServerBootstrap bootstrap = new ServerBootstrap(factory);
        final ClientChannelHandler clientHandler = new ClientChannelHandler(this);
        final IdleStateHandler idleState = new IdleStateHandler(new HashedWheelTimer(1, TimeUnit.SECONDS), 2, 0, 0);
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() {
                return Channels.pipeline(
                        new DelimiterBasedFrameDecoder(250, Delimiters.lineDelimiter()),
                        new PacketDecoder(),
                        new PacketEncoder(),
                        idleState,
                        clientHandler);
            }
        });
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        try {
            bootstrap.bind(new InetSocketAddress("0.0.0.0", 4242));
            new Thread(this).start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                Thread.sleep(10);
                Iterator<Event> iterator = events.iterator();
                while (iterator.hasNext()) {
                    Event evt = iterator.next();
                    try {
                        if (evt.shouldProcess(this)) {
                            evt.process(this);
                            iterator.remove();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        iterator.remove();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
