package cc.wanforme.hi;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequestMapping("/server")
public class FileController {

    /**
     * 该接口需要注意权限问题，会下载任何文件
     */
    @GetMapping("/local")
    public ResponseEntity<byte[]> loadFile(String p,HttpServletRequest request, HttpServletResponse response) {
//        String uri = request.getRequestURI();
//        String path = uri.substring("/local/".length());
        String path = p;
        return downloadStreamLocal(path, request, response);
    }

    /**
     * 文件下载,支持断点续载 <br>
     * 如果是续载，则在输入流中进行偏移
     *
     * @param request
     * @throws IOException
     */
    public static ResponseEntity<byte[]> downloadStreamLocal(String path, HttpServletRequest request, HttpServletResponse response) {
        File f = new File(path);
        if (!f.exists()) {
            System.err.println("File not found: " + f.getAbsolutePath());
        }

        String outName = f.getName();
        try {
            outName = URLEncoder.encode(f.getName(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.err.println("UnsupportedEncodingException: " + e.getMessage());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment;filename=" + outName);
        headers.add("Access-Control-Expose-Headers", "Content-Disposition");
        headers.add("Accept-Ranges", "bytes");

        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        byte[] bytes = new byte[0];
        try (FileInputStream fis = new FileInputStream(f);) {
            //端点续载检查和处理, 检查请求头中的信息
            Long[] range = getRange(request);
            long start = range[0].longValue() == -1 ? 0 : range[0].longValue();
            long end = range[1].longValue() == -1 ? f.length() - 1 : range[1].longValue();
            int blockSize = (int) (end - start + 1);

            headers.add("Content-Length", blockSize + "");

            // Range 的 end 需要减一!!!
            String contentRange = "bytes " + start + "-" + end + "/" + f.length();
            headers.add("Content-Range", contentRange);
            System.out.println("Range-response: " + contentRange);

            bytes = new byte[blockSize];
            fis.skip(start);
            fis.read(bytes);

            System.out.println("block-size:" + blockSize + "/" + f.length());
            System.out.println("流文件下载成功!");
            boolean fullSize = (range[0].longValue() == -1L && range[1].longValue() == -1L)
                    || blockSize == f.length();
            return new ResponseEntity<byte[]>(bytes, headers, fullSize ? HttpStatus.OK : HttpStatus.PARTIAL_CONTENT);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.ok().build();
        }
    }

    /**
     * 检查端点续传，如果是续传，则在输入流中进行指针偏移
     *
     * @param request
     * @throws IOException
     */
    protected static Long[] getRange(HttpServletRequest request) {
        String rangeHeader = request.getHeader("Range");
        return getRange(rangeHeader);
    }

    protected static Long[] getRange(String rangeHeader) {
        Long[] range = new Long[]{-1L, -1L};
        System.out.println("Range-header: " + rangeHeader);
        if (rangeHeader != null && !rangeHeader.trim().equals("")) {//断点续载
            /* 请求
             * Range: bytes=786432-
             * Range: bytes=786432-14385736/14385737
             * 返回
             * "Content-Range: bytes x-(y-1)/y" 例如: Content-Range: bytes 16-14385736/14385737 */
            try {
                String[] split = rangeHeader.replace("bytes=", "").split("-");
                if (split.length > 0) {
                    Long posStart = Long.parseLong(split[0]);
                    range[0] = posStart;
                }
                if (split.length > 1) {
                    Long posEnd = Long.parseLong(split[1]);
                    range[1] = posEnd;
                }
            } catch (NumberFormatException e) {
                System.err.println("Range: " + range + " is not a number!");
                e.printStackTrace();
            }
        }
        return range;
    }


}
