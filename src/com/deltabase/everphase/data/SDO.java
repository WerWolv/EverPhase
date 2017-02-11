package com.deltabase.everphase.data;

import com.deltabase.everphase.api.Log;

import java.io.*;

public abstract class SDO implements Serializable {

    public void serialize() {
        try {
            File file = new File(System.getProperty("user.home") + "/everphase/data/" + getClass().getSimpleName() + ".sdo");
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);

            out.writeObject(this);
            out.close();

            Log.i("Serializer", "Data serialized successfully to " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SDO deserialize() {
        SDO sdo;
        try {
            File file = new File(System.getProperty("user.home") + "/everphase/data/" + getClass().getSimpleName() + ".sdo");
            file.createNewFile();
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileInputStream);

            sdo = (SDO) in.readObject();
            in.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            Log.i("Serializer", "Save file not found. Created a new one.");
            this.serialize();
            sdo = this.deserialize();
        }

        return sdo;
    }
}
