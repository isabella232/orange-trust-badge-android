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
package com.orange.essentials.otb.event

/**
 * Created by veeb7280 on 05/02/2016.
 * This enum represents the possible events that can be triggered in the lib
 */
enum class EventType {
    /// Event triggered when entering in Permissions Page
    TRUSTBADGE_PERMISSION_ENTER,

    /// Event triggered when entering in Usage Page
    TRUSTBADGE_USAGE_ENTER,

    /// Event triggered when entering in Terms Page
    TRUSTBADGE_TERMS_ENTER,

    /// Event triggered when leaving TrustBadge component
    TRUSTBADGE_LEAVE,

    /// Event triggered when tapping a TrustBadge element to open/close associated explanations
    TRUSTBADGE_ELEMENT_TAPPED,

    /// Event triggered when toggling a TrustBadge element (if toggable)
    TRUSTBADGE_ELEMENT_TOGGLED,

    /// Event triggered when going into Android settings
    TRUSTBADGE_GO_TO_SETTINGS
}
