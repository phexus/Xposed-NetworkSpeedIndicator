package me.seasonyuu.xposed.networkspeedindicator.h2os.widget;

import android.view.View;
import android.view.ViewGroup;
import de.robv.android.xposed.callbacks.XC_LayoutInflated.LayoutInflatedParam;

public interface PositionCallback {
	void setup(LayoutInflatedParam liparam, View v);
	void setup(View root, View v);
	void setLeft();
	void setRight();
	ViewGroup getClockParent();
}
