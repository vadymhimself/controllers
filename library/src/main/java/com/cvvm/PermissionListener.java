package com.cvvm;

public interface PermissionListener {
    void onPermissionGranted(String permission);
    void onPermissionDenied(String permission);
}
