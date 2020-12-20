package com.jhooc77.Conversation.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import com.jhooc77.Conversation.PlayingConversation;
import com.jhooc77.Conversation.selectionType.Selection;

public class ConversationSelectEvent extends PlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	
	private PlayingConversation playingConversation;

	private boolean isCancelled;

	private Selection selected;
	
	public ConversationSelectEvent(PlayingConversation playingConversation, Selection selected) {
		super(playingConversation.getPlayer());
		this.playingConversation = playingConversation;
		this.isCancelled = false;
		this.selected = selected;
	}

	@Override
	public boolean isCancelled() {
		return this.isCancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.isCancelled = cancel;
	}
	
	public PlayingConversation getPlayingConversation() {
		return playingConversation;
	}
	
	public Selection getSelected() {
		return selected;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}

}
