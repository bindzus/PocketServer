/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pocketserver.packets;

import pocketserver.packets.Packet;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import pocketserver.Hex;
import pocketserver.PacketHandler;

/**
 *
 * @author dev
 */
public class Packet1c extends Packet {
    private long pingID;
    private int packetType;
    private byte[] magic;
    private long serverID = 1L;
    
    String identifier = "MCCPP;MINECON;";
    String serverName = "PocketServer";

    public Packet1c(DatagramPacket p) {
        ByteBuffer bb = ByteBuffer.wrap(p.getData());
        packetType = bb.get();
        if (packetType != 0x1c) { return; }
        pingID = bb.getLong();
        magic = Hex.getMagicFromBuffer(bb);
    }

    public DatagramPacket getPacket() {
        String motd =  identifier + serverName;
        byte[] motdBytes = motd.getBytes();
        ByteBuffer rData;
        rData = ByteBuffer.allocate(35+motd.length());
        rData.put((byte)0x1c);
        rData.putLong(pingID);
        rData.putLong(serverID);
        rData.put(magic);
        rData.putShort((short)motd.length());
        rData.put(motdBytes);
        return new DatagramPacket(rData.array(),35+motd.length());
    }
    
    public void process(PacketHandler handler) {
        handler.write(getPacket());
    }
}
