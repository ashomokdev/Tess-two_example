package com.ashomok.tesseractsample.tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * This class in used only for API>=23
 * https://developer.android.com/training/permissions/requesting.html
 * Created by iuliia on 10/15/16.
 */

public class RequestPermissionsToolImpl implements RequestPermissionsTool {

    private static final String CONFIRMATION_DIALOG = "ConfirmationDialog";
    private static final String TAG = RequestPermissionsToolImpl.class.getSimpleName();
    private Activity activity;


    @Override
    public void requestPermissions(Activity activity, String[] permissions) {
        Map<Integer, String> permissionsMap = new HashMap<>();
        this.activity = activity;


        for (int i = 0; i < permissions.length; i++) {
            permissionsMap.put(i, permissions[i]);
        }

        for (Map.Entry<Integer, String> permission : permissionsMap.entrySet()) {
            if (!isPermissionGranted(activity, permission.getValue())) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission.getValue())) {

                    ConfirmationDialog.newInstance(permission.getKey(), permission.getValue()).
                            show(activity.getFragmentManager(), CONFIRMATION_DIALOG);
                }
                else {
                    ActivityCompat.requestPermissions(activity, permissions,
                            permission.getKey());
                    return;
                }
            }
        }
    }

    @Override
    public boolean isPermissionsGranted(Context context, String[] permissions) {

        for (String permission : permissions) {
            if (!isPermissionGranted(context, permission)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onPermissionDenied() {
        ErrorDialog.newInstance("Permission needs").
        show(activity.getFragmentManager(), CONFIRMATION_DIALOG);
    }

    private boolean isPermissionGranted(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context,
                permission)
                == PackageManager.PERMISSION_GRANTED;
    }


    /**
     * Shows OK/Cancel confirmation dialog about permission.
     */
    public static class ConfirmationDialog extends DialogFragment {

        private static final String ARG_PERMISSION = "permission";
        private static final String ARG_REQUEST_CODE = "request_code";

        public static ConfirmationDialog newInstance(int permissionKey, String permissionValue) {
            ConfirmationDialog dialog = new ConfirmationDialog();
            Bundle bundle = new Bundle();
            bundle.putString(ARG_PERMISSION, permissionValue);
            bundle.putInt(ARG_REQUEST_CODE, permissionKey);
            dialog.setArguments(bundle);
            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            return new AlertDialog.Builder(getActivity())
                    .setMessage("Please allow permission")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{getArguments().getString(ARG_PERMISSION)},
                                    getArguments().getInt(ARG_REQUEST_CODE));
                        }
                    })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getActivity(), "Not available", Toast.LENGTH_SHORT).show();
                                }
                            })
                    .create();
        }
    }

    /**
     * Shows an error message dialog.
     */
    public static class ErrorDialog extends DialogFragment {

        private static final String ARG_MESSAGE = "message";

        public static ErrorDialog newInstance(String message) {
            ErrorDialog dialog = new ErrorDialog();
            Bundle args = new Bundle();
            args.putString(ARG_MESSAGE, message);
            dialog.setArguments(args);
            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Activity activity = getActivity();
            return new AlertDialog.Builder(activity)
                    .setMessage(getArguments().getString(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//nothing
                        }
                    })
                    .create();
        }

    }


}
