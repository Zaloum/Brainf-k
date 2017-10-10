package com.zaloum.brainfcuk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Objects;

public class BrainFcuk {

    /** Moves the data pointer to the right */
    public static final byte INC_PTR   = '>';
    /** Moves the data pointer to the left */
    public static final byte DEC_PTR   = '<';
    /** Increments the byte at the data pointer */
    public static final byte INC       = '+';
    /** Decrements the byte at the data pointer */
    public static final byte DEC       = '-';
    /** Outputs the byte at the data pointer */
    public static final byte OUT       = '.';
    /** Accepts one byte of input */
    public static final byte IN        = ',';
    /** If the byte at the data pointer is 0 jump the instruction pointer to the matching {@link #JMP_BCK "]"}*/
    public static final byte JMP_FWD   = '[';
    /** If the byte at the data pointer is not 0 jump the instruction pointer back to the matching {@link #JMP_FWD "["} */
    public static final byte JMP_BCK   = ']';

    private InputStream in;
    private OutputStream out;

    /**
     * Constructs a BrainFcuk interpreter using the provided streams for I/O.
     * @param in
     * @param out
     */
    public BrainFcuk(InputStream in, OutputStream out) {
        Objects.requireNonNull(in);
        Objects.requireNonNull(out);
        this.in = in;
        this.out = out;
    }

    /**
     * Constructs a BrainFcuk interpreter using System.in and System.out for
     * I/O.
     */
    public BrainFcuk() {
        this(System.in, System.out);
    }

    /**
     * Interprets and runs the program.
     * @param program to run
     * @throws IOException if the program uses I/O commands and an I/O error occurs
     * @throws NullPointerException if program is {@code null}
     */
    public void interpret(byte[] program) throws IOException {
        int ptr = 0;
        int dataPtr = 0;
        byte[] data = new byte[10];

        while (ptr < program.length) {
            byte command = program[ptr++];

            switch (command) {
                case INC_PTR:
                    dataPtr++;
                    // expand data array if necessary
                    if (dataPtr >= data.length) {
                        int oldCapacity = data.length;
                        int newCapacity  = oldCapacity + (oldCapacity >> 1);
                        data = Arrays.copyOf(data, newCapacity);
                    }
                    break;
                case DEC_PTR:
                    if (dataPtr > 0)
                        dataPtr--;
                    break;
                case INC:
                    data[dataPtr]++;
                    break;
                case DEC:
                    data[dataPtr]--;
                    break;
                case OUT:
                    this.out.write(data[dataPtr]);
                    this.out.flush();
                    break;
                case IN:
                    data[dataPtr] = (byte)this.in.read();
                    break;
                case JMP_FWD:
                    if (data[dataPtr] == 0) {
                        int count = 1;
                        do {
                            switch (program[ptr++]) {
                                case JMP_BCK: count--; break;
                                case JMP_FWD: count++; break;
                                default: // ignore
                            }
                        } while (count != 0);
                    }
                    break;
                case JMP_BCK:
                    if (data[dataPtr] != 0) {
                        int count = 0;
                        do {
                            switch (program[--ptr]) {
                                case JMP_BCK: count++; break;
                                case JMP_FWD: count--; break;
                                default: // ignore
                            }
                        } while (count != 0);
                    }
                    break;
                default:
                    // ignore
            }

        }
    }

    /**
     * Interprets and runs the program.
     * @param program to run
     * @throws IOException if the program uses I/O commands and an I/O error occurs
     * @throws NullPointerException if program is {@code null}
     */
    public void interpret(String program) throws IOException {
        Objects.requireNonNull(program);
        interpret(program.getBytes());
    }

    /**
     * Interprets and runs the program. The stream is read fully before the program is run.
     * @param stream containing the program
     * @throws IOException if any I/O error occurs
     * @throws NullPointerException if stream is {@code null}
     */
    public void interpret(InputStream stream) throws IOException {
        Objects.requireNonNull(stream);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int len;
            while ((len = stream.read(buffer)) != -1)
                outputStream.write(buffer, 0, len);
            interpret(outputStream.toByteArray());
        }
    }
}
