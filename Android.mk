LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := $(call all-subdir-java-files)
LOCAL_PACKAGE_NAME := AmbientPlayHistoryProvider
LOCAL_MODULE_TAGS := optional
LOCAL_SDK_VERSION := system_current
include $(BUILD_PACKAGE)