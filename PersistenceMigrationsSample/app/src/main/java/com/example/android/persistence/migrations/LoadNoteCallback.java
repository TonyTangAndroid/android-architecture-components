/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.persistence.migrations;

import android.support.annotation.MainThread;

/**
 * Callback called when the user was loaded from the repository.
 */
public interface LoadNoteCallback {
    /**
     * Method called when the user was loaded from the repository.
     *
     * @param note the user from repository.
     */
    @MainThread
    void onNoteLoaded(Note note);

    /**
     * Method called when there was no user in the repository.
     */
    @MainThread
    void onDataNotAvailable();
}
