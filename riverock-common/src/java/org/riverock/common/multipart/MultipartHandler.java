/*
 * org.riverock.common -- Supporting classes, interfaces, and utilities
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

/**
 * 2003. Copyright (c) jSmithy. http:// multipart.jSmithy.com
 * 2001-2003. Copyright (c) Simon Brooke. http://www.weft.co.uk/library/maybeupload/
 *
 * $Id$
 */

package org.riverock.common.multipart;

import org.apache.log4j.Category;

import java.io.*;
import java.util.*;

// for unpacking content disposition headers

/** an extremely kludgy way of implementing the nasty recursion
 *  necessary in readrfc1867() */
class EndOfPartException extends IOException
{
    EndOfPartException(String line)
    {
        super(line);
    }
}


/** A handler for multipart-form-data data per RFC 1867. One of the
 *  trickier elements of RFC 1867 is that multipart/mixed elements may
 *  be embedded inside multipart/form-data. The RFC does
 *  <strong>not</strong> say that multipart elements may not be
 *  arbitrarily nested. While I don't know whether any clients
 *  <em>do</em> nest multipart elements, it would be nice to be fully
 *  RFC 1867 compliant...</p>
 *
 *  <p>This class knows about content-type-encodings but does not yet
 *  decode encoded types. This is something I intend to fix in a later
 *  release.</p>
 *
 *  <p>Objects of this class are not intended to be reusable. Use once
 *  and throw away.</p>
 *  </pre> */

public class MultipartHandler
{
    private static Category log = Category.getInstance(MultipartHandler.class);

    /** policy for whether to save uploaded files immediately.
     Default is true. If false, we add the binary data to the
     hash instead */
    private boolean saveUploadedFilesToDisk = true;

    /** policy for whether or not to allow uploaded files to be
     *  overwritten by files uploaded later. If you want to be able to
     *  over-write files, set to true */
    private boolean allowOverwrite = false;

    /** policy for whether or not to auto-generate unique
     *  filenames. Default is we do, so there's no danger of
     *  previously uploaded files being over-written. */
    private boolean silentlyRename = true;

    /** the boundaries I am currently watching */
    private Stack boundaries = new Stack();

    /**
     * the name-value pairs I have identified
     *
     * @deprecated use getValue()
     */
    protected Hashtable values = null;

    private Hashtable hashValue = null;

    /** the stream I read from */
    private PushbackInputStream in;

    /** the advertised length of the stream */
    private int expected;

    /** the directory in which I will save
     *  uploaded files */
    private File workDir;

    /** the number of bytes I've read so far */
    private int read = 0;

    /** a buffer to assemble stuff I'm reading */
    private byte[] buf = new byte[8 * 1024];

    /** a nasty marker to indicate whether
     * the last file part read contained
     * any valid characters -- some *
     * browsers send more than one
     * line-end for 'no file to upload' */
    private int validChars = 0;

    /** a counter to use to name anonymous
     *  part values (should never be
     *  needed) */
    protected int anon = 0;

    /** content-transfer-encoding types, as mandated by RFC 1521,
     *  section 5. X-tokens will not be handled because to quote the
     *  RFC 'the creation of new Content-Transfer-Encoding values is
     *  explicitly and strongly discouraged' */
    protected final char CTE_7BIT = '7'; // default
    protected final char CTE_QUOTED_PRINTABLE = 'q';
    protected final char CTE_BASE64 = 'a';
    protected final char CTE_8BIT = '8';
    protected final char CTE_BINARY = 'i';
    protected final char CTE_XTOKEN = 'x';

    /** patterns I use to unpack the content-disposition header */
    private final String filePattern =
        "form-data; *name=\"([^\"]*)\"; *filename=\"([^\"]*)\"";

    private final String simplePattern = "form-data; *name=\"([^\"]*)\"";

