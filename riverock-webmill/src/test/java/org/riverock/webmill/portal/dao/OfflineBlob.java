package org.riverock.webmill.portal.dao;

import java.sql.Blob;
import java.sql.SQLException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.StringTokenizer;

/**
 * User: SMaslyukov
 * Date: 24.08.2007
 * Time: 17:36:44
 */
public class OfflineBlob implements Blob {

    private byte[] bytes=null;

    public OfflineBlob() {
    }

    public OfflineBlob(String data) {
        setData(data);
    }

    public void setData(String data) {
        if (data==null) {
            throw new IllegalStateException("Blob data is null");
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        for (StringTokenizer stringTokenizer = new StringTokenizer(data, " "); stringTokenizer.hasMoreTokens();) {
            String value = stringTokenizer.nextToken();
            os.write(Byte.parseByte(value, 16));
        }
        bytes = os.toByteArray();
    }

    public OfflineBlob(byte[] data) {
        if (data==null) {
            throw new IllegalStateException("Blob data is null");
        }
        this.bytes = data;
    }

    /**
     * Returns the number of bytes in the <code>BLOB</code> value
     * designated by this <code>Blob</code> object.
     *
     * @return length of the <code>BLOB</code> in bytes
     * @throws java.sql.SQLException if there is an error accessing the
     *                               length of the <code>BLOB</code>
     * @since 1.2
     */
    public long length() throws SQLException {
        return bytes.length;
    }

    /**
     * Retrieves all or part of the <code>BLOB</code>
     * value that this <code>Blob</code> object represents, as an array of
     * bytes.  This <code>byte</code> array contains up to <code>length</code>
     * consecutive bytes starting at position <code>pos</code>.
     *
     * @param pos    the ordinal position of the first byte in the
     *               <code>BLOB</code> value to be extracted; the first byte is at
     *               position 1
     * @param length the number of consecutive bytes to be copied
     * @return a byte array containing up to <code>length</code>
     *         consecutive bytes from the <code>BLOB</code> value designated
     *         by this <code>Blob</code> object, starting with the
     *         byte at position <code>pos</code>
     * @throws java.sql.SQLException if there is an error accessing the
     *                               <code>BLOB</code> value
     * @see #setBytes
     * @since 1.2
     */
    public byte[] getBytes(long pos, int length) throws SQLException {
        byte[] result = new byte[length];
        System.arraycopy(bytes, (int)pos-1, result, 0, length);
        return result;
    }

    /**
     * Retrieves the <code>BLOB</code> value designated by this
     * <code>Blob</code> instance as a stream.
     *
     * @return a stream containing the <code>BLOB</code> data
     * @throws java.sql.SQLException if there is an error accessing the
     *                               <code>BLOB</code> value
     * @see #setBinaryStream
     * @since 1.2
     */
    public InputStream getBinaryStream() throws SQLException {
        ByteArrayInputStream result = new ByteArrayInputStream(bytes);
        return result;
    }

    /**
     * Retrieves the byte position at which the specified byte array
     * <code>pattern</code> begins within the <code>BLOB</code>
     * value that this <code>Blob</code> object represents.  The
     * search for <code>pattern</code> begins at position
     * <code>start</code>.
     *
     * @param pattern the byte array for which to search
     * @param start   the position at which to begin searching; the
     *                first position is 1
     * @return the position at which the pattern appears, else -1
     * @throws java.sql.SQLException if there is an error accessing the
     *                               <code>BLOB</code>
     * @since 1.2
     */
    public long position(byte pattern[], long start) throws SQLException {
        throw new IllegalStateException("Not implemented");
    }

    /**
     * Retrieves the byte position in the <code>BLOB</code> value
     * designated by this <code>Blob</code> object at which
     * <code>pattern</code> begins.  The search begins at position
     * <code>start</code>.
     *
     * @param pattern the <code>Blob</code> object designating
     *                the <code>BLOB</code> value for which to search
     * @param start   the position in the <code>BLOB</code> value
     *                at which to begin searching; the first position is 1
     * @return the position at which the pattern begins, else -1
     * @throws java.sql.SQLException if there is an error accessing the
     *                               <code>BLOB</code> value
     * @since 1.2
     */
    public long position(Blob pattern, long start) throws SQLException {
        throw new IllegalStateException("Not implemented");
    }

    /**
     * Writes the given array of bytes to the <code>BLOB</code> value that
     * this <code>Blob</code> object represents, starting at position
     * <code>pos</code>, and returns the number of bytes written.
     *
     * @param pos   the position in the <code>BLOB</code> object at which
     *              to start writing
     * @param bytes the array of bytes to be written to the <code>BLOB</code>
     *              value that this <code>Blob</code> object represents
     * @return the number of bytes written
     * @throws java.sql.SQLException if there is an error accessing the
     *                               <code>BLOB</code> value
     * @see #getBytes
     * @since 1.4
     */
    public int setBytes(long pos, byte[] bytes) throws SQLException {
        throw new IllegalStateException("Not implemented");
//        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Writes all or part of the given <code>byte</code> array to the
     * <code>BLOB</code> value that this <code>Blob</code> object represents
     * and returns the number of bytes written.
     * Writing starts at position <code>pos</code> in the <code>BLOB</code>
     * value; <code>len</code> bytes from the given byte array are written.
     *
     * @param pos    the position in the <code>BLOB</code> object at which
     *               to start writing
     * @param bytes  the array of bytes to be written to this <code>BLOB</code>
     *               object
     * @param offset the offset into the array <code>bytes</code> at which
     *               to start reading the bytes to be set
     * @param len    the number of bytes to be written to the <code>BLOB</code>
     *               value from the array of bytes <code>bytes</code>
     * @return the number of bytes written
     * @throws java.sql.SQLException if there is an error accessing the
     *                               <code>BLOB</code> value
     * @see #getBytes
     * @since 1.4
     */
    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
        throw new IllegalStateException("Not implemented");
//        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Retrieves a stream that can be used to write to the <code>BLOB</code>
     * value that this <code>Blob</code> object represents.  The stream begins
     * at position <code>pos</code>.
     *
     * @param pos the position in the <code>BLOB</code> value at which
     *            to start writing
     * @return a <code>java.io.OutputStream</code> object to which data can
     *         be written
     * @throws java.sql.SQLException if there is an error accessing the
     *                               <code>BLOB</code> value
     * @see #getBinaryStream
     * @since 1.4
     */
    public OutputStream setBinaryStream(long pos) throws SQLException {
        throw new IllegalStateException("Not implemented");
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Truncates the <code>BLOB</code> value that this <code>Blob</code>
     * object represents to be <code>len</code> bytes in length.
     *
     * @param len the length, in bytes, to which the <code>BLOB</code> value
     *            that this <code>Blob</code> object represents should be truncated
     * @throws java.sql.SQLException if there is an error accessing the
     *                               <code>BLOB</code> value
     * @since 1.4
     */
    public void truncate(long len) throws SQLException {
        throw new IllegalStateException("Not implemented");
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void free() throws SQLException {
        bytes = null;
    }

    public InputStream getBinaryStream(long pos, long length) throws SQLException {
        ByteArrayInputStream result = new ByteArrayInputStream(getBytes(pos, (int)length));
        return result;
    }
}
