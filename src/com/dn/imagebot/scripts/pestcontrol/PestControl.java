package com.dn.imagebot.scripts.pestcontrol;

import com.dn.imagebot.main.Bot;
import com.dn.imagebot.script.LoopScript;

public class PestControl extends LoopScript {

	public PestControl(Bot bot) {
		super(bot);
	}

	@Override
	public boolean onStart() {
		return true;
	}

	@Override
	public int onLoop() {
		/*if (onIsland()) {
			System.out.println("here");
			context.walking.walkTo("pest_control_middle");
		} else if (!inBoat()) {
			Point p = context.colors.getClosest(context.colors.findColorObjectArray(0.01, -10269651, 1, 10));
			if (p != null) {
				if (context.mouse.click(p, true)) {
					Time.sleep(new Condition() {

						@Override
						public boolean validate() {
							return !inBoat();
						}
						
					}, 1500, 2500);
				}
			}
		}
		System.out.println(inBoat());*/
		System.out.println(context.walking.isMoving());
		return 100;
	}

	@Override
	public void onFinish() {

	}
	
	private boolean inBoat() {
		return context.colors.isColorAt(-2174102, 138, 73, 0.05d);
	}
	
	private boolean onIsland() {
		return context.walking.getDistance("pest_control_middle") != -1;
	}

}
