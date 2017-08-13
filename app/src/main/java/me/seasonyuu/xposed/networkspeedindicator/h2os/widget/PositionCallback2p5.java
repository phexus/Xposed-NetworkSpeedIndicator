package me.seasonyuu.xposed.networkspeedindicator.h2os.widget;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import de.robv.android.xposed.callbacks.XC_LayoutInflated.LayoutInflatedParam;
import me.seasonyuu.xposed.networkspeedindicator.h2os.Utils;

/**
 * 实现氢OS 2.5的状态栏网速位置
 * 
 * @author seasonyuu
 *
 */
public class PositionCallback2p5 implements PositionCallback {
	private static final String PKG_NAME_SYSTEM_UI = "com.android.systemui";
	private LinearLayout mSystemIconArea;
	private LinearLayout mStatusBarContents;
	private LinearLayout container;
	private View view;

	@Override
	public void setup(final LayoutInflatedParam liparam, final View v) {
		view = v;

		FrameLayout root = (FrameLayout) liparam.view;

		mSystemIconArea = (LinearLayout) root
				.findViewById(liparam.res.getIdentifier("system_icon_area", "id", PKG_NAME_SYSTEM_UI));
		mStatusBarContents = (LinearLayout) root
				.findViewById(liparam.res.getIdentifier("status_bar_contents", "id", PKG_NAME_SYSTEM_UI));

		container = new LinearLayout(root.getContext());
		container.setOrientation(LinearLayout.HORIZONTAL);
		container.setWeightSum(1);
		container.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		container.setVisibility(View.GONE);
		mStatusBarContents.addView(container, 0);
	}

	@Override
	public void setup(View root, View v) {
		view = v;

		mSystemIconArea = Utils.findViewById(root,"system_icon_area",PKG_NAME_SYSTEM_UI);
		mStatusBarContents = Utils.findViewById(root,"status_bar_contents",PKG_NAME_SYSTEM_UI);

		container = new LinearLayout(root.getContext());
		container.setOrientation(LinearLayout.HORIZONTAL);
		container.setWeightSum(1);
		container.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
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

		mSystemIconArea.addView(view, 0);
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
