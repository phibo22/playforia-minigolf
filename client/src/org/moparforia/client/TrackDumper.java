package org.moparforia.client;

import org.moparforia.client.trackdump.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Playforia
 * 3.6.2013
 */
public class TrackDumper implements ConnListener {

    static int track_basic = 1;
    static int track_traditional = 2;
    static int track_modern = 3;
    static int track_hio = 4;
    static int track_short = 5;
    static int track_long = 6;
    int track_type = -1;
    Connection conn = null;
    String map_dir = "trackdump/";
    int tries = 0;

    public TrackDumper(int track_type, String map_dir) {
        this.track_type = track_type;
        this.map_dir = map_dir;
        conn = new Connection(this, aStringArray2369);
        conn.connect();
        System.out.println("started downloading " + track_type + " into " + map_dir);
    }

    public static void main(String args[]) {
        new TrackDumper(track_basic, "tracks/basic/");
        new TrackDumper(track_traditional, "tracks/traditional/");
        new TrackDumper(track_modern, "tracks/modern/");
        new TrackDumper(track_hio, "tracks/hio/");
        new TrackDumper(track_short, "tracks/short/");
        new TrackDumper(track_long, "tracks/long/");
    }

    @Override
    public void dataReceived(String cmd) {
        try {
            String[] args = cmd.split("\t");
            if(args[0].equals("versok")) {
                conn.writeData("language\ten_US");
                conn.writeData("logintype\tnr");
            }
            else if(args[0].equals("status")) {
                if(args[1].equals("login")) {
                    conn.writeData("login");
                }
                else if(args[1].equals("lobbyselect")) {
                    String something = args[2];
                    conn.writeData("lobbyselect\trnop");
                }
                else if(args[1].equals("lobby")) {
                    String id = args[2];
                    if(id.equals("1"))
                        conn.writeData("lobby\ttracksetlist");
                }
                else if(args[1].equals("game")) {
                    // ok thanks
                }
            }
            else if(args[0].equals("basicinfo")) {
                // do nothing
            }
            else if(args[0].equals("lobbyselect")) {
                if(args[1].equals("nop")) {
                    conn.writeData("lobbyselect\tselect\t1");
                }
            }
            else if(args[0].equals("lobby")) {
                if(args[1].equals("numberofusers")) {
                    // do nothing
                }
                else if(args[1].equals("users")) {
                    // fuck those losers
                }
                else if(args[1].equals("ownjoin")) {
                    // yyaaayy that's us right?
                }
                else if(args[1].equals("tracksetlist")) {
                    // >implying we give a fuck

                    conn.writeData("lobby\tcspt\t20\t" + track_type + "\t0");
                }
            }
            else if(args[0].equals("game")) {
                if(args[1].equals("gameinfo")) {
                    // didn't read lol
                }
                else if(args[1].equals("players")) {
                    // noope, singleplayer
                }
                else if(args[1].equals("owninfo")) {
                    // !!
                }
                else if(args[1].equals("start")) {
                    // ???
                }
                else if(args[1].equals("starttrack")) {
                    boolean var17 = args[2].equals("ttm1");
                    boolean canPlay = args[2].equals("ttm2");
                    boolean var23 = var17 || canPlay;
                    boolean hasPlayed = false;
                    int startIndex = var23 ? 5 : 4;
                    int argsLen = args.length;
                    String author = null;
                    String name = null;
                    String data = null;
                    String fullInstruction = "";
                    for (int commandIndex = startIndex; commandIndex < argsLen; ++commandIndex) {
                        char var12 = args[commandIndex].charAt(0);
                        if (var12 == 'A') {
                            author = args[commandIndex].substring(2);
                            //tabSplit[commandIndex] = "A fc_and_vortex_co";
                        }

                        if (var12 == 'N') {
                            name = args[commandIndex].substring(2);
                            //tabSplit[commandIndex] = "N DA REIGHSTAFFFFFFFGGG GHETTO";
                        }

                        if (var12 == 'T') {
                            data = args[commandIndex].substring(2);
                        }

                        if (var12 == 'T' && args[commandIndex].charAt(2) == '!') {// a track we already played?
                            hasPlayed = true;
                        }

                        fullInstruction = fullInstruction + args[commandIndex];
                        if (commandIndex < argsLen - 1) {
                            fullInstruction = fullInstruction + '\n';
                        }
                    }
                    if(!hasPlayed) {
                        String checksum = MD5Checksum.getMD5Checksum(fullInstruction.getBytes());
                        File file = new File(map_dir, checksum + ".track");
                        if(!file.exists()) {
                            System.out.println("track " + checksum);
                            PrintStream out = null;
                            try {
                                out = new PrintStream(new FileOutputStream(file, false));
                                out.print(fullInstruction);
                            }
                            catch (Exception e) { }
                            finally {
                                if (out != null) out.close();
                            }
                        }
                    }
                }
                else if(args[1].equals("startturn")) {
                    conn.writeData("game\tskip");
                }
                else if(args[1].equals("end")) {
                    conn.writeData("game\tnewgame");
                }
            }
        } catch (Exception ex) {
            System.err.println("dataReceived error: " + ex);
            this.conn.disconnect();
        }
    }

