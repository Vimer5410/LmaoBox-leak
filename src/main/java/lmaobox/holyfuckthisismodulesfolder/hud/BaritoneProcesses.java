package lmaobox.holyfuckthisismodulesfolder.hud;

import baritone.api.BaritoneAPI;
import baritone.api.process.IBaritoneProcess;
import meteordevelopment.meteorclient.systems.modules.render.hud.HUD;
import meteordevelopment.meteorclient.systems.modules.render.hud.modules.DoubleTextHudElement;

public class BaritoneProcesses extends DoubleTextHudElement {
	public BaritoneProcesses(HUD hud) {
		super(hud, "Baritone-Processes", "Displays what baritone is doing now.", "Baritone Process: ");
	}
	
	@Override
	protected String getRight() {
		IBaritoneProcess process = BaritoneAPI.getProvider().getPrimaryBaritone().getPathingControlManager().mostRecentInControl().orElse(null);
		
		if (process == null) return "X";
		
		return process.displayName();
		
		
	}
}