    /** Disallowed characters in filenames. Hashtable of Characters. */
    protected Hashtable disallowedCharacters = new Hashtable();

    /** read multiple values from this RFC 1867 formatted input stream
     *  into this hashtable
     *
     *  @param values a hashtable to populate with the values read
     *  @param in an input stream, assumed to be RFC 1867 formatted
     *  @param cthdr the content-type header which identifies this
     *  stream as multipart
     *  @param workdir a directory in which to save uploaded files
     */
    MultipartHandler(Hashtable values, InputStream in, int length,
                     String cthdr, File workdir)
        throws IOException, UploadException
    {
        this(values, in, length, cthdr, workdir, true, false, true);

    }

    /** read multiple values from this RFC 1867 formatted input stream
     *  into this hashtable
     *
     *  @param values a hashtable to populate with the values read
     *  @param in an input stream, assumed to be RFC 1867 formatted
     *  @param cthdr the content-type header which identifies this
     *  stream as multipart
     *  @param workdir a directory in which to save uploaded files
     */
    public MultipartHandler(Hashtable values, InputStream in, int length,
        String cthdr, File workdir,
        boolean saveUploadedFilesToDisk,
        boolean allowOverwrite,
        boolean silentlyRename
        )
        throws IOException, UploadException
    {
        if (saveUploadedFilesToDisk && workdir==null)
            throw new IllegalArgumentException("Work dir is not specified");

        this.hashValue = new Hashtable();
        this.in = new PushbackInputStream(in);
        this.expected = length;
        this.saveUploadedFilesToDisk = saveUploadedFilesToDisk;
        this.workDir = workdir;
        this.values = values;
        this.allowOverwrite = allowOverwrite;
        this.silentlyRename = silentlyRename;

        disallow(' ', '_');	// spaces in filenames are a nuisance
        disallow("?*", '_');	// wildcards are a worse nuisance
        disallow('#', '_');	// UN*X shell comment chars are best avoided

        if (log.isDebugEnabled())
            log.debug("*** starting to read input");

        readrfc1867(cthdr);

        if (log.isDebugEnabled())
        {
            log.debug("*** finished reading input");
            log.debug("internal count shows: read " + read +
                " of expected " + expected + " bytes\n");
        }
    }


    /** mark the specified character as diallowed in filenames, and
     *  replace it if found with the specified replacement
     *
     *  @param disallowed the character we disallow
     *  @param preferred the character to replace it with
     */
    public void disallow(char disallowed, char preferred)
    {
        disallowedCharacters.put(new Character(disallowed),
            new Character(preferred));
    }

    /** mark the specified characters as diallowed in filenames, and
     *  replace it if found with the specified replacement
     *
     *  @param disallowed a String comprising the characters we disallow
     *  @param preferred the character to replace it with
     */
    public void disallow(String disallowed, char preferred)
    {
        for (int i = 0; i < disallowed.length(); i++)
            disallow(disallowed.charAt(i), preferred);
    }

    /** the recursive multipart reader. Deep. Dark. Mysterious. Could
     *  almost certainly be rewritten better. See RFC 1521, RFC 1867
     *
     *  @param cthdr the content-type header which identifies this
     *  stream as multipart
     *  @return the last line read.  */
    private String readrfc1867(String cthdr)
        throws IOException, UploadException
    {
        String line = readLine();
        String boundary = getBoundary(cthdr);

        while (line != null && line.startsWith(boundary))
        {			// iterate over the parts in this part
            if (log.isDebugEnabled())
                log.debug("*** seeking new part");

            line = readLine();
            // OK: the next line after a part
            // boundary is either
            // a header or
            // the next boundary up the stack or
            // null for the end of input

            if (line != null)
            {		// got something
                if (line.startsWith("--"))
                {	// looks like another boundary
                    if (line.startsWith((String)
                        boundaries.peek()))
                        throw new EndOfPartException(line);
                    // a slightly inelegant way of
                    // returning from recursion, no?
                    else
                        throw new
                            IOException("Unexpected recursive " +
                            "boundary: " + line);
                }
                else
                {
                    line = handlePart(line, boundary);
                }
            }
        }
        return line;
    }

