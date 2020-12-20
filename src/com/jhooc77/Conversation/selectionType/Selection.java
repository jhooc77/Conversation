package com.jhooc77.Conversation.selectionType;

import org.bukkit.map.MapFont;

import com.jhooc77.Conversation.PlayingConversation;

public abstract class Selection {
	
	public abstract String getText();
	
	public abstract MapFont getFont();
	
	public abstract void applyResult(PlayingConversation playingConversation);

}
