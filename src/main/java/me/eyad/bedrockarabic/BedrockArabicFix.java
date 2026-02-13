
package me.eyad.bedrockarabic;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import io.github.geysermc.geyser.api.GeyserApi;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class BedrockArabicFix extends JavaPlugin implements Listener {

    private GeyserApi geyserApi;

    @Override
    public void onEnable() {
        geyserApi = GeyserApi.api();
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("BedrockArabicFix Pro Enabled!");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        event.setCancelled(true);

        for (Player receiver : Bukkit.getOnlinePlayers()) {

            boolean isBedrock = geyserApi.isBedrockPlayer(receiver.getUniqueId());

            if (isBedrock) {
                receiver.sendMessage(processArabic(message));
            } else {
                receiver.sendMessage(message);
            }
        }
    }

    private String processArabic(String input) {
        try {
            ArabicShaping shaping = new ArabicShaping(
                    ArabicShaping.LETTERS_SHAPE |
                    ArabicShaping.TEXT_DIRECTION_LOGICAL |
                    ArabicShaping.LENGTH_GROW_SHRINK
            );

            String shaped = shaping.shape(input);

            Bidi bidi = new Bidi(shaped, Bidi.DIRECTION_DEFAULT_RIGHT_TO_LEFT);
            bidi.setReorderingMode(Bidi.REORDER_DEFAULT);
            return bidi.writeReordered(Bidi.DO_MIRRORING);

        } catch (ArabicShapingException e) {
            return input;
        }
    }
}
