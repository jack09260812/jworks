import org.junit.Test;

import java.io.IOException;

/**
 * Created by jinwei on 17-5-21.
 */
public class BasicTest {

    @Test
    public void testCompress() throws Exception {
        System.out.println("-----------");
    }

    @Test
    public void testProcess() throws IOException, InterruptedException {
        String cmd = "soffice --headless -convert-to pdf  --outdir /home/jinwei/桌面 /home/jinwei/桌面/测试.docx ";
        Process process = Runtime.getRuntime().exec(cmd);
        process.waitFor();
        System.out.println(process.exitValue());
    }
}
