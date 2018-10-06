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

import androidx.annotation.Nullable;

/**
 * Listens for users's actions from the UI {@link UserActivity}, retrieves the data and updates
 * the UI as required.
 */
public class UserPresenter {

    private final LoadNoteCallback mLoadNoteCallback;
    private final UpdateNoteCallback mUpdateNoteCallback;
    private final NoteRepository noteRepository;
    private UserRepository mDataSource;
    @Nullable
    private UserView mView;

    private LoadUserCallback mLoadUserCallback;
    private UpdateUserCallback mUpdateUserCallback;

    public UserPresenter(UserRepository dataSource, NoteRepository noteRepository, UserView view) {
        mDataSource = dataSource;
        this.noteRepository = noteRepository;
        mView = view;

        mLoadUserCallback = createLoadUserCallback();
        mLoadNoteCallback = createLoadNoteCallback();
        mUpdateUserCallback = createUpdateUserCallback();
        mUpdateNoteCallback = createUpdateNoteCallback();
    }

    /**
     * Start working with the view.
     */
    public void start() {
        mDataSource.getUser(mLoadUserCallback);
        noteRepository.getUser(mLoadNoteCallback);
    }

    public void stop() {
        mView = null;
    }

    /**
     * Update the username of the user.
     *
     * @param userName the new userName
     */
    public void updateUserName(final String userName) {

        mDataSource.updateUserName(userName, mUpdateUserCallback);
    }

    /**
     * Update the username of the user.
     *
     * @param userName the new userName
     */
    public void updateNote(final String userName) {

        noteRepository.updateUserName(userName, mUpdateNoteCallback);
    }

    private LoadUserCallback createLoadUserCallback() {
        return new LoadUserCallback() {
            @Override
            public void onUserLoaded(User user) {
                if (mView != null) {
                    mView.showUserName(user.getUserName());
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (mView != null) {
                    mView.hideUserName();
                }
            }
        };
    }

    private LoadNoteCallback createLoadNoteCallback() {
        return new LoadNoteCallback() {
            @Override
            public void onNoteLoaded(Note note) {
                if (mView != null) {
                    mView.showNote(note.getUserName());
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (mView != null) {
                    mView.hideNote();
                }
            }
        };
    }

    private UpdateUserCallback createUpdateUserCallback() {
        return user -> {
            if (mView != null) {
                mView.showUserName(user.getUserName());
            }
        };
    }

    private UpdateNoteCallback createUpdateNoteCallback() {
        return note -> {
            if (mView != null) {
                mView.showNote(note.getUserName());
            }
        };
    }
}
