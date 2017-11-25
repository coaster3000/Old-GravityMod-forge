package tk.coaster3000.gravity.command;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class GravityConfigCommand extends CommandTreeBase {
	final static String CMD_NAME = "gravity";

	public static GravityConfigCommand instance = new GravityConfigCommand();

	private GravityConfigCommand() {
		addSubcommand(GravityConfigSetCommand.instance);
		addSubcommand(GravityConfigGetCommand.instance);
		addSubcommand(GravityConfigSaveCommand.instance);
		addSubcommand(GravityConfigLoadCommand.instance);
	}

	@Override
	public String getName() {
		return CMD_NAME;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.gravity.usage";
	}
}
