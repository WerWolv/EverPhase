package com.deltabase.everphase.engine.audio;

import com.deltabase.everphase.engine.toolbox.Maths;
import com.deltabase.everphase.entity.EntityPlayer;
import org.joml.Matrix4f;
import org.lwjgl.openal.*;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.libc.Stdlib;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.ALC10.*;

public class AudioHelper {

    private static long context, device;

    public static void createContext() {
        String defaultDeviceName = ALC10.alcGetString(0, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
        device = alcOpenDevice(defaultDeviceName);
        context = alcCreateContext(device, new int[]{ 0 });
        alcMakeContextCurrent(context);

        ALCapabilities alCapabilities = AL.createCapabilities(ALC.createCapabilities(device));

        AL10.alDistanceModel(AL11.AL_EXPONENT_DISTANCE);

        if(!alCapabilities.OpenAL10) {
            System.err.println("OpenAL 1.0 isn't supported!");
        }

        System.out.println("OpenAL: " + AL10.alGetString(AL10.AL_VERSION));
    }

    public static int loadSoundFile(String fileName) {
        MemoryStack.stackPush();
        IntBuffer channelsBuffer = MemoryStack.stackMallocInt(1);
        MemoryStack.stackPush();
        IntBuffer sampleRateBuffer = MemoryStack.stackMallocInt(1);

        ShortBuffer rawAudioBuffer = STBVorbis.stb_vorbis_decode_filename("res/sounds/" + fileName + ".ogg", channelsBuffer, sampleRateBuffer);

        int channels = channelsBuffer.get();
        int sampleRate = sampleRateBuffer.get();

        MemoryStack.stackPop();
        MemoryStack.stackPop();

        int format = -1;
        if(channels == 1)
            format = AL10.AL_FORMAT_MONO16;
        else if(channels == 2)
            format = AL10.AL_FORMAT_STEREO16;

        int bufferPointer = AL10.alGenBuffers();

        AL10.alBufferData(bufferPointer, format, rawAudioBuffer, sampleRate);

        Stdlib.free(rawAudioBuffer);

        return bufferPointer;
    }

    public static void setListener(EntityPlayer player) {
        AL10.alListener3f(AL10.AL_POSITION, player.getPosition().x(), player.getPosition().y(), player.getPosition().z());
        AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);

        Matrix4f absMatrix = Maths.createTransformationMatrix(player.getPosition(), player.getYaw(), player.getPitch(), player.getRoll(), 1.0F);

        AL10.alListenerfv(AL10.AL_ORIENTATION, new float[] { absMatrix.m00(), absMatrix.m10(), absMatrix.m20(), absMatrix.m01(), absMatrix.m11(), absMatrix.m21() });
    }

    public static void clean() {
        alcDestroyContext(context);
        alcCloseDevice(device);
    }
}
