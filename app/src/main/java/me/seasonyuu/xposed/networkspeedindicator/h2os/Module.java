package me.seasonyuu.xposed.networkspeedindicator.h2os;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.content.res.XResources;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import me.seasonyuu.xposed.networkspeedindicator.h2os.logger.Log;
import me.seasonyuu.xposed.networkspeedindicator.h2os.widget.PositionCallback1p2;
import me.seasonyuu.xposed.networkspeedindicator.h2os.widget.PositionCallbackOp2;
import me.seasonyuu.xposed.networkspeedindicator.h2os.widget.PositionCallback1p4;
import me.seasonyuu.xposed.networkspeedindicator.h2os.widget.PositionCallback2p5;
import me.seasonyuu.xposed.networkspeedindicator.h2os.widget.TrafficView;

public final class Module implements IXposedHookLoadPackage, IXposedHookInitPackageResources {

	private static final String TAG = Module.class.getSimpleName();

	private final View getClock() {
		if (trafficView == null || trafficView.mPositionCallback == null) {
			return null;
		}

		if (trafficView.mPositionCallback.getClockParent().findViewById(clock.getId()) != null) {
			return clock;
		}

		return null;
	}

	@Override
	public final void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
		try {
			if (!lpparam.packageName.equals(PKG_NAME_SYSTEM_UI)) {
				return;
			}
		} catch (Exception e) {
			Log.e(TAG, "handleLoadPackage failed: ", e);
			throw e;
		}
		try {
			Class<?> cClock = XposedHelpers.findClass("com.android.systemui.statusbar.policy.Clock",
					lpparam.classLoader);
			XposedBridge.hookAllMethods(cClock, "setTextColor", new XC_MethodHook() {
				@Override
				protected final void afterHookedMethod(final MethodHookParam param) throws Throwable {
					try {
						if (param.thisObject != getClock())
							return;

						if (trafficView != null && clock != null) {
							if (clock instanceof TextView) {
								trafficView.setTextColor(((TextView) clock).getCurrentTextColor());
							} else {
								// probably LinearLayout in VN ROM v14.1 (need
								// to search child elements to find correct text
								// color)
								Log.w(TAG, "clock is not a TextView, it is ", clock.getClass().getSimpleName());
								trafficView.setTextColor(Common.ANDROID_SKY_BLUE);
							}
						}
					} catch (Exception e) {
						Log.e(TAG, "afterHookedMethod (setTextColor) failed: ", e);
						throw e;
					}
				}
			});

			// we hook this method to follow alpha changes in kitkat
			Method setAlpha = XposedHelpers.findMethodBestMatch(cClock, "setAlpha", Float.class);
			XposedBridge.hookMethod(setAlpha, new XC_MethodHook() {
				@SuppressLint("NewApi")
				@Override
				protected final void afterHookedMethod(final MethodHookParam param) throws Throwable {
					try {
						if (param.thisObject != getClock())
							return;

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
							if (trafficView != null) {
								if (statusIcons != null) {
									trafficView.setAlpha(statusIcons.getAlpha());
								} else if (clock != null) {
									trafficView.setAlpha(clock.getAlpha());
								}
							}
						}
					} catch (Exception e) {
						Log.e(TAG, "afterHookedMethod (setAlpha) failed: ", e);
						throw e;
					}
				}
			});
		} catch (Exception e) {
			Log.e(TAG, "handleLoadPackage failure ignored: ", e);
		} catch (ClassNotFoundError e) {
			// Clock class not found, ignore
			Log.w(TAG, "handleLoadPackage failure ignored: ", e);
		} catch (NoSuchMethodError e) {
			// setAlpha method not found, ignore
			Log.w(TAG, "handleLoadPackage failure ignored: ", e);
		}
	}

	private TrafficView trafficView;
	private View clock;
	private View statusIcons;

	private static final String PKG_NAME_SYSTEM_UI = "com.android.systemui";

	private static final Map<String, String> mLayouts;

	static {
		Map<String, String> tmpMap = new HashMap<String, String>();
		tmpMap.put("tw_super_status_bar", "statusIcons");
		tmpMap.put("status_bar", "statusIcons");

		mLayouts = Collections.unmodifiableMap(tmpMap);
	}

	@Override
	public final void handleInitPackageResources(final InitPackageResourcesParam resparam) throws Throwable {
		try {
			if (!resparam.packageName.equals(PKG_NAME_SYSTEM_UI)) {
				return;
			}
			XResources res = resparam.res;

			final Entry<String, String> layoutInfo = findLayoutInfo(res);
			if (layoutInfo == null) {
				return;
			}

			res.hookLayout(PKG_NAME_SYSTEM_UI, "layout", layoutInfo.getKey(), new XC_LayoutInflated() {

				@Override
				public final void handleLayoutInflated(final LayoutInflatedParam liparam) throws Throwable {
					try {
						FrameLayout root = (FrameLayout) liparam.view;

						clock = root.findViewById(liparam.res.getIdentifier("clock", "id", PKG_NAME_SYSTEM_UI));
						statusIcons = root
								.findViewById(liparam.res.getIdentifier("statusIcons", "id", PKG_NAME_SYSTEM_UI));

						if (trafficView == null) {
							trafficView = new TrafficView(root.getContext());
							trafficView.clock = clock;
						}
						if (clock != null) {
							trafficView.setLayoutParams(clock.getLayoutParams());
							if (clock instanceof TextView) {
								trafficView.setTextColor(((TextView) clock).getCurrentTextColor());
							} else {
								// probably LinearLayout in VN ROM v14.1 (need
								// to search child elements to find correct text
								// color)
								Log.w(TAG, "clock is not a TextView, it is ", clock.getClass().getSimpleName());
								trafficView.setTextColor(Common.ANDROID_SKY_BLUE);
							}
						}
						trafficView.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

						Log.i(TAG, "find Clock succeed");

						Process p = null;
						String romVersion = "";
						try {
							p = new ProcessBuilder("/system/bin/getprop", "ro.rom.version").redirectErrorStream(true)
									.start();
							BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
							String line = "";
							while ((line = br.readLine()) != null) {
								romVersion = line;
							}
							Log.e(TAG, line);
							romVersion = romVersion.split("V", 2)[1];
							p.destroy();
						} catch (IOException e) {
							e.printStackTrace();
							Log.e(TAG, e.getStackTrace().toString());
						}
						if (romVersion != null) {
							if (romVersion.compareTo("2.5.0") >= 0 || romVersion.contains("OP3_H2_Open")) {
								Log.i(TAG, "PositionCallback: H2OS 2.5");
								trafficView.mPositionCallback = new PositionCallback2p5();
							} else if (romVersion.compareTo("1.4.0") >= 0) {
								Log.i(TAG, "PositionCallback: H2OS 1.4");
								trafficView.mPositionCallback = new PositionCallback1p4();
							} else if (romVersion.compareTo("1.2.0") >= 0) {
								Log.i(TAG, "PositionCallback: H2OS 1.2");
								String product = Build.PRODUCT;
								if (product.equals("OnePlus2"))
									trafficView.mPositionCallback = new PositionCallbackOp2();
								else
									trafficView.mPositionCallback = new PositionCallback1p2();
							}
						} else {
							Log.e(TAG, "ROM VERSION is null");
						}

						trafficView.mPositionCallback.setup(liparam, trafficView);
						trafficView.refreshPosition();
					} catch (Exception e) {
						Log.e(TAG, "handleLayoutInflated failed: ", e);
						throw e;
					}
				}
			});
		} catch (Exception e) {
			Log.e(TAG, "handleInitPackageResources failed: ", e);
			throw e;
		}
	}

	private static final Entry<String, String> findLayoutInfo(final XResources res) {
		Iterator<Entry<String, String>> iterator = mLayouts.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			if (res.getIdentifier(entry.getKey(), "layout", PKG_NAME_SYSTEM_UI) != 0
					&& res.getIdentifier(entry.getValue(), "id", PKG_NAME_SYSTEM_UI) != 0)
				return entry;
		}

		return null;
	}

}
