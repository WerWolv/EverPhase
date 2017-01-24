package com.werwolv.game.audio;

import com.werwolv.game.entity.EntityPlayer;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.*;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.libc.Stdlib;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.openal.ALC10.*;

public class AudioHelper {

    public static long context, device;
    public static Map<String, Integer> audioFiles = new HashMap<>();

    public static void createContext() {
        String defaultDeviceName = ALC10.alcGetString(0, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
        device = alcOpenDevice(defaultDeviceName);
        context = alcCreateContext(device, new int[]{ 0 });
        alcMakeContextCurrent(context);

        ALCapabilities alCapabilities = AL.createCapabilities(ALC.createCapabilities(device));

        if(!alCapabilities.OpenAL10) {
            System.err.println("OpenGL 1.0 isn't supported!");
        }

        System.out.println("OpenAL: " + AL10.alGetString(AL10.AL_VERSION));
    }

    public static void loadSoundFile(String fileName) {
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
        audioFiles.put(fileName, bufferPointer);
    }

    public static void setListener(EntityPlayer player) {
        AL10.alListener3f(AL10.AL_POSITION, player.getPosition().x(), player.getPosition().y(), player.getPosition().z());

        FloatBuffer listenerPosition = BufferUtils.createFloatBuffer( 3 ).put(
                new float[] { 0.0f, 0.0f, 0.0f } );
        FloatBuffer listenerOrientation = BufferUtils.createFloatBuffer( 6 ).put (
                new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f } );
        FloatBuffer listenerVelocity = BufferUtils.createFloatBuffer( 3 ).put (
                new float[] { 0.0f, 0.0f, 0.0f } );

        listenerPosition.flip();
        listenerOrientation.flip();
        listenerVelocity.flip();

        float xOffset = player.getPosition().x() - listenerPosition.get( 0 );
        float yOffset = player.getPosition().y() - listenerPosition.get( 1 );
        float zOffset = player.getPosition().z() - listenerPosition.get( 2 );

        listenerPosition.put( 0, player.getPosition().x() );
        listenerPosition.put( 1, player.getPosition().y() );
        listenerPosition.put( 2, player.getPosition().z() );

        AL10.alListenerfv( AL10.AL_POSITION, listenerPosition );

        listenerOrientation.put( 0, listenerOrientation.get( 0 ) + xOffset );
        listenerOrientation.put( 1, listenerOrientation.get( 1 ) + yOffset );
        listenerOrientation.put( 2, listenerOrientation.get( 2 ) + zOffset );

        AL10.alListenerfv( AL10.AL_ORIENTATION, listenerOrientation );
        float xOffsetAngle = -1.0f * (float) Math.sin(player.getYaw());
        float zOffsetAngle = -1.0f * (float) Math.cos(player.getYaw());
        listenerOrientation.put( 0, listenerPosition.get( 0 ) + xOffsetAngle);
        listenerOrientation.put( 2, listenerPosition.get( 2 ) + zOffsetAngle);
        AL10.alListenerfv( AL10.AL_ORIENTATION, listenerOrientation );
    }

    public static void playSound(String fileName) {
        int sourcePointer = AL10.alGenSources();
        AL10.alSource3f(sourcePointer, AL10.AL_POSITION, 0,0,0);
        AL10.alSource3f(sourcePointer, AL10.AL_VELOCITY, 1F,1F,1F);
        AL10.alSourcei(sourcePointer, AL10.AL_BUFFER, audioFiles.get(fileName));
        AL10.alSourcePlay(sourcePointer);

    }

    public static void clean() {
        alcDestroyContext(context);
        alcCloseDevice(device);
    }
}
