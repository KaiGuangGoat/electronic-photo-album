LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE  := videomerger
LOCAL_SRC_FILES := videomerger/videoMerger.c videomerger/ffmpeg.c videomerger/cmdutil.c
LOCAL_SHARED_LIBRARIES := avutil avformat avcodec

#包含ffmpeg目录下的所有头文件
LOCAL_C_INCLUDES := $(LOCAL_PATH)/ffmpeg
LOCAL_C_INCLUDES += $(shell ls -FR $(LOCAL_C_INCLUDES) | grep $(LOCAL_PATH)/$ )
LOCAL_C_INCLUDES := $(LOCAL_C_INCLUDES:$(LOCAL_PATH)/%:=$(LOCAL_PATH)/%)

LOCAL_CFLAGS += -g -Iffmpeg -Ivideokit -Wno-deprecated-declarations 
LOCAL_LDLIBS += -llog -lz #$(FFMPEG_LIBS) x264/libx264.a
include $(BUILD_SHARED_LIBRARY) 


#swscale module
include $(CLEAR_VARS)
LOCAL_MODULE    := swscale
LOCAL_ARM_MODE := arm
LOCAL_CFLAGS := -DHAVE_AV_CONFIG_H
LOCAL_SHARED_LIBRARIES := avutil
LOCAL_LDFLAGS := 
#include $(LOCAL_PATH)/../lib$(LOCAL_MODULE)_files.mk
include $(BUILD_SHARED_LIBRARY) 
#end of swscale module

#avutil module
include $(CLEAR_VARS)
LOCAL_MODULE := avutil
LOCAL_ARM_MODE := arm
LOCAL_CFLAGS := -DHAVE_AV_CONFIG_H
#include $(LOCAL_PATH)/../lib$(LOCAL_MODULE)_files.mk
include $(BUILD_SHARED_LIBRARY)
#end of avutil module

#avformat module
include $(CLEAR_VARS)
LOCAL_MODULE := avformat
LOCAL_ARM_MODE := arm
LOCAL_CFLAGS := -DHAVE_AV_CONFIG_H
#include $(LOCAL_PATH)/../lib$(LOCAL_MODULE)_files.mk
LOCAL_SHARED_LIBRARIES := avutil avcodec
LOCAL_LDFLAGS := -L$(SYSROOT)/usr/lib -lz
include $(BUILD_SHARED_LIBRARY)
#end of avformat module

#avcodec module
include $(CLEAR_VARS)
LOCAL_MODULE := avcodec
LOCAL_ARM_MODE := arm
LOCAL_CFLAGS := -DHAVE_AV_CONFIG_H
#include $(LOCAL_PATH)/../lib$(LOCAL_MODULE)_files.mk
LOCAL_SHARED_LIBRARIES := avutil
LOCAL_LDFLAGS := -L$(SYSROOT)/usr/lib -lz -lm
include $(BUILD_SHARED_LIBRARY)
#end of avcodec module









# These need to be in the right order
#FFMPEG_LIBS := $(addprefix ffmpeg/, \
#				 libavdevice/avdevice.c \
#				 libavformat/avformat.c \
#				 libavcodec/avcodec.c \
#				 libavfilter/avfilter.c \
#				 libswscale/swscale.c \
#				 libavutil/avutil.c \
#				 libpostproc/postproc.c )
## ffmpeg uses its own deprecated functions liberally, so turn off that annoying noise
#LOCAL_CFLAGS += -g -Iffmpeg -Ivideokit -Wno-deprecated-declarations 
#LOCAL_LDLIBS += -llog -lz #$(FFMPEG_LIBS) x264/libx264.a
#LOCAL_SRC_FILES := videomerger/videoMerger.c videomerger/ffmpeg.c videomerger/cmdutils.c
#include $(BUILD_SHARED_LIBRARY)
#
#
#include $(CLEAR_VARS)
#LOCAL_MODULE  := ffmpeg
#FFMPEG_LIBS := $(addprefix ffmpeg/, \
#				 libavdevice/avdevice.c \
#				 libavformat/avformat.c \
#				 libavcodec/avcodec.c \
#				 libavfilter/avfilter.c \
#				 libswscale/swscale.c \
#				 libavutil/avutil.c \
#				 libpostproc/postproc.c )
#LOCAL_CFLAGS += -g -Iffmpeg -Ivideokit -Wno-deprecated-declarations 
#LOCAL_LDLIBS += -llog -lz #$(FFMPEG_LIBS) x264/libx264.a
#LOCAL_SRC_FILES := ffmpeg/ffmpeg.c ffmpeg/cmdutils.c
#include $(BUILD_EXECUTABLE)