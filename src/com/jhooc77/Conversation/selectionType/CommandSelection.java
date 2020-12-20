package com.jhooc77.Conversation.selectionType;

import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapFont;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.jhooc77.Conversation.ConversationPlugin;
import com.jhooc77.Conversation.PlayingConversation;

public class CommandSelection extends Selection {
	
	private String text;
	private MapFont font;
	private String command;
	private ConversationPlugin plugin;
	
	public CommandSelection(String text, MapFont font, String command, ConversationPlugin plugin) {
		this.text = text;
		this.font = font;
		this.command = command;
		this.plugin = plugin;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public MapFont getFont() {
		return font;
	}

	@Override
	public void applyResult(PlayingConversation playingConversation) {
		MapView view = ((MapMeta) playingConversation.getFrame().getItem().getItemMeta()).getMapView();
		view.getRenderers().clear();
		view.addRenderer(new MapRenderer(true) {

			@Override
			public void render(MapView view, MapCanvas canvas, Player player) {
				canvas.setCursors(new MapCursorCollection());
				canvas.drawImage(0, 0, plugin.getImage());
				canvas.drawText(10, 50, font, "Command Executed");
			}
		});
		view.getRenderers().clear();
		plugin.getServer().dispatchCommand(playingConversation.getPlayer(), command);
		playingConversation.stop();
	}
}
