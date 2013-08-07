################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../jni/backup_jni_part.cpp \
../jni/jni_part.cpp 

OBJS += \
./jni/backup_jni_part.o \
./jni/jni_part.o 

CPP_DEPS += \
./jni/backup_jni_part.d \
./jni/jni_part.d 


# Each subdirectory must supply rules for building sources it contributes
jni/%.o: ../jni/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -I"/platforms/android-9/arch-arm/usr/include" -I"/sources/cxx-stl/gnu-libstdc++/4.6/include" -I"/sources/cxx-stl/gnu-libstdc++/4.6/libs/armeabi-v7a/include" -I/home/jay/Android_OpenCV/OpenCV-2.4.4-android-sdk/sdk/native/jni/include -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '

jni/jni_part.o: ../jni/jni_part.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -I"/sources/cxx-stl/gnu-libstdc++/4.6/include" -I"/sources/cxx-stl/gnu-libstdc++/4.6/libs/armeabi-v7a/include" -I/home/jay/Android_OpenCV/OpenCV-2.4.4-android-sdk/sdk/native/jni/include -I/home/jay/Android_OpenCV/android-ndk-r8d/platforms/android-9/arch-arm/usr/include -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"jni/jni_part.d" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


