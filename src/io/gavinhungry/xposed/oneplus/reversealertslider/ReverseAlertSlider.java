package io.gavinhungry.xposed.oneplus.reversealertslider;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;


public class ReverseAlertSlider implements IXposedHookLoadPackage {
  public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
    if (lpparam.packageName.equals("com.android.systemui")) {
      try {

        XposedHelpers.findAndHookMethod("com.android.systemui.statusbar.policy.ZenModeControllerImpl", lpparam.classLoader,
          "setZen", int.class, new XC_MethodHook() {

          @Override
          protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            // 1 - No interruptions
            // 2 - Priority interruptions only
            // 3 - All notifications
            int state = (Integer) param.args[0];

            if (state == 1) {
              param.args[0] = 3;
            } else if (state == 3) {
              param.args[0] = 1;
            }
          }
        });

        XposedHelpers.findAndHookMethod("com.android.systemui.volume.ZenModePanel", lpparam.classLoader,
          "handleUpdateZen", int.class, XC_MethodReplacement.DO_NOTHING);

        XposedHelpers.findAndHookMethod("com.android.systemui.volume.VolumePanel", lpparam.classLoader,
          "setZenPanelVisible", boolean.class, new XC_MethodHook() {

          @Override
          protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            param.args[0] = false;
          }
        });

        XposedHelpers.findAndHookMethod("com.android.systemui.volume.ZenToast", lpparam.classLoader,
          "handleShow", int.class, String.class, XC_MethodReplacement.DO_NOTHING);

      } catch(Throwable t) {
        XposedBridge.log(t);
      }
    }
  }
}
