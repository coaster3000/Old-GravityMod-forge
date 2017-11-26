/**
 * Copyright 2017 Coaster3000 (Christopher Krier)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
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
	static final String CMD_NAME = "set";

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
