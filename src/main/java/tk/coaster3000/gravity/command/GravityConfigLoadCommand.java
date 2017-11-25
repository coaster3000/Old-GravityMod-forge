package tk.coaster3000.gravity.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import tk.coaster3000.gravity.common.Config;

public class GravityConfigLoadCommand extends CommandBase {
	private static final String CMD_NAME = "load";

	static GravityConfigLoadCommand instance = new GravityConfigLoadCommand();

	private GravityConfigLoadCommand() {}

	@Override
	public String getName() {
		return CMD_NAME;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.gravity.load.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		try {
			Config.loadConfig();
			sender.sendMessage(new TextComponentTranslation("commands.gravity.load.complete"));
		} catch (Exception e) {
			sender.sendMessage(new TextComponentTranslation("commands.gravity.load.error", e.getMessage()));
		}

	}
}
