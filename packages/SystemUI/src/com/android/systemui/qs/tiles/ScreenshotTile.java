/*
 * Copyright (C) 2017 ABC rom
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

package com.android.systemui.qs.tiles;

import android.content.Intent;
import android.os.RemoteException;
import android.text.format.DateUtils;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;

import com.android.internal.util.superior.SuperiorUtils;
import com.android.systemui.R;
import com.android.systemui.plugins.qs.QSTile.BooleanState;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.tileimpl.QSTileImpl;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;

import javax.inject.Inject;

public class ScreenshotTile extends QSTileImpl<BooleanState> {

    @Inject
    public ScreenshotTile(QSHost host) {
        super(host);
    }

    @Override
    public void handleSetListening(boolean listening) {
    }

    @Override
    public BooleanState newTileState() {
        return new BooleanState();
    }

    @Override
    protected void handleClick() {
        mHost.collapsePanels();
        mHandler.postDelayed(() -> takeScreenshot(false), DateUtils.SECOND_IN_MILLIS);
    }

    @Override
    public void handleLongClick() {
        mHost.collapsePanels();
        mHandler.postDelayed(() -> takeScreenshot(true), DateUtils.SECOND_IN_MILLIS);
    }

    @Override
    public Intent getLongClickIntent() {
        return null;
    }

    @Override
    public CharSequence getTileLabel() {
        return mContext.getString(R.string.quick_settings_screenshot_label);
    }

    @Override
    protected void handleUpdateState(BooleanState state, Object arg) {
        state.icon = ResourceIcon.get(R.drawable.ic_qs_screenshot);
        state.label = mContext.getString(R.string.quick_settings_screenshot_label);
    }

    private void takeScreenshot(final boolean partial) {
        final int type = partial
                ? WindowManager.TAKE_SCREENSHOT_SELECTED_REGION
                : WindowManager.TAKE_SCREENSHOT_FULLSCREEN;

        try {
            WindowManagerGlobal.getWindowManagerService().takeAlternativeScreenshot(type);
        } catch (RemoteException e) {
            // Do nothing
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.SUPERIOR;
    }

}