    /** handle a single part of a multipart file, starting with this
     *  line which has already been read in */
    protected String handlePart(String line, String boundary)
        throws IOException, UploadException
    {
        Hashtable headers = handlePartHeaders(line);
        // nicer if we could use a
        // uk.co.weft.dbutil.Context, but this
        // version is intended to be
        // Jacquard-independent

        if (!headers.isEmpty())
        {			// if headers is empty, we've probably
            // come to the end of input. That's OK.
            if (log.isDebugEnabled())
                log.debug("*** seeking part data");

            String cthdr = (String) headers.get("content-type");

            if (cthdr != null && cthdr.indexOf("multipart") > -1)
            {		// it's a multipart (RFC 1521) - recurse
                boundaries.push(boundary);

                try
                {
                    readrfc1867(cthdr);
                }
                catch (EndOfPartException ok)
                {
                    line = ok.getMessage();
                }

                if (!boundary.startsWith((String)
                    boundaries.pop()))
                    throw new
                        IOException("Misnested multipart " +
                        "boundaries?");
            }
            else	// not multipart
            {
                line = handlePartData(headers, boundary);
            }

            if (log.isDebugEnabled())
                log.debug("-- handled data, returning ["
                    + line + "]");

        }

        return line;
    }

    /** strip leading and trailing spaces and quotes from this string */
    private String deString(String string)
    {
        if (string != null)
        {
            string = string.trim();

            while (string.startsWith("\""))
                string = string.substring(1);

            while (string.endsWith("\""))
                string = string.substring(0, string.length() - 1);
        }

        return string;
    }


    private void extractPartHeadersFromLine(String line, Hashtable headers)
    {
        // OK, why not use StringTokenizer?
        // Well, because I tried that, and on
        // some JVMs it swallowed some
        // separators and not others.
        int tokenStart = 0, tokenEnd = 0;
        String name = null, value = null;
        String namesep = ":";
        String valuesep = ";";

        while (tokenEnd < line.length())
        {
            tokenEnd = line.indexOf(namesep, tokenStart);
            if (tokenEnd == -1) tokenEnd = line.length();

            name = deString(line.substring(tokenStart, tokenEnd));

            namesep = "=";
            // first name on a line is terminated
            // with a colon; second and subsequent
            // with '='

            tokenStart = tokenEnd + 1;
            tokenEnd = line.indexOf(valuesep, tokenStart);
            if (tokenEnd == -1) tokenEnd = line.length();

            value = deString(line.substring(tokenStart, tokenEnd));

            tokenStart = tokenEnd + 1;

            if (log.isDebugEnabled())
                log.debug("  ++ Setting part header [" +
                    name + "] to [" + value + "]");

            headers.put(name, value);
        }
    }


    private Hashtable handlePartHeaders(String line)
        throws IOException
    {
        Hashtable headers = new Hashtable();
        // nicer if we could use a
        // uk.co.weft.dbutil.Context, but this
        // version is intended to be
        // Jacquard-independent
        int colon;		// index of first colon in line;
        String name, value;	// the name and value parts of the line

        while (line != null &&
            line.indexOf(':') > -1)
        {			// extract all the headers for this part
            if (log.isDebugEnabled())
                log.debug("  -- Seeking new part headers in " +
                    line);

            extractPartHeadersFromLine(line, headers);

            line = readLine();
            // seek next header
        }

        // the line which follows the headers
        // should be blank; if it's not we may
        // have an error.
        if (log.isDebugEnabled() && line != null && line.trim().length() > 0)
            log.debug("Non-blank line [" + line +
                "] (first character [" +
                line.getBytes()[0] +
                "]) following part headers?");

        return headers;
    }

