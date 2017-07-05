package de.minebench.syncinv.messenger;

import lombok.Getter;
import lombok.ToString;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;

/*
 * Copyright 2017 Phoenix616 All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation,  version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
@Getter
@ToString
public class Message {
    public static final int VERSION = 2;
    private final String sender;
    private final MessageType type;
    private final Queue<Object> data = new ArrayDeque<>();

    /**
     * A Message of a certain type. Optionally with some data
     * @param type      The type of the message
     * @param objects   The data, in the order that it should be send
     * @throws IllegalArgumentException when the amount of Objects given didn't match the MessageType requirements
     */
    public Message(String sender, MessageType type, Object... objects) {
        if (objects.length < type.getArgCount()) {
            throw new IllegalArgumentException(type + " requires at least " + type.getArgCount() + " arguments. Only " + objects.length + " were given!");
        }
        this.sender = sender;
        this.type = type;
        if (objects.length > 0) {
            Collections.addAll(data, objects);
        }
    }

    /**
     * Read the first object in this message
     * @return The first object
     */
    public Object read() {
        return data.poll();
    }

    /**
     * Generate a byte array out of the data of this message
     * @return          The generated byte array (starts with the sender
     *                  then the type ordinal, then the amount
     *                  of data being send and each data object);
     *                  an empty one if an error occurred
     */
    public byte[] toByteArray() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new BukkitObjectOutputStream(bos)) {
            out.writeInt(VERSION);
            out.writeUTF(getSender());
            out.writeUTF(getType().toString());
            out.writeInt(data.size());
            for (Object o : data) {
                out.writeObject(o);
            }
            out.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    /**
     * Get the message from a bytearray generated as described in the toByteArray method
     * @param bytes The bytes
     * @return      The Message object
     * @throws IOException
     * @throws IllegalArgumentException         When the message type is not supported
     * @throws ClassNotFoundException
     * @throws InvalidConfigurationException    If the data is invalid
     * @throws VersionMismatchException         If the received message is of a different version than it can accept
     */
    public static Message fromByteArray(byte[] bytes) throws IOException, IllegalArgumentException, ClassNotFoundException, InvalidConfigurationException, VersionMismatchException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInput in = new BukkitObjectInputStream(bis)) {
            int version = in.readInt();
            if (version != VERSION) {
                throw new VersionMismatchException(version, VERSION, "The received message is of version " + version + " while this plugin expects version " + VERSION);
            }
            String sender = in.readUTF();
            MessageType type = MessageType.valueOf(in.readUTF());
            Object[] data = new Object[in.readInt()];
            for (int i = 0; i < data.length; i++) {
                data[i] = in.readObject();
            }
            return new Message(sender, type, data);
        }
    }

}
