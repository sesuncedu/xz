package org.tukaani.xz;

import static org.tukaani.xz.LZMA2Options.MF_HC4;
import static org.tukaani.xz.LZMA2Options.MODE_FAST;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestPostEncodeDecodeDict {

    @SuppressWarnings("UnusedDeclaration")
    private static Logger logger = LoggerFactory.getLogger(TestPostEncodeDecodeDict.class);

    @Test
    public void testEncodingDict() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        LZMA2Options opts = getLzma2Options(MODE_FAST,64*1024,MF_HC4);
        XZOutputStream out = new XZOutputStream(bos, opts);
        PrintStream ps = new PrintStream(new BufferedOutputStream(out),false);
        for(int i=0;i<1000000;i++) {
            ps.println(i % 1000);
        }
        ps.close();
    }
    private LZMA2Options getLzma2Options(int mode, int dictSize, int matchFinder) throws UnsupportedOptionsException {
        LZMA2Options lzma2Options = new LZMA2Options();
        lzma2Options.setDictSize(dictSize);
        lzma2Options.setMode(mode);
        lzma2Options.setMatchFinder(matchFinder);
        return lzma2Options;
    }

}
