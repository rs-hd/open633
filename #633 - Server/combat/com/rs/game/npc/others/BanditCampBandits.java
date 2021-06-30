package com.rs.game.npc.others;

import com.rs.game.Entity;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.godwars.saradomin.GodwarsSaradominFaction;
import com.rs.game.npc.godwars.zammorak.GodwarsZammorakFaction;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.ForceTalk;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class BanditCampBandits extends NPC {

	public BanditCampBandits(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea,
			boolean spawned) {
		super((short) id, tile, (byte) mapAreaNameHash, canBeAttackFromOutOfArea);
		setForceAgressive(true); // to ignore combat lvl
	}

	@Override
	public ObjectArrayList<Entity> getPossibleTargets() {
		ObjectArrayList<Entity> targets = super.getPossibleTargets();
		ObjectArrayList<Entity> targetsCleaned = new ObjectArrayList<Entity>();
		for (Entity t : targets) {
			if (!(t.isPlayer()) || (!GodwarsZammorakFaction.hasGodItem((Player) t)
					&& !GodwarsSaradominFaction.hasGodItem((Player) t)))
				continue;
			targetsCleaned.add(t);
		}
		return targetsCleaned;
	}

	@Override
	public void setTarget(Entity entity) {
		if (entity.isPlayer() && (GodwarsZammorakFaction.hasGodItem((Player) entity)
				|| GodwarsSaradominFaction.hasGodItem((Player) entity)))
			setNextForceTalk(new ForceTalk(
					GodwarsZammorakFaction.hasGodItem((Player) entity) ? "Time to die, Saradominist filth!"
							: "Prepare to suffer, Zamorakian scum!"));
		super.setTarget(entity);
	}

}
