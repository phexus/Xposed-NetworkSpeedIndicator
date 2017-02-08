package me.seasonyuu.xposed.networkspeedindicator.h2os.widget;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.robv.android.xposed.callbacks.XC_LayoutInflated;

/**
 * position callback for miui8
 * <p>
 * Created by seasonyuu on 2017/1/11.
 */
public class PositionCallbackMiui8 implements PositionCallback {

	private static final String PKG_NAME_SYSTEM_UI = "com.android.systemui";
	private ViewGroup mSystemIconArea;
	private ViewGroup mStatusBarContents;
	private LinearLayout container;
	private View view;

	@Override
	public void setup(final XC_LayoutInflated.LayoutInflatedParam liparam, final View v) {
		view = v;

		FrameLayout root = (FrameLayout) liparam.view;

		mSystemIconArea = (ViewGroup) root.findViewById(liparam.res.getIdentifier("statusbar_icon", "id",
				PKG_NAME_SYSTEM_UI));

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
		layoutParams.addRule(RelativeLayout.END_OF, liparam.res.getIdentifier("notification_icon_area", "id",
				PKG_NAME_SYSTEM_UI));

		mStatusBarContents = (ViewGroup) root.findViewById(liparam.res.getIdentifier("icons", "id",
				PKG_NAME_SYSTEM_UI));

		container = new LinearLayout(root.getContext());
		container.setOrientation(LinearLayout.HORIZONTAL);
		container.setWeightSum(1);
		container.setLayoutParams(layoutParams);
		container.setVisibility(View.GONE);
		mStatusBarContents.addView(container, 0);
	}

	@Override
	public void setLeft() {
		removeFromParent();

		container.addView(view);
		((TextView) view).setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		container.setVisibility(View.VISIBLE);
	}

	@Override
	public void setRight() {
		removeFromParent();

		((TextView) view).setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		mSystemIconArea.addView(view, 1);
		container.setVisibility(View.GONE);
	}

	private void removeFromParent() {
		if (view.getParent() != null) {
			((ViewGroup) view.getParent()).removeView(view);
		}
	}

	@Override
	public ViewGroup getClockParent() {
		return mStatusBarContents;

	}
}