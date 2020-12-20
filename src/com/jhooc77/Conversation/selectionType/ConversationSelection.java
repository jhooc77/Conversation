package com.jhooc77.Conversation.selectionType;

import org.bukkit.map.MapFont;

import com.jhooc77.Conversation.Conversation;
import com.jhooc77.Conversation.ConversationPlugin;
import com.jhooc77.Conversation.PlayingConversation;

public class ConversationSelection extends Selection {
	
	private String text;
	private MapFont font;
	private Conversation conversation;
	private ConversationPlugin plugin;
	
	public ConversationSelection(String text, MapFont font, Conversation conversation, ConversationPlugin plugin) {
		this.text = text;
		this.font = font;
		this.conversation = conversation;
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
		PlayingConversation con = new PlayingConversation(playingConversation.getEntity(), playingConversation.getFrame(), playingConversation.getPlayer(), conversation);
		con.start();
	}

}