    @Override
    public void connectionLost(int var1) {
        System.err.println("connectionLost: " + var1);
        if(tries < 5) {
            conn.connect();
            tries++;
        }
    }

    @Override
    public void notifyConnectionDown() {
        System.out.println("disconnected");
        if(tries < 5) {
            conn.connect();
            tries++;
        }
    }

    @Override
    public void notifyConnectionUp() {
        System.out.println("connected");
    }

    static String[] aStringArray2369 = new String[68];
    static {
        aStringArray2369[0] = "status\t";
        aStringArray2369[1] = "basicinfo\t";
        aStringArray2369[2] = "numberofusers\t";
        aStringArray2369[3] = "users\t";
        aStringArray2369[4] = "ownjoin\t";
        aStringArray2369[5] = "joinfromgame\t";
        aStringArray2369[6] = "say\t";
        aStringArray2369[7] = "logintype\t";
        aStringArray2369[8] = "login";
        aStringArray2369[9] = "lobbyselect\t";
        aStringArray2369[10] = "select\t";
        aStringArray2369[11] = "back";
        aStringArray2369[12] = "challenge\t";
        aStringArray2369[13] = "cancel\t";
        aStringArray2369[14] = "accept\t";
        aStringArray2369[15] = "cfail\t";
        aStringArray2369[16] = "nouser";
        aStringArray2369[17] = "nochall";
        aStringArray2369[18] = "cother";
        aStringArray2369[19] = "cbyother";
        aStringArray2369[20] = "refuse";
        aStringArray2369[21] = "afail";
        aStringArray2369[22] = "gsn\t";
        aStringArray2369[23] = "lobby\tnc\t";
        aStringArray2369[24] = "lobby\t";
        aStringArray2369[25] = "lobby";
        aStringArray2369[26] = "tracksetlist\t";
        aStringArray2369[27] = "tracksetlist";
        aStringArray2369[28] = "gamelist\t";
        aStringArray2369[29] = "full\t";
        aStringArray2369[30] = "add\t";
        aStringArray2369[31] = "change\t";
        aStringArray2369[32] = "remove\t";
        aStringArray2369[33] = "gameinfo\t";
        aStringArray2369[34] = "players";
        aStringArray2369[35] = "owninfo\t";
        aStringArray2369[36] = "game\tstarttrack\t";
        aStringArray2369[37] = "game\tstartturn\t";
        aStringArray2369[38] = "game\tstart";
        aStringArray2369[39] = "game\tbeginstroke\t";
        aStringArray2369[40] = "game\tendstroke\t";
        aStringArray2369[41] = "game\tresetvoteskip";
        aStringArray2369[42] = "game\t";
        aStringArray2369[43] = "game";
        aStringArray2369[44] = "quit";
        aStringArray2369[45] = "join\t";
        aStringArray2369[46] = "part\t";
        aStringArray2369[47] = "cspt\t";
        aStringArray2369[48] = "qmpt";
        aStringArray2369[49] = "cspc\t";
        aStringArray2369[50] = "jmpt\t";
        aStringArray2369[51] = "tracklist\t";
        aStringArray2369[52] = "Tiikoni";
        aStringArray2369[53] = "Leonardo";
        aStringArray2369[54] = "Ennaji";
        aStringArray2369[55] = "Hoeg";
        aStringArray2369[56] = "Darwin";
        aStringArray2369[57] = "Dante";
        aStringArray2369[58] = "ConTrick";
        aStringArray2369[59] = "Dewlor";
        aStringArray2369[60] = "Scope";
        aStringArray2369[61] = "SuperGenuis";
        aStringArray2369[62] = "Zwan";
        aStringArray2369[63] = "\tT !\t";
        aStringArray2369[64] = "\tcr\t";
        aStringArray2369[65] = "rnop";
        aStringArray2369[66] = "nop\t";
        aStringArray2369[67] = "error";
    }
}
