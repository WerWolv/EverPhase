package com.deltabase.everphase.audio;

import org.lwjgl.openal.AL10;

public class SoundSource {

    private int sourceID;
    private int bufferID;

    public SoundSource(String fileName, float volume, float pitch, boolean background) {
        sourceID = AL10.alGenSources();
        bufferID = AudioHelper.loadSoundFile(fileName);
        AL10.alSourcef(sourceID, AL10.AL_GAIN, volume);
        AL10.alSourcef(sourceID, AL10.AL_PITCH, pitch);
        AL10.alSource3f(sourceID, AL10.AL_POSITION, 0, 0, 0);

        AL10.alSourcef(sourceID, AL10.AL_ROLLOFF_FACTOR, background ? 0 : 1);
        AL10.alSourcef(sourceID, AL10.AL_REFERENCE_DISTANCE, 6);
        AL10.alSourcef(sourceID, AL10.AL_MAX_DISTANCE, 15);

    }

    public SoundSource play() {
        stop();
        AL10.alSourcei(sourceID, AL10.AL_BUFFER, bufferID);
        AL10.alSourcePlay(sourceID);

        return this;
    }

    public SoundSource pause() {
        AL10.alSourcei(sourceID, AL10.AL_BUFFER, bufferID);
        AL10.alSourcePause(sourceID);

        return this;
    }

    public SoundSource stop() {
        AL10.alSourceStop(sourceID);

        return this;
    }

    public void delete() {
        stop();
        AL10.alDeleteSources(sourceID);
    }

    public SoundSource setPosition(float x, float y, float z) {
        AL10.alSource3f(sourceID, AL10.AL_POSITION, x, y, z);

        return this;
    }

    public SoundSource setVelocity(float x, float y, float z) {
        AL10.alSource3f(sourceID, AL10.AL_VELOCITY, x, y, z);

        return this;
    }

    public SoundSource setLooping(boolean looping) {
        AL10.alSourcei(sourceID, AL10.AL_LOOPING, looping ? AL10.AL_TRUE : AL10.AL_FALSE);

        return this;
    }

    public boolean isPlaying() {
        return AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
    }

}
