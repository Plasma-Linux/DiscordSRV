/*-
 * LICENSE
 * DiscordSRV
 * -------------
 * Copyright (C) 2016 - 2021 Austin "Scarsz" Shapiro
 * -------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * END
 */

package github.scarsz.discordsrv.util;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlaceholderUtil {

    private PlaceholderUtil() {}

    public static String replacePlaceholders(String input) {
        return replacePlaceholders(input, null);
    }

    public static String replacePlaceholders(String input, OfflinePlayer player) {
        if (input == null) return null;
        if (PluginUtil.pluginHookIsEnabled("placeholderapi")) {
            Player onlinePlayer = player != null ? player.getPlayer() : null;
            input = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(
                    onlinePlayer != null ? onlinePlayer : player, input);
        }
        return input;
    }

    /**
     * Important when the content may contain role mentions
     */
    public static String replacePlaceholdersToDiscord(String input) {
        return replacePlaceholdersToDiscord(input, null);
    }

    /**
     * Important when the content may contain role mentions
     */
    public static String replacePlaceholdersToDiscord(String input, OfflinePlayer player) {
        boolean placeholderapi = PluginUtil.pluginHookIsEnabled("placeholderapi");

        // PlaceholderAPI has a side effect of replacing chat colors at the end of placeholder conversion
        // that breaks role mentions: <@&role id> because it converts the & to a §
        // So we add a zero width space after the & to prevent it from translating, and remove it after conversion
        if (placeholderapi) input = input.replace("&", "&\u200B");

        input = replacePlaceholders(input, player);

        if (placeholderapi) {
            input = MessageUtil.stripLegacySectionOnly(input); // Color codes will be in this form
            input = input.replace("&\u200B", "&");
        }
        return input;
    }
}
