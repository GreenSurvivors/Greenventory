package de.minebench.syncinv.messenger;

import de.greensurvivors.greensocket.network.packets.ByteArrayPacket;
import de.greensurvivors.greensocket.spigot.SpigotSocketApi;
import de.greensurvivors.greensocket.spigot.event.ReceivedPacketEvent;
import de.minebench.syncinv.SyncInv;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.IOException;
import java.util.logging.Level;

public class GreenSocketMessenger extends ServerMessenger {

    private static final String CHANNEL_PREFIX = "syncinv:";
    private static final String VERSION_PREFIX = Message.VERSION + ":";

    public GreenSocketMessenger(SyncInv plugin) {
        super(plugin);
        Bukkit.getPluginManager().registerEvents(getPacketListener(), plugin);

    }

    @Override
    protected void close() {}

    @Override
    protected void sendMessageImplementation(String target, Message message, boolean sync) {
        ByteArrayPacket packet = new ByteArrayPacket(CHANNEL_PREFIX + VERSION_PREFIX + target, message.toByteArray());
        if (sync) {
            determineSendMethod(target, packet);
        } else {
            this.plugin.runAsync(() -> {
                determineSendMethod(target, packet);
            });
        }
    }

    private void determineSendMethod(String target, ByteArrayPacket packet) {
        if (target.equals("*") || target.startsWith("group:")) {
            SpigotSocketApi.broadcastPacket(packet);
        } else {
            SpigotSocketApi.sendPacket(target, packet);
        }
    }

    private Listener getPacketListener() {
        return new Listener() {
            @EventHandler
            void listenPacket(ReceivedPacketEvent event) {
                if (event.getPacket() instanceof ByteArrayPacket) {
                    ByteArrayPacket byteArrayPacket = (ByteArrayPacket) event.getPacket();
                    if (!byteArrayPacket.getMetainfo().startsWith(CHANNEL_PREFIX)) {
                        plugin.getLogger().log(Level.WARNING, "Received a message on " + byteArrayPacket.getMetainfo() + " even 'though it doesn't belong to our plugin? ");
                        return;
                    }
                    if (!byteArrayPacket.getMetainfo().startsWith(CHANNEL_PREFIX + VERSION_PREFIX)) {
                        plugin.getLogger().log(Level.WARNING, "Received a message on " + byteArrayPacket.getMetainfo() + " that doesn't match the accepted version " + Message.VERSION + "! ");
                        return;
                    }
                    if (byteArrayPacket.getData().length == 0) {
                        plugin.getLogger().log(Level.WARNING, "Received a message with 0 bytes on " + byteArrayPacket.getMetainfo() + " channel? ");
                        return;
                    }
                    try {
                        onMessage(byteArrayPacket.getMetainfo().substring(CHANNEL_PREFIX.length() + VERSION_PREFIX.length()), Message.fromByteArray(byteArrayPacket.getData()));
                    } catch (IOException | ClassNotFoundException | IllegalArgumentException | InvalidConfigurationException e) {
                        plugin.getLogger().log(Level.SEVERE, "Error while decoding message on " + byteArrayPacket.getMetainfo() + " channel! ", e);
                    } catch (VersionMismatchException e) {
                        plugin.getLogger().log(Level.WARNING, e.getMessage() + ". Ignoring message!");
                    }
                }
            }
        };
    }
}
