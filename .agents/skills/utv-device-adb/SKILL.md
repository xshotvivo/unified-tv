---
name: utv-device-adb
description: Use for Android TV discovery, ADB connection, package inventory, log capture, app installation, reversible settings and device acceptance on Toshiba R700 or another declared Android device.
---
# UTV Device and ADB Operations

## Safe order
1. Confirm device identity, model, firmware/build, Android version, IP/connection method and authorization.
2. Capture baseline: adb devices, getprop summary, package list, storage, current launcher and relevant logs.
3. Back up every configuration/package state that may be changed. Verify backup files exist and are readable.
4. Prefer read-only inspection. Use reversible disable-user experiments only after dependency review; never uninstall system packages first.
5. Change one item at a time. Record exact command and output.
6. Verify behavior and rollback procedure; restore immediately if acceptance fails.

## Prohibited without explicit informed approval
Root, bootloader unlock, firmware flash, factory reset, system partition writes, mass package disable, deletion of user data, changes to DRM/HDCP/OTA/CEC/tuner services.

## R700 specifics
Treat Toshiba REGZA 75R700SF / Android 12 as a target, not proof of current connection. Reconfirm exact suffix, firmware and capabilities on device. Preserve display, audio, HDMI-CEC, DRM/HDCP, tuner, OTA and OEM control paths.

## Evidence
Command success is not proof of correct TV behavior. Record logs, screenshots or repeatable steps and rollback.
