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

package tk.coaster3000.gravity.util;

import net.minecraft.world.World;
import tk.coaster3000.gravity.IWorldHandle;
import tk.coaster3000.gravity.WorldHandle;

public class Util {
	/**
	 * Wraps a world in a world handle for abstraction purpose.
	 * @param world for the world handle object
	 * @return a new world handle object.
	 */
	public static IWorldHandle wrapWorld(World world) {
		return new WorldHandle(world);
	}
}