    /** read part data from the provided input stream, up to and
     *  including the next boundary line. Interpret the headers and
     *  dispose of the content appropriately. At the point at which
     *  this method is entered, the next line in the file is the value
     *  part of the part...
     *
     *  @param headers the headers for this part
     *  @param boundary the boundary delimiting this part
     *  @return the last line read (should be boundary or null) */
    private String handlePartData(Hashtable headers, String boundary)
        throws IOException, UploadException
    {
        String line = null;	// moderately safe default value...
        String cteheader = (String) headers.get("content-transfer-encoding");
        char cte = CTE_7BIT;	// RFC 1521 default
        String name = (String) headers.get("name");

        if (name == null)
        {
            name = "unknown" + new Integer(anon++).toString();
            // if a name wasn't provided, invent
            // one (unlikely).
            headers.put("name", name);
        }

        if (cteheader != null)	// encode content-transfer-encoding
        {
            cteheader = cteheader.toLowerCase();

            cte = cteheader.charAt(0);

            if (cte == 'b') cteheader.charAt(1);
            // else 'base64' and 'binary' would be
            // ambiguous
        }

        if (headers.get("filename") != null)
        {			// handle file
            line = handleFilePart(headers, cte, boundary);
        }
        else
        {
            line = handleInlinePart(headers, cte, boundary);
        }

        return line;
    }


    /** read a value from the input stream up to the next boundary,
     *  and cache it in my values on the name which is the value of
     *  the "name" header in these headers
     *
     *  @param headers a hash of the headers of the current part
     *  @param cte the content-transfer-encoding of the current part
     *  @param boundary the boundary of the current part
     *
     *  @return the last line read- which should be the boundary...
     */
    protected String handleInlinePart(Hashtable headers, char cte,
                                      String boundary)
        throws IOException
    {
        String line = null;

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        if (log.isDebugEnabled())
            log.debug("-- handling inline part [" +
                headers.get("name") + "]");

        line = readPartData(out, boundary, cte);

        String name = (String) headers.get("name");
        String outString = out.toString();
        put(name, outString);
        putValue( name, new ParameterPart(outString));

        if (log.isDebugEnabled())
            log.debug("-- handled inline part, value was [" +
                out.toString() + "]");

        return line;
    }

