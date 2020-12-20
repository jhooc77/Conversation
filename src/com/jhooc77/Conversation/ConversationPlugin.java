package com.jhooc77.Conversation;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.map.MinecraftFont;
import org.bukkit.plugin.java.JavaPlugin;

import com.jhooc77.Conversation.event.ConversationSelectEvent;
import com.jhooc77.Conversation.selectionType.CommandSelection;
import com.jhooc77.Conversation.selectionType.ConversationSelection;
import com.jhooc77.Conversation.selectionType.Selection;
import com.jhooc77.Conversation.selectionType.TextSelection;

public class ConversationPlugin extends JavaPlugin implements Listener {

	private BufferedImage image;
	
	private Conversation conversation;

	@Override
	public void onEnable() {
		new Task(this).run();
		try {
			Image image = ImageIO.read(getResource("image.png"));
			image.getScaledInstance(128, 128, Image.SCALE_SMOOTH);
			BufferedImage newImage = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics2D = newImage.createGraphics();
			graphics2D.drawImage(image, 0, 0, 128, 128, null);
			graphics2D.dispose();
            this.image = newImage;
		} catch (IOException e) {
			e.printStackTrace();
		}
		getServer().getPluginManager().registerEvents(this, this);
		
		//대화문 넣는장면입니다. 선택지는 3개만가능
		
		conversation = new Conversation(this, "프리");
		conversation.putSelection(new TextSelection("Hi JingJingE!", MinecraftFont.Font, "Hi BabyMandarinDuck!", this));
		conversation.putSelection(new CommandSelection("Give me the Boat!", MinecraftFont.Font, "give BabyMandarinDuck minecraft:acacia_boat 1", this));
		conversation.putSelection(new ConversationSelection("Again", MinecraftFont.Font, conversation, this));
	}

	@Override
	public void onDisable() {
		PlayingConversation.getAllPlayingConversation().values().forEach(cp -> cp.stop());
	}
	
	@EventHandler
	public void t(PlayerInteractAtEntityEvent event) {
		if (!(event.getRightClicked() instanceof LivingEntity)) return;
		Location loc = event.getRightClicked().getLocation().toBlockLocation();
		ItemFrame frame = (ItemFrame) loc.getWorld().spawn(loc, ItemFrame.class, itemframe -> {
			itemframe.setFixed(true);
			itemframe.setFacingDirection(BlockFace.WEST);
			itemframe.setGravity(false);
			itemframe.setInvulnerable(true);
		});
		PlayingConversation con = new PlayingConversation((LivingEntity) event.getRightClicked(), frame, event.getPlayer(), conversation);
		con.start();
	}
	
	@EventHandler
	public void t(PlayerInteractEvent event) {
		if (!event.getAction().equals(Action.LEFT_CLICK_AIR)) return;
		if (PlayingConversation.hasPlayingConversation(event.getPlayer())) {
			event.setCancelled(true);
			PlayingConversation pc = PlayingConversation.getPlayingConversation(event.getPlayer());
			Selection sel = pc.getSelection();
			if (sel == null) return;
			ConversationSelectEvent nevent = new ConversationSelectEvent(pc, sel);
			getServer().getPluginManager().callEvent(nevent);
			if (nevent.isCancelled()) return;
			sel.applyResult(pc);
		}
	}
	
	@EventHandler
	public void t(HangingBreakByEntityEvent event) {
		PlayingConversation.getAllPlayingConversation().values().forEach(pc -> {
			if (event.getEntity().equals(pc.getFrame())) {
				event.setCancelled(true);
			}
		});
	}
	
	public BufferedImage getImage() {
		return image;
	}

	private class Task {
		
		private ConversationPlugin plugin;
		
		public Task(ConversationPlugin plugin) {
			this.plugin = plugin;
		}

		public void run() {
			getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
				public void run() {
					PlayingConversation.getAllPlayingConversation().values().forEach(pc -> {
						getServer().getScheduler().runTask(plugin, () -> {
							pc.getFrame().setFixed(true);
							pc.getFrame().teleport(pc.getEntity().getLocation().clone().add(-1, 1, 0));
							double pitch = pc.getPlayer().getLocation().getPitch();
							if (pitch > 15) {
								pc.setSelection(3);
							} else if (pitch < -5) {
								pc.setSelection(1);
							} else {
								pc.setSelection(2);
							}
						});
					});
				}
			}, 0L, 1L);
		}
	}
}
