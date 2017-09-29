package com.controllers;

public interface PermissionListener {
    void onPermissionGranted(String permission);
    void onPermissionDenied(String permission);
}
