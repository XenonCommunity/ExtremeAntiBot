package ir.realstresser.extreme.velocity.check.packetlisteners;

import dev.simplix.protocolize.api.Direction;
import dev.simplix.protocolize.api.listener.AbstractPacketListener;
import dev.simplix.protocolize.api.listener.PacketReceiveEvent;
import dev.simplix.protocolize.api.listener.PacketSendEvent;
import dev.simplix.protocolize.data.packets.HeldItemChange;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemHeldChange extends AbstractPacketListener<HeldItemChange> {
    public static ItemHeldChange instance;
    public final List<UUID> playerResponses;
    public ItemHeldChange() {
        super(HeldItemChange.class, Direction.UPSTREAM, 0);
        instance = this;
        playerResponses = new ArrayList<>();
    }
    @Override
    public void packetReceive(final PacketReceiveEvent<HeldItemChange> packetReceiveEvent) {
        /*final UUID playerId = packetReceiveEvent.player().uniqueId();
        if (!playerResponses.contains(playerId)) {
            playerResponses.add(playerId);
        }*/
        System.out.println("CLIENT bro just sent item held change packet");
    }

    @Override
    public void packetSend(final PacketSendEvent<HeldItemChange> packetSendEvent) {
        System.out.println("SERVER bro just sent item held change packet");
    }
    public boolean hasResponded(final UUID playerId) {
        return playerResponses.contains(playerId);
    }
}