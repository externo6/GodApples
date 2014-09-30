package me.externo6.godapples;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Material.*;

import java.util.*;

public class AListener implements Listener {

    /** playername -> last throw timestamp */
    private final Map<String, Long> lastEaten = new HashMap<String, Long>();
    
    
    //check if its an apple
    @EventHandler(priority = EventPriority.NORMAL)
    
    public void onPlayerConsumeGodApple(PlayerItemConsumeEvent event) {
    	Player player = event.getPlayer();
        if (event.isCancelled()) return;
        if (event.getItem() == null
                || event.getItem().getType() != GOLDEN_APPLE ) {
        	//player.sendMessage("Eaten 4");
            return;
        }

        // may a player use godapples at all?
        if (!player.hasPermission("godapples.use")) {
            sendMessageChecked(player, Main.getInstance().messageNotAllowed);
            event.setCancelled(true);
            return;
        }

        // apply cooldown to player
        Long now = System.currentTimeMillis();
        if (valideat(player, now)) {
        	lastEaten.put(player.getName(), now);
         //   player.sendMessage("Eaten 1");
        } else {
            event.setCancelled(true);
        //    player.sendMessage("Eaten 2");
        }
    }
    /** Return remaining cooldown in seconds. */
    private double remainingCooldown(Player player, long eattime) {
        Long lastAppleEaten = lastEaten.get(player.getName());
      //  player.sendMessage("Eaten 3");
        return (Main.getInstance().cooldown - (eattime - lastAppleEaten)) / 1000.0;
    }

    /** Check if player is able to eat */
    private boolean valideat(Player player, long eattime) {
        if (player.hasPermission("godapple.cooldown")) {
            return true; // no cooldown for this player
        }

        Long lastAppleEaten = lastEaten.get(player.getName());

        // for players with cooldown, check if cooldown has passed
        if (lastAppleEaten == null
                || (eattime - lastAppleEaten) >= Main.getInstance().cooldown) {
            return true;
        }

        sendMessageChecked(
                player,
                Main.getInstance().messageCooldown.replace(
                        "{seconds}",
                        String.format("%.1f",
                                remainingCooldown(player, eattime))));
        return false;
    }

    private static void sendMessageChecked(Player player, String message) {
        if (Main.getInstance().showMessage) {
            player.sendMessage(message);
        }
    }
}
    