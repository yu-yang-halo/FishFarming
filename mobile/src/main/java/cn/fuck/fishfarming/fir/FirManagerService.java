package cn.fuck.fishfarming.fir;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;

/**
 * Created by Administrator on 2016/4/18.
 */
public class FirManagerService {
    public static PackageInfo getVersionInfo(Context ctx) {
        try {
            PackageManager manager = ctx.getPackageManager();
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            return info;
        } catch (Exception e) {
            return null;
        }
    }
    public static void checkUpdate(final Context ctx){

        FIR.checkForUpdateInFIR("11044c8cac8c136a31cb0b8ab5bd5162", new VersionCheckCallback() {
                @Override
                public void onSuccess(String versionJson) {
                    Gson gson = new Gson();
                    final FirVersion firVersion = gson.fromJson(versionJson, FirVersion.class);

                    PackageInfo packageInfo = getVersionInfo(ctx);

                    Log.i("fir", "check from fir.im success! "
                            + "\n" + firVersion + " packageInfo " + packageInfo.versionName + " " + packageInfo.versionCode);

                    if (firVersion.getBuild().equals(packageInfo.versionCode + "")
                            && firVersion.getVersionShort().equals(packageInfo.versionName)) {
                        Log.i("fir", "版本号一致,无需更新");
                    } else {

                            new AlertDialog.Builder(ctx)
                                    .setTitle("提示")
                                    .setMessage("发现新版本~")
                                    .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {


                                            Uri uri = Uri.parse(firVersion.getInstall_url());
                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                            ctx.startActivity(intent);

                                        }
                                    })
                                    .setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();
                        }


                }

                @Override
                public void onFail(Exception exception) {
                    Log.i("fir", "check fir.im fail! " + "\n" + exception.getMessage());
                }

                @Override
                public void onStart() {

                }

                @Override
                public void onFinish() {

                }
            });



    }
}