    /** read a value from the input stream up to the next boundary,
     *  save it to a file in my workdir whose name is the value of the
     *  'filename' header in these headers, and cache a File object
     *  describing it in my values on the name which is the value of
     *  the "name" header in these headers
     *
     *  @param headers a hash of the headers of the current part
     *  @param cte the content-transfer-encoding of the current part
     *  @param boundary the boundary of the current part
     *
     *  @return the last line read-which should be the boundary...
     */
    protected String handleFilePart(Hashtable headers, char cte, String boundary)
        throws IOException, UploadException
    {
        String line = null;
        String filename = (String) headers.get("filename");

        String originName = null;
        String originFullName = null;
        if (filename != null)  // which it shouldn't be, but let's be
        // carefull
        {
            originFullName = filename;
            int sep = Math.max(filename.lastIndexOf('/'),
                filename.lastIndexOf("\\"));

            if (log.isDebugEnabled())
            {
                log.debug("Seeking separator in filename [" +
                    filename + "]; index was " + sep);
            }

            if (sep > -1)
                filename = filename.substring(sep + 1);
            // we don't want any directory
            // structure in the filename!

            originName = filename;
        }

        if (filename == null || filename.length() == 0)
            filename = "unknown" + new Integer(anon++).toString();

        // Replace disallowed characters from within the filename
        Enumeration elements = disallowedCharacters.keys();

        while (elements.hasMoreElements())
        {
            Character charToReplace =
                (Character) elements.nextElement();
            Character charToReplaceWith =
                (Character) disallowedCharacters.get(charToReplace);
            filename = filename.replace(charToReplace.charValue(),
                charToReplaceWith.charValue());
        }

        if (log.isDebugEnabled())
            log.debug("-- handling file part [" + filename + "]");

        if (isSaveUploadedFilesToDisk())
        {

            UploadedFile uploadedFile = new UploadedFile(workDir + File.separator + filename);
            uploadedFile.originFullName = originFullName;
            uploadedFile.originName = originName;

            if (log.isDebugEnabled())
                log.debug("File "+uploadedFile+", exists - "+
                    uploadedFile.exists()+", allowOverwrite -"+allowOverwrite
                );

            if (uploadedFile.exists() && !allowOverwrite)
            {
                if (silentlyRename)
                {
                    if (log.isDebugEnabled())
                        log.debug("silentlyRename name of file");

                    for (int i = 1; uploadedFile.exists(); i++)
                    {
                        // generate a unique prefix
                        String newFileName = workDir +
                            File.separator +
                            new Integer(i).toString() +
                            "_" + filename;
                        if (log.isDebugEnabled())
                            log.debug("new filename - "+newFileName);

                        uploadedFile = new
                            UploadedFile(newFileName);
                    }
                }
                else
                    throw new
                        UploadException("There is already a file " +
                        "called " + filename +
                        " in the upload directory");
            }

            OutputStream out = null;

            try
            {
                // 64K buffer
                int bufferSize = 64 * 1024;

                out = new
                    BufferedOutputStream(new
                        FileOutputStream(uploadedFile),
                        bufferSize);

                validChars = 0;
                // dirty hacky marker to let us know
                // whether there was any valid content
                // in the file

                line = readPartData(out, boundary, cte);
                // read the part into the file
            }
            finally
            {
                if (out != null)
                {
                    try
                    {
                        out.flush();
                        out = null;
                    }
                    catch (IOException ioe)
                    {
                    }
                }
            }

            if (validChars > 0)
            {
                uploadedFile.setClientPathname((String)headers.get("filename"));
                uploadedFile.setContentType((String)headers.get("Content-Type"));
                // if we've uploaded something, cache
                // the client-side name and the mime
                // type sent by the client on the
                // object.

                String name = (String) headers.get("name");
                put(name, uploadedFile);
                try {
                    putValue(name, new FileOnDiskPart(uploadedFile) );
                }
                catch( MultipartRequestException e ) {
                    String es = "Error create FilePart";
                    log.error( es, e );
                    throw new UploadException( es, e );
                }
                // cache the file object on the uploadedFile
                // name - we don't cache empty files!
            }
            else
                uploadedFile.delete();
            // delete empty file. Is this the
            // right thing to do?
        }
        else
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            // read the part into the outputstream
            line = readPartData(out, boundary, cte);

            if (out.size() > 0)
            {
                UploadedBytes bytes = new
                    UploadedBytes(out.toByteArray());
                bytes.setClientPathname(
                    (String) headers.get("filename"));
                bytes.setContentType(
                    (String) headers.get("Content-Type"));

                // Add the binary data to the hash
                String name = (String) headers.get("name");
                put(name, bytes);
                putValue(name, new FileInMemoryPart(bytes) );
            }
        }

        if (log.isDebugEnabled())
            log.debug("-- handled file part");

