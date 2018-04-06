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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

/**
 * Concrete implementation of the {@link LocalNoteDataSource} that works with Room.
 */
public class LocalNoteDataSource implements NoteDataSource {

    private static volatile LocalNoteDataSource INSTANCE;

    private NoteDao mUserDao;

    @VisibleForTesting
    LocalNoteDataSource(NoteDao userDao) {
        mUserDao = userDao;
    }

    public static LocalNoteDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            synchronized (LocalNoteDataSource.class) {
                if (INSTANCE == null) {
                    UsersDatabase database = UsersDatabase.getInstance(context);
                    INSTANCE = new LocalNoteDataSource(database.noteDao());
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Note getNote() {
        return mUserDao.getNote();
    }

    @Override
    public void insertOrUpdateNote(Note note) {
        mUserDao.insertNote(note);
    }

    @Override
    public void deleteAllNote() {
        mUserDao.deleteAllNote();
    }
}
