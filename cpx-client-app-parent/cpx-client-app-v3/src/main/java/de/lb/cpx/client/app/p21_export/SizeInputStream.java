/* 
 * Copyright (c) 2019 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.p21_export;

import java.io.IOException;
import java.io.InputStream;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;

/**
 *
 * @author niemeier
 */
public class SizeInputStream extends InputStream {

    // The InputStream to read bytes from
    private InputStream in = null;

    // The number of bytes that can be read from the InputStream
    private int size = 0;

    // The number of bytes that have been read from the InputStream
    private final ReadOnlyIntegerWrapper bytesRead = new ReadOnlyIntegerWrapper(0);
    private BooleanProperty stopProperty;
    private long lastUpdate = 0L;
    private boolean closed;
    private int totalBytesRead = 0;

//    public SizeInputStream(InputStream in, int size) {
//        this.in = in;
//        this.size = size;
//    }
    public void init(InputStream in, int size, BooleanProperty pStopProperty) {
        this.in = in;
        this.size = size;
        this.stopProperty = pStopProperty;
    }

    @Override
    public int available() {
        return (size - bytesRead.get());
    }

    public int size() {
        return size;
    }

    public ReadOnlyIntegerProperty bytesReadProperty() {
        return bytesRead.getReadOnlyProperty();
    }

    public int bytesRead() {
        return bytesRead.get();
    }

    @Override
    public int read() throws IOException {
//        if (stopProperty.get()) {
//            LOG.log(Level.INFO, "Stopping stream");
//            return 0;
//        }
        int b = in.read();
        if (b != -1) {
            //bytesRead++;
//            bytesRead.add(1);
            addBytesRed(1);
        }
        return b;
    }

    @Override
    public int read(byte[] b) throws IOException {
//        if (stopProperty.get()) {
//            LOG.log(Level.INFO, "Stopping stream");
//            return 0;
//        }
        int read = in.read(b);
//        bytesRead.add(read);
        addBytesRed(read);
        return read;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
//        if (stopProperty.get()) {
//            LOG.log(Level.INFO, "Stopping stream");
//            return 0;
//        }
        int read = in.read(b, off, len);
        //bytesRead += read;
        addBytesRed(read);
        //bytesRead.add(read);
        return read;
    }

    private void addBytesRed(final int read) {
        totalBytesRead += read;
        final int updatesPerSecond = 10;
        long now = System.currentTimeMillis() / (1000L / updatesPerSecond);
        if (lastUpdate == 0L || lastUpdate != now) {
            //don't update on each change. Limit to 10 updates per second
            bytesRead.set(totalBytesRead);
            lastUpdate = now;
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
        if (in != null) {
            in.close();
        }
        closed = true;
    }

    public boolean isClosed() {
        return closed;
    }
}
