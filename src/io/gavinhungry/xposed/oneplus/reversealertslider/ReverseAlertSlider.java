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

        XposedHelpers.findAndHookMethod("com.android.systemui.volume.ZenToast", lpparam.classLoader,
          "handleShow", int.class, String.class, XC_MethodReplacement.DO_NOTHING);

        XposedHelpers.findAndHookMethod("com.android.systemui.statusbar.policy.ZenModeControllerImpl", lpparam.classLoader,
          "setZen", int.class, new XC_MethodHook() {

          @Override
          protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            // 1 - No interruptions
            // 2 - Priority interruptions only
            // 3 - All notifications
            if (param.args[0].equals(1)) {
              param.args[0] = 3;
            } else if (param.args[0].equals(3)) {
              param.args[0] = 1;
            }
          }
        });

      } catch(Throwable t) {
        XposedBridge.log(t);
      }
    }
  }
}
