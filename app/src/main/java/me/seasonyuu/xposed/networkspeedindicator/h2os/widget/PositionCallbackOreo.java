package me.seasonyuu.xposed.networkspeedindicator.h2os.widget;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import me.seasonyuu.xposed.networkspeedindicator.h2os.Utils;
import me.seasonyuu.xposed.networkspeedindicator.h2os.logger.Log;

/**
 * for AOSP like Oreo ROM
 * Created by seaso on 2018/1/9.
 */

public class PositionCallbackOreo implements PositionCallback {

	private static final String PKG_NAME_SYSTEM_UI = "com.android.systemui";
	private LinearLayout mSystemIconArea;
	private LinearLayout mStatusBarContents;
	private LinearLayout container;
	private View view;

	@Override
	public void setup(final XC_LayoutInflated.LayoutInflatedParam liparam, final View v) {
		view = v;

		FrameLayout root = (FrameLayout) liparam.view;

		mSystemIconArea = (LinearLayout) root.findViewById(liparam.res.getIdentifier("system_icon_area", "id",
				PKG_NAME_SYSTEM_UI));
		mStatusBarContents = (LinearLayout) root.findViewById(liparam.res.getIdentifier("status_bar_contents", "id",
				PKG_NAME_SYSTEM_UI));

		container = new LinearLayout(root.getContext());
		container.setOrientation(LinearLayout.HORIZONTAL);
		container.setWeightSum(1);
		container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
		container.setVisibility(View.GONE);
		mStatusBarContents.addView(container, 0);
	}

	public void setup(View root, View trafficView) {
		Log.d("Oreo", "Setup enter");
		view = trafficView;

		mSystemIconArea = Utils.findViewById(root, "system_icon_area", PKG_NAME_SYSTEM_UI);
		mStatusBarContents = Utils.findViewById(root, "status_bar_contents", PKG_NAME_SYSTEM_UI);

		if (mStatusBarContents == null && mSystemIconArea != null)
			mStatusBarContents = (LinearLayout) mSystemIconArea.getParent();

		container = new LinearLayout(root.getContext());
		container.setOrientation(LinearLayout.HORIZONTAL);
		container.setWeightSum(1);
		container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
		container.setVisibility(View.GONE);
		mStatusBarContents.addView(container, 0);
	}

	@Override
	public void setLeft() {
		removeFromParent();

		container.addView(view);
		container.setVisibility(View.VISIBLE);
	}

	@Override
	public void setRight() {
		removeFromParent();

		mSystemIconArea.addView(view, 0,
				new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
		container.setVisibility(View.GONE);
	}

	private final void removeFromParent() {
		if (view.getParent() != null) {
			((ViewGroup) view.getParent()).removeView(view);
		}
	}

	@Override
	public ViewGroup getClockParent() {
		return mStatusBarContents;

	}
}
