package org.tukaani.xz;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

@State(Scope.Thread)
public class UsrDictWordsBenchmark {
    @SuppressWarnings("UnusedDeclaration")
    private static Logger logger = Logger.getLogger(UsrDictWordsBenchmark.class.getName());

     private Path src;


    @Setup(Level.Invocation)

    public void setUp() throws IOException {
        src = Files.createTempFile("words","txt");
        Files.copy(getClass().getResourceAsStream("/words"), src, StandardCopyOption.REPLACE_EXISTING);
    }

    @Benchmark
    public void compressHC4ModeNormal() throws IOException {
        LZMA2Options lzma2Options = getLzma2Options(LZMA2Options.MODE_NORMAL, 16 * 1024 * 1024,
            LZMA2Options.MF_HC4);
        doTest(lzma2Options);
    }
    @Benchmark
    public void compressBT4ModeNormal() throws IOException {
        LZMA2Options lzma2Options = getLzma2Options(LZMA2Options.MODE_NORMAL, 16 * 1024 * 1024,
            LZMA2Options.MF_BT4);
        doTest(lzma2Options);
    }

    @Benchmark
    public void compressBT4ModeFast() throws IOException {
        LZMA2Options lzma2Options = getLzma2Options(LZMA2Options.MODE_FAST, 16 * 1024 * 1024,
            LZMA2Options.MF_BT4);
        doTest(lzma2Options);
    }

    @Benchmark
    public void compressHC4ModeFast() throws IOException {
        LZMA2Options lzma2Options = getLzma2Options(LZMA2Options.MODE_FAST, 16 * 1024 * 1024,
            LZMA2Options.MF_HC4);
        doTest(lzma2Options);
    }

    private LZMA2Options getLzma2Options(int mode, int dictSize, int matchFinder) throws UnsupportedOptionsException {
        LZMA2Options lzma2Options = new LZMA2Options();
        lzma2Options.setDictSize(dictSize);
        lzma2Options.setMode(mode);
        lzma2Options.setMatchFinder(matchFinder);
        return lzma2Options;
    }

    private void doTest(LZMA2Options lzma2Options) throws IOException {
        Path dst = Files.createTempFile("words", "txt.xz");
        OutputStream outputStream = Files.newOutputStream(dst);
        XZOutputStream xzout = new XZOutputStream(new BufferedOutputStream(outputStream),
            lzma2Options);
        Files.copy(src,xzout);
        xzout.close();
    }

}
