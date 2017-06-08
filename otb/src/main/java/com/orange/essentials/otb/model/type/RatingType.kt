/*
 * Copyright 2016 Orange
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/* Orange Trust Badge library
 *
 * Module name: com.orange.essentials:otb
 * Version:     1.0
 * Created:     2016-03-15 by Aurore Penault, Vincent Boesch, and Giovanni Battista Accetta
 */
package com.orange.essentials.otb.model.type

/**
 * List of possible PEGI status.
 */
enum class RatingType private constructor(val age: Int) {
    /**
     * Host app age rating 3 and above
     */
    THREE(3),
    /**
     * Host app age rating 7 and above
     */
    SEVEN(7),
    /**
     * Host app age rating 12 and above
     */
    TWELVE(12),
    /**
     * Host app age rating 16 and above
     */
    SIXTEEN(16),
    /**
     * Host app age rating 18 and above
     */
    EIGHTEEN(18)
}
