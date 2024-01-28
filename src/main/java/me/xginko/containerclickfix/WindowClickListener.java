package me.xginko.containerclickfix;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class WindowClickListener extends PacketAdapter {

    public WindowClickListener(JavaPlugin plugin) {
        super(plugin, ListenerPriority.HIGHEST, PacketType.Play.Client.WINDOW_CLICK);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        final PacketContainer packet = event.getPacket();

        final int button = this.getButton(packet);

        if (button < 0 || button > 9 && button != 40 && button != 99) {
            event.setCancelled(true);
            return;
        }

        final int slot = this.getSlot(packet);

        if (slot != -999 && slot != -1) {
            event.setCancelled(slot < 0);
            return;
        }

        if (button == 40) {
            event.setCancelled(true);
            return;
        }

        switch (this.getClickType(packet)) {
            case SWAP, PICKUP_ALL -> {
                event.setCancelled(true);
            }
            case THROW -> {
                event.setCancelled(slot == -1);
            }
            case QUICK_MOVE -> {
                event.setCancelled(slot == -999);
            }
        }
    }

    private int getSlot(PacketContainer packet) {
        return packet.getIntegers().read(2);
    }

    private int getButton(PacketContainer packet) {
        return packet.getIntegers().read(3);
    }

    private enum InventoryClickType {
        PICKUP, QUICK_MOVE, SWAP, CLONE, THROW, QUICK_CRAFT, PICKUP_ALL
    }

    private InventoryClickType getClickType(PacketContainer packet) {
        return packet.getEnumModifier(InventoryClickType.class, 4).read(0);
    }
}