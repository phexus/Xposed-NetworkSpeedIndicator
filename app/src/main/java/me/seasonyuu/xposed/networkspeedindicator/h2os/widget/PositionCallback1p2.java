package me.seasonyuu.xposed.networkspeedindicator.h2os.widget;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import de.robv.android.xposed.callbacks.XC_LayoutInflated.LayoutInflatedParam;
import me.seasonyuu.xposed.networkspeedindicator.h2os.R;

/**
 * 实现氢OS 1.2以上的状态栏网速位置
 * 
 * @author seasonyuu
 *
 */
public class PositionCallback1p2 implements PositionCallback {

	private static final String PKG_NAME_SYSTEM_UI = "com.android.systemui";
	private LinearLayout mSystemIconArea;
	private RelativeLayout mStatusBarContents;
	private LinearLayout container;
	private View view;

	private static final int TRAFFIC_VIEW_CONTAINER_ID = R.id.traffic_view_container;

	@Override
	public void setup(final LayoutInflatedParam liparam, final View v) {
		view = v;

		FrameLayout root = (FrameLayout) liparam.view;

		mSystemIconArea = (LinearLayout) root
				.findViewById(liparam.res.getIdentifier("system_icon_area", "id", PKG_NAME_SYSTEM_UI));
		mStatusBarContents = (RelativeLayout) root
				.findViewById(liparam.res.getIdentifier("status_bar_contents", "id", PKG_NAME_SYSTEM_UI));

		container = new LinearLayout(root.getContext());
		container.setId(TRAFFIC_VIEW_CONTAINER_ID);
		container.setOrientation(LinearLayout.HORIZONTAL);
		container.setWeightSum(1);
		container.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		container.setVisibility(View.GONE);

		// mStatusBarContents.addView(container, 0);
		LinearLayout iconAreaInner = (LinearLayout) root
				.findViewById(liparam.res.getIdentifier("notification_icon_area_inner", "id", PKG_NAME_SYSTEM_UI));
		iconAreaInner.addView(container);
	}

	@Override
	public void setup(View clock, View v) {

	}

	@Override
	public void setLeft() {
		removeFromParent();

		container.addView(view);
		((TrafficView) view).setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		container.setVisibility(View.VISIBLE);
	}

	@Override
	public void setRight() {
		removeFromParent();

		mSystemIconArea.addView(view, 0);
		((TrafficView) view).setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
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
