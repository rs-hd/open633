package com.rs.game.npc.familiar;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.content.Summoning.Pouch;
import com.rs.game.task.Task;
import com.rs.utilities.RandomUtils;

import skills.Skills;

public class Spiritcoraxatrice extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4532687672911847604L;
	private int chocoTriceEgg;

	public Spiritcoraxatrice(Player owner, Pouch pouch, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public void processNPC() {
		super.processNPC();
		chocoTriceEgg++;
		if (chocoTriceEgg == 500)
			addChocolateEgg();
	}

	private void addChocolateEgg() {
		getBob().getBeastItems().add(new Item(12109, 1));
		chocoTriceEgg = 0;
	}

	@Override
	public String getSpecialName() {
		return "Petrifying Gaze";
	}

	@Override
	public String getSpecialDescription() {
		return "Inflicts damage and drains a combat stat, which varies according to the type of cockatrice.";
	}

	@Override
	public int getBOBSize() {
		return 30;
	}

	@Override
	public int getSpecialAmount() {
		return 5;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

	@Override
	public boolean submitSpecial(Object object) {
		final Entity target = (Entity) object;
		getOwner().setNextGraphics(new Graphics(1316));
		getOwner().setNextAnimation(new Animation(7660));
		setNextAnimation(new Animation(7766));
		setNextGraphics(new Graphics(1467));
		World.sendProjectile(this, target, 1468, 34, 16, 30, 35, 16, 0);
		
		target.ifPlayer(targetSelected -> {
			int level = targetSelected.getSkills().getLevelForXp(Skills.SUMMONING);
			int drained = 3;
			if (level - drained > 0)
				drained = level;
			targetSelected.getSkills().drainLevel(Skills.SUMMONING, drained);
		});
		World.get().submit(new Task(2) {
			@Override
			protected void execute() {
				target.applyHit(new Hit(getOwner(), RandomUtils.random(100), HitLook.MELEE_DAMAGE));
				this.cancel();
			}
		});
		return true;
	}
}
