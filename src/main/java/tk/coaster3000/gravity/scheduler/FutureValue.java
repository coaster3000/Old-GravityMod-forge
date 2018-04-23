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

package tk.coaster3000.gravity.scheduler;

import java.util.NoSuchElementException;

public interface FutureValue<T> {

	/**
	 * Retrieves the value stored.
	 * The standard behaviour of this method is to return the value stored, or throw an exception if the value is not ready.
	 * @return value stored of type T
	 * @throws NoSuchElementException if the value was not available at the time of the call.
	 */
	T getValue() throws NoSuchElementException;

	/**
	 * Tells whether or not the value is available to be called upon.
	 * @return true if value is available, false otherwise
	 */
	boolean hasValue();
}
