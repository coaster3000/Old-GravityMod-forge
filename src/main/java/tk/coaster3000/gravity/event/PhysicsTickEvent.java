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
package tk.coaster3000.gravity.event;

import net.minecraftforge.fml.common.eventhandler.Event;
import tk.coaster3000.gravity.IWorldHandle;

public class PhysicsTickEvent extends Event {
	public final Phase phase;
	public final IWorldHandle worldHandle;

	public enum Phase {
		START, END
	}

	/**
	 * Constructs a Physics Tick Event for use within the event system.
	 * @param phase the phase of the event, see {@link Phase}
	 * @param worldHandle involved in the event
	 */
	public PhysicsTickEvent(Phase phase, IWorldHandle worldHandle) {
		this.phase = phase;
		this.worldHandle = worldHandle;
	}

	@Override
	public boolean isCancelable() {
		return phase == Phase.START;
	}
}
