package me.xginko.containerclickfix;

import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.plugin.java.JavaPlugin;

public final class ContainerClickFix extends JavaPlugin {
    @Override
    public void onEnable() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new WindowClickListener(this));
    }
}
