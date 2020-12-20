package com.jhooc77.Conversation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.jhooc77.Conversation.selectionType.Selection;

public class Conversation {
	
	private static HashMap<String, Conversation> conversation = new HashMap<String, Conversation>();
	
	private String name;
	private ArrayList<Selection> selection = new ArrayList<Selection>(3);
	private ConversationPlugin plugin;
	
	public Conversation (ConversationPlugin plugin, String name) {
		this.name = name;
		this.plugin = plugin;
		conversation.put(name, this);
	}
	
	public void putSelection(Selection result) {
		this.selection.add(result);
	}
	
	public Selection getSelection(int num) {
		return selection.get(num);
	}
	
	public String getName() {
		return this.name;
	}
	
	public void display(PlayingConversation playingConversation) {
		ItemStack item = new ItemStack(Material.FILLED_MAP);
		MapMeta meta = (MapMeta) item.getItemMeta();
		MapView view = Bukkit.createMap(playingConversation.getPlayer().getWorld());
		view.getRenderers().clear();
		view.addRenderer(new MapRenderer(true) {
			
			@Override
			public void render(MapView view, MapCanvas canvas, Player player) {
				canvas.drawImage(0, 0, plugin.getImage());
				canvas.drawText(10, 20, selection.get(0).getFont(), selection.get(0).getText());
				canvas.drawText(10, 60, selection.get(1).getFont(), selection.get(1).getText());
				canvas.drawText(10, 100, selection.get(2).getFont(), selection.get(2).getText());
			}
		});
		view.getRenderers().clear();
		meta.setMapView(view);
		item.setItemMeta(meta);
		playingConversation.getFrame().setItem(item);
		return;
	}
	
	public static Conversation getConversation(String name) {
		return conversation.get(name);
	}
	
	public static Set<Conversation> getAllConversation() {
		return conversation.values().stream().collect(Collectors.toSet());
	}

}
