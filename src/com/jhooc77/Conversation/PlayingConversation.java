package com.jhooc77.Conversation;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.jhooc77.Conversation.event.ConversationEndEvent;
import com.jhooc77.Conversation.event.ConversationStartEvent;
import com.jhooc77.Conversation.selectionType.Selection;

public class PlayingConversation {
	
	private static HashMap<Player, PlayingConversation> list = new HashMap<Player, PlayingConversation>();
	
	private Player player;
	private ItemFrame frame;
	private Conversation conversation;
	private LivingEntity entity;
	private int selection = 0;

	public PlayingConversation(LivingEntity entity, ItemFrame frame, Player player, Conversation conversation) {
		this.player = player;
		this.frame = frame;
		this.conversation = conversation;
		this.entity = entity;
		list.put(player, this);
	}
	
	public Conversation getConversation() {
		return conversation;
	}
	
	public ItemFrame getFrame() {
		return frame;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public LivingEntity getEntity() {
		return entity;
	}
	
	public void setSelection(int num) {
		selection = num;
	}
	
	public Selection getSelection() {
		return selection!=0?conversation.getSelection(selection-1):null;
	}
	
	public void start() {
		getAllPlayingConversation().values().forEach(cp -> {
			if (cp.entity.equals(this.entity)) return;
		});
		ConversationStartEvent event = new ConversationStartEvent(this);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) return;
		entity.teleport(entity.getLocation().setDirection(player.getLocation().subtract(entity.getLocation()).toVector()));
		entity.setAI(false);
		conversation.display(this);
		MapCursorCollection cursors = new MapCursorCollection();
		MapCursor cursor = new MapCursor((byte) -120, (byte) 0, (byte) 4, MapCursor.Type.BANNER_GREEN, true);
		cursors.addCursor(cursor);
		((MapMeta) frame.getItem().getItemMeta()).getMapView().addRenderer(new MapRenderer(true) {
			
			@Override
			public void render(MapView view, MapCanvas canvas, Player p) {
				canvas.setCursors(cursors);
				switch(selection) {
				case(1):
					cursor.setVisible(true);
					cursor.setY((byte) (Byte.MIN_VALUE+40));
					break;
				case(2):
					cursor.setVisible(true);
					cursor.setY((byte) (Byte.MIN_VALUE+120));
					break;
				case(3):
					cursor.setVisible(true);
					cursor.setY((byte) (Byte.MIN_VALUE+200));
					break;
				default:
					cursor.setVisible(false);
					break;
				}
			}
		});
	}
	
	public void stop() {
		ConversationEndEvent event = new ConversationEndEvent(this);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) return;
		list.remove(player);
		Bukkit.getScheduler().runTaskLater(ConversationPlugin.getPlugin(ConversationPlugin.class), new Runnable() {
			public void run() {
				frame.remove();
				entity.setAI(true);
			}
		}, 120L);
	}
	
	public static PlayingConversation getPlayingConversation(Player player) {
		return list.get(player);
	}
	
	public static HashMap<Player, PlayingConversation> getAllPlayingConversation() {
		return list;
	}

	public static boolean hasPlayingConversation(Player player) {
		return list.containsKey(player);
	}
	
	

}
