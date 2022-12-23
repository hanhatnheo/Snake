package org.cis1200.snake;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * FileLineIterator provides a useful wrapper around Java's provided
 * BufferedReader and provides practice with implementing an Iterator. Your
 * solution should not read the entire file into memory at once, instead reading
 * a line whenever the next() method is called.
 *
 */
public class FileLineIterator implements Iterator<String> {

    private BufferedReader r;
    private String line;
    /**
     * Creates a FileLineIterator for the reader. Fill out the constructor so
     * that a user can instantiate a FileLineIterator. Feel free to create and
     * instantiate any variables that your implementation requires here. See
     * recitation and lecture notes for guidance.
     *
     */
    public FileLineIterator(BufferedReader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("Reader is null");
        }
        try {
            r = reader;
            line = r.readLine();
        } catch (IOException e) {
            line = null;
        }
    }

    /**
     * Returns true if there are lines left to read in the file, and false
     * otherwise.
     * <p>
     * If there are no more lines left, this method should close the
     * BufferedReader.
     *
     * @return a boolean indicating whether the FileLineIterator can produce
     *         another line from the file
     */
    @Override
    public boolean hasNext() {
        if (line == null) {
            try {
                r.close();
            } catch (IOException e) {
                throw new NoSuchElementException("No more lines");
            }
        }
        return line != null;
    }

    /**
     * Returns the next line from the file, or throws a NoSuchElementException
     * if there are no more strings left to return (i.e. hasNext() is false).
     * <p>
     * This method also advances the iterator in preparation for another
     * invocation. If an IOException is thrown during a next() call, your
     * iterator should make note of this such that future calls of hasNext()
     * will return false and future calls of next() will throw a
     * NoSuchElementException
     *
     * @return the next line in the file
     * @throws java.util.NoSuchElementException if there is no more data in the
     *                                          file
     */
    @Override
    public String next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more lines left to return");
        }
        String l = line;
        try {
            line = r.readLine();
        } catch (IOException e) {
            line = null;
        }
        return l;
    }
}
