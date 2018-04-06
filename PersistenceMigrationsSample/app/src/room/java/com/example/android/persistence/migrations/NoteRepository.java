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

import java.lang.ref.WeakReference;

/**
 * The repository is responsible of handling user data operations.
 */
public class NoteRepository {

    private AppExecutors mAppExecutors;

    private NoteDataSource mUserDataSource;

    private Note mCachedUser;

    public NoteRepository(AppExecutors appExecutors, NoteDataSource userDataSource) {
        mAppExecutors = appExecutors;
        mUserDataSource = userDataSource;
    }

    /**
     * Get the user from the data source, cache it and notify via the callback that the user has
     * been retrieved.
     *
     * @param callback callback that gets called when the user was retrieved from the data source.
     */
    void getUser(LoadNoteCallback callback) {
        final WeakReference<LoadNoteCallback> loadUserCallback = new WeakReference<>(callback);

        // request the user on the I/O thread
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final Note user = mUserDataSource.getNote();
                // notify on the main thread
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        final LoadNoteCallback userCallback = loadUserCallback.get();
                        if (userCallback == null) {
                            return;
                        }
                        if (user == null) {
                            userCallback.onDataNotAvailable();
                        } else {
                            mCachedUser = user;
                            userCallback.onNoteLoaded(mCachedUser);
                        }
                    }
                });
            }
        });
    }

    /**
     * Insert an new user or update the name of the user.
     *
     * @param userName the user name
     * @param callback callback that gets triggered when the user was updated.
     */
    void updateUserName(String userName, UpdateNoteCallback callback) {
        final WeakReference<UpdateNoteCallback> updateUserCallback = new WeakReference<>(callback);

        final Note user = mCachedUser == null
                ? new Note(userName)
                : new Note(mCachedUser.getId(), userName);

        // update the user on the I/O thread
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mUserDataSource.insertOrUpdateNote(user);
                mCachedUser = user;
                // notify on the main thread
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        UpdateNoteCallback userCallback = updateUserCallback.get();
                        if (userCallback != null) {
                            userCallback.onNoteUpdated(user);
                        }
                    }
                });
            }
        });
    }


}

