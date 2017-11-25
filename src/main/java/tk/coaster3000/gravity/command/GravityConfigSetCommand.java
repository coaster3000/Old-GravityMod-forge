package tk.coaster3000.gravity.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import tk.coaster3000.gravity.common.Config;

import java.util.Collection;
import java.util.function.Consumer;

public class GravityConfigSetCommand extends CommandBase {
	final static String CMD_NAME = "set";

	static GravityConfigSetCommand instance = new GravityConfigSetCommand();

	private GravityConfigSetCommand(){}

	@Override
	public String getName() {
		return CMD_NAME;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.gravity.set.usage";
	}

	@Override
	@SuppressWarnings("unchecked")
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		Collection<String> keys = Config.getSetters().keySet();
		if (args.length > 0) {
			if (keys.contains(args[0])) {
				Consumer consumer = Config.getSetters().get(args[0]);

				Class cType = Config.getKeyClass().get(args[0]);
				if (Integer.class.isAssignableFrom(cType)) {
					consumer.accept(parseInt(args[1]));
				} else if (Double.class.isAssignableFrom(cType)) {
					consumer.accept(parseDouble(args[1]));
				} else if (Long.class.isAssignableFrom(cType)) {
					consumer.accept(parseLong(args[1]));
				} else if (Boolean.class.isAssignableFrom(cType)) {
					consumer.accept(parseBoolean(args[1]));
				} else if (String.class.isAssignableFrom(cType)) {
					consumer.accept(args[1]);
				} else {
					throw new CommandException("commands.gravity.set.invalidcast", args[0], cType.getName());
				}

				sender.sendMessage(new TextComponentTranslation("commands.gravity.set.result", args[0], args[1]));
				return;
			} else {
				sender.sendMessage(new TextComponentTranslation("commands.gravity.set.invalid", args[0]));
			}
		} else {
			sender.sendMessage(new TextComponentTranslation(getUsage(sender)));
		}
		sender.sendMessage(new TextComponentTranslation("commands.gravity.set.list", CommandBase.joinNiceStringFromCollection(keys)));
	}
}
