/*
 * Copyright 2018 Coaster3000 (Christopher Krier)
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

public class GravityConfigSaveCommand extends CommandBase {
	private static final String CMD_NAME = "save";

	static GravityConfigSaveCommand instance = new GravityConfigSaveCommand();

	private GravityConfigSaveCommand() {}

	@Override
	public String getName() {
		return CMD_NAME;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.gravity.config.save.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		try {
			Config.saveConfig();
			sender.sendMessage(new TextComponentTranslation("commands.gravity.save.complete"));
		} catch (Exception e) {
			sender.sendMessage(new TextComponentTranslation("commands.gravity.save.error", e.getMessage()));
		}
	}
}