        return line;		// and return the supposed boundary
    }


    /** within the name/value stream a name may have multiple
     *  values. If the name has just one value I want to store just
     *  the object because that makes life simpler; however if I find
     *  a second value I want to convert it to a vector of values
     *
     *  @param name the key to store against
     *  @param value the String or File to store
     */
    protected synchronized void put(String name, Object value)
    {
        Object existing = getValues().get(name);

        if (existing != null)
        {
            List multival = null;

            if (existing instanceof List)
                multival = (List) existing;
            else
            {
                multival = new ArrayList();

                multival.add(existing);
                getValues().put(name, multival);
            }

            multival.add(value);
        }
        else
            getValues().put(name, value);
    }

    /** within the name/value stream a name may have multiple
     *  values. If the name has just one value I want to store just
     *  the object because that makes life simpler; however if I find
     *  a second value I want to convert it to a vector of values
     *
     *  @param name the key to store against
     *  @param value the String or File to store
     */
    private synchronized void putValue(String name, Object value)
    {
        Object existing = hashValue.get(name);

        if (existing != null)
        {
            Vector multival = null;

            if (existing instanceof Vector)
                multival = (Vector) existing;
            else
            {
                multival = new Vector();

                multival.addElement(existing);
                hashValue.put(name, multival);
            }

            multival.addElement(value);
        }
        else
            hashValue.put(name, value);
    }

    /** extract a multipart boundary from this string, presumed to be
     *  a content-type header. Private to this class; final for
     *  efficiency.
     *
     *  @param cthdr a Content-type header value
     *  @return the multipart boundary string which is embedded in it, if any
     */
    private final String getBoundary(String cthdr)
    {
        int bend = cthdr.lastIndexOf("boundary=");

        if (bend == -1)
            return null;

        return "--" + cthdr.substring(bend + 9); // length of string 'boundary='
    }


    /** read the next part from my input stream, considered an an
     *  ASCII stream, and write it to this output stream. Omit (do not
     *  write) trailing CR/LF sequence; return the boundary found on
     *  the stream if any. This version is a total rewrite of the
     *  version in 1.0.4 and earlier; the objective is to make it
     *  easier to understand, and, consequently, cat.isDebugEnabled().
     *
     *  @param out the OutputStream to write to
     *  @param boundary the boundary to read up to
     *  @param cte the content-type-encoding used in the current
     *  part. Currently not used. A later version of this method may
     *  read-and-decode
     *  @return the boundary found on the input stream
     */
    private String readPartData(OutputStream out,
                                String boundary, char cte) throws IOException
    {
        final int STATE_READING = 0; // Normal state where we're just
        // zipping along copying to output
        final int STATE_EOL = 1;     // We've seen an EOL and have
        // started buffering, but haven't
        // seen any of the boundary
        final int STATE_HYPHEN_1 = 3;// We've seen a newline followed
        // by a hyphen - this is probably
        // a boundary
        final int STATE_HYPHEN_2 = 4;// We've seen a newline followed
        // by more than one hyphen - this
        // is probably a boundary
        final int STATE_COMPARING = 5; // We think we've got a
        // boundary, and are checking
        // along it
        final int STATE_DONE = -1;   // We've done.

        final int hyphen = 45;	// ASCII hyphen

        ByteArrayOutputStream bbuf = null;

        int boundstart = 0;	// where we thought we saw the
        // start of the boundary

        int state = STATE_READING;
        // the state we're in; initially,
        // we're reading

        String value = null;	// The value we're going to
        // return. Moderately safe default...

        int ch = -1;		// the next character read from the stream

        while (state != STATE_DONE)
        {
            ch = in.read();	// read the next character...
            read++;	// and increment the characters read counter
            if (read>expected)
                throw new IOException("Count of readed bytes ("+read+
                    ") exceeds limit of "+expected);

            switch (state)
            {
                case STATE_READING:
                    switch (ch)
                    {
                        case 13:
                            // a CR by itself is an EOL condition
                            // (UN*X), but a CR followed by an LF
                            // is the same EOL condition (DOS &
                            // others); we start a new buffer
                            bbuf = new ByteArrayOutputStream();
                            bbuf.write(ch);
                            int chnext = in.read();
                            if (chnext == 10)
                            {
                                bbuf.write(chnext);
                                read++;
                            }
                            else
                            {
                                in.unread(chnext);
                            }
                            state = STATE_EOL;
                            break;
                        case 10:
                            // an LF by itself is an EOL condition
                            // (Mac OS); we start a new buffer
                            bbuf = new ByteArrayOutputStream();
                            bbuf.write(ch);
                            state = STATE_EOL;
                            break;
                        case -1:
                            // read off the end of input -
                            // shouldn't happen
                            state = STATE_DONE;
                        default:
                            validChars++;
                            out.write(ch);
                            break;
                    }
                    break;
                case STATE_EOL:
                    switch (ch)
                    {
                        case hyphen:
                            bbuf.write(ch);
                            state = STATE_HYPHEN_1;
                            break;

                        case 10:
                        case 13:
                            // uh-oh. looks like two EOLs in a
                            // row. back up so our normal EOL case
                            // can take care of it
                            bbuf.writeTo(out);
                            in.unread(ch);
                            read--;
                            state = STATE_READING;
                            break;
                        case -1:
                            // read off the end of input -
                            // shouldn't happen
                            bbuf.writeTo(out);
                            state = STATE_DONE;
                            break;
                        default:
                            // read a character which wasn't a new
                            // line or a hyphen - we're back to
                            // ordinary reading
                            bbuf.write(ch);
                            bbuf.writeTo(out);
                            state = STATE_READING;
                            break;
                    }
                    break;
                case STATE_HYPHEN_1:
                    boundstart = read - 1;
                    // we start counting the boundary
                    // from the first hyphen
                    switch (ch)
                    {
                        case hyphen:
                            // a hyphen followed by another hyphen
                            // suggests a boundary
                            bbuf.write(ch);
                            state = STATE_HYPHEN_2;
                            break;
                        case -1:
                            // read off the end of input -
                            // shouldn't happen
                            bbuf.writeTo(out);
                            state = STATE_DONE;
                            break;
                        default:
                            // read a character which wasn't a
                            // hyphen - we're back to ordinary
                            // reading
                            bbuf.write(ch);
                            bbuf.writeTo(out);
                            state = STATE_READING;
                            break;
                    }
                    break;
                case STATE_HYPHEN_2:
                    switch (ch)
                    {
                        // The following lines commented out
                        // by Shawn Grunberger 30 Sept 2002
                        // This fixes a bug in which some
                        // lines of regular content, if they
                        // contained enough hyphens, would
                        // cause a runtime error.

                        //case hyphen:
                        //// arbitrary number of hyphens in a boundary
                        //bbuf.write( ch);
                        //break;
                        case -1:
                            // read off the end of input -
                            // shouldn't happen
                            bbuf.writeTo(out);
                            state = STATE_DONE;
                            break;
                        default:
                            // start to compare against boundary;
                            // back up so compare starts at first
                            // char after second hyphen
                            in.unread(ch);
                            read--;

                            state = STATE_COMPARING;
                            break;
                    }
                    break;
                case STATE_COMPARING:
                    {
                        int cursor = read - boundstart;
                        // cursor into boundary

                        if (ch == boundary.charAt(cursor))
                        {
                            bbuf.write(ch);
                            if ((cursor + 1) == boundary.length())
                            {
                                state = STATE_DONE;

                                value = boundary + readLine();
                                // swallow anything remaining on the line
                            }
                        }
                        else if (ch == -1)
                        {
                            bbuf.write(ch);  // added by Shawn Grunberger 30 Sept 2002
                            // read off end of input - shouldn't happen
                            state = STATE_DONE;
                        }
                        else
                        {
                            // Not a match - this line is not a boundary after all
                            // Next two lines added by Shawn Grunberger 30 Sept 2002 - back up so reading starts at first char that doesn't match boundary
                            in.unread(ch);
                            read--;
                            bbuf.writeTo(out);
                            state = STATE_READING;
                        }
                    }
                    break;
            }
        }
        return value;
    }


    /** read the next line from my input stream and return it as a
     *  String, assuming 7-bit ASCII encoding.
     *
     *  @return a String representation of my next input line */
    private String readLine()
        throws IOException
    {
        return readLine(CTE_7BIT);
    }

    /** read the next line from my input stream and return it as a String
     *
     *  @param cte the content-type-encoding used in the current
     *  part. Currently not used. A later version of this method may
     *  read-and-decode
     *
     *  @return a String representation of my next input line */
    private String readLine(char cte)
        throws IOException
    {
        StringBuffer sbuf = new StringBuffer();
        String result = null;	// safe default
        int nread;


        /* OK, what's going on here? We want
         * to read at least one bufferfull from the input. If we
         * completely fill the buffer, we want to read another
         * bufferfull, and so on until we don't fill the buffer */
        do
        {
            nread = readLine(buf, 0, buf.length, cte);

            if (nread != -1)
            {
                sbuf.append(new String(buf, 0, nread, "ISO-8859-1"));
            }
        }
        while (nread == buf.length);

        if (sbuf.length() > 0)	// got some
        {
            result = sbuf.toString();

            for (boolean trimmed = false;
                 result.length() > 0 && trimmed == false;)
            {
                switch (result.charAt(result.length() - 1))
                {
                    case '\r':
                    case '\n':
                        result =
                            result.substring(0, result.length() - 1);
                        break;
                    default:
                        trimmed = true;
                }
            }
            // trim trailing linefeed
            // sequence. This may not be the best
            // way to do it. Just 'result =
            // sbuf.toString().trim()' might be
            // better. But what if there's
            // significant whitespace at the ends
            // of the string?
        }

        if (log.isDebugEnabled())
            log.debug("-- String readLine returning [" +
                result + "]; total read = " + read +
                " out of " + expected + " bytes expected");

        return result;
    }

    /** read a line up to and including a CR/LF line end from my
     *  InputStream into this buffer.
     *
     *  @param b a byte array to read into
     *  @param off the offset in the buffer at which to start
     *  @param len the maximum number of bytes to read
     *  @param cte the content-type-encoding used in the current
     *  part. Currently not used. A later version of this method may
     *  read-and-decode
     *
     *  @return the number of bytes read
     */
    public int readLine(byte b[], int off, int len, char cte)
        throws IOException
    {
        int result = 0;

        final int STATE_DFLT = -1;
        final int STATE_CR = 13;
        final int STATE_EOL = 0;

        int state = STATE_DFLT;

        int ch = -2;		// char read: initialise to impossible value


        while (state != STATE_EOL &&
            result < len)
        {
            ch = in.read();	// read a byte from the stream

            b[result + off] = (byte) ch;
            // store the byte read
            result++;	// increment counter for this line
            read++;	// and for total read

            switch (state)
            {
                case STATE_CR:
                    switch (ch)
                    {
                        case -1:
                        case 10:
                            state = STATE_EOL;
                            break;
                        case 13:
                            state = STATE_CR;
                            break;
                        default:
                            state = STATE_DFLT;
                            break;
                    }
                    break;
                default:
                    switch (ch)
                    {
                        case -1:
                            state = STATE_EOL;
                            break;
                        case 13:
                            state = STATE_CR;
                            break;
                        default:
                            state = STATE_DFLT;
                            break;
                    }
                    break;
            }
        }

        if (b[0] == -1)	// first thing we read was an EOF
            result = 0;

        return result;
    }

    public boolean isSaveUploadedFilesToDisk()
    {
        return saveUploadedFilesToDisk;
    }

    public void setSaveUploadedFilesToDisk(boolean saveUploadedFilesToDisk)
    {
        this.saveUploadedFilesToDisk = saveUploadedFilesToDisk;
    }

    public Hashtable getValues()
    {
        return values;
    }

    public void setValues(Hashtable values)
    {
        this.values = values;
    }

    public Map getPartsHash()
    {
        return hashValue;
    }

    public void setPartsHash( Hashtable hashValue )
    {
        this.hashValue = hashValue;
    }
}
