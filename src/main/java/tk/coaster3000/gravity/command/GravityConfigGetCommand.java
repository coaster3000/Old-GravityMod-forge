package tk.coaster3000.gravity.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import tk.coaster3000.gravity.common.Config;

import java.util.Collection;

public class GravityConfigGetCommand extends CommandBase {
	private static final String CMD_NAME = "get";

	static GravityConfigGetCommand instance = new GravityConfigGetCommand();

	private GravityConfigGetCommand() {}

	@Override
	public String getName() {
		return CMD_NAME;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.gravity.get.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		Collection<String> keys = Config.getRetrievers().keySet();
		if (args.length > 0) {
			if (keys.contains(args[0])) {
				sender.sendMessage(new TextComponentTranslation("commands.gravity.get.result", args[0], Config.getRetrievers().get(args[0]).get()));
				return;
			} else {
				sender.sendMessage(new TextComponentTranslation("commands.gravity.get.invalid", args[0]));
			}
		} else {
			sender.sendMessage(new TextComponentTranslation(getUsage(sender)));
		}
		sender.sendMessage(new TextComponentTranslation("commands.gravity.get.list", CommandBase.joinNiceStringFromCollection(keys)));
	}
}
