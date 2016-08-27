package me.seasonyuu.xposed.networkspeedindicator.h2os.widget;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.robv.android.xposed.callbacks.XC_LayoutInflated.LayoutInflatedParam;

public class PositionCallbackOp2 implements PositionCallback {
	private static final String PKG_NAME_SYSTEM_UI = "com.android.systemui";
	private View view;
	private ViewGroup mStatusBarContents;

	@Override
	public void setup(LayoutInflatedParam liparam, View v) {
		view = v;
		
		FrameLayout root = (FrameLayout) liparam.view;
		LinearLayout system_icon_area = (LinearLayout) root
				.findViewById(liparam.res.getIdentifier("system_icon_area", "id", PKG_NAME_SYSTEM_UI));
		mStatusBarContents = (ViewGroup) root
				.findViewById(liparam.res.getIdentifier("status_bar_contents", "id", PKG_NAME_SYSTEM_UI));
		TextView clock = (TextView) root.findViewById(liparam.res.getIdentifier("clock", "id", PKG_NAME_SYSTEM_UI));

		TrafficView trafficView = new TrafficView(root.getContext());
		trafficView.setLayoutParams(clock.getLayoutParams());
		trafficView.setSingleLine(true);
		trafficView.setTextColor(clock.getCurrentTextColor());
		trafficView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

		system_icon_area.addView(trafficView, 0);
	}

	@Override
	public void setLeft() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRight() {
		// TODO Auto-generated method stub

	}

	@Override
	public ViewGroup getClockParent() {
		// TODO Auto-generated method stub
		return mStatusBarContents;
	}

}
