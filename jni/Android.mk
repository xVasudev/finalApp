LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
//OPENCV_LIB_TYPE:=STATIC
OPENCV_INSTALL_MODULES:=on

include /home/jay/Android_OpenCV/OpenCV-2.4.4-android-sdk/sdk/native/jni/OpenCV.mk

LOCAL_MODULE    := depth_magic
LOCAL_SRC_FILES := jni_part.cpp
LOCAL_LDLIBS +=  -L$(SYSROOT)/usr/lib -llog -ldl 


include $(BUILD_SHARED_LIBRARY)
