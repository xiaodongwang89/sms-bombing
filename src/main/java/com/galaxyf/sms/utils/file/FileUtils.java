package com.galaxyf.sms.utils.file;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;

public class FileUtils {

    /**
     * 用戶名，用戶行為，以key-value方式存储 用户行为 以json方式存储
     * 
     * @throws IOException
     * 
     * @throws InterruptedException
     */
    public static String readFileByLines(String fileName) {
        BufferedReader reader = null;
        String tempString = null;
        StringBuilder result = new StringBuilder();
        try {
            File file = new File(fileName);

            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");

            reader = new BufferedReader(isr);

            while ((tempString = reader.readLine()) != null) {
                result.append(tempString);
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return result.toString();
    }

    public static byte[] InputStream2ByteArray(String filePath) throws IOException {

        InputStream in = new FileInputStream(filePath);
        byte[] data = toByteArray(in);
        in.close();

        return data;
    }

    static byte[] toByteArray(InputStream in) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    /**
     * 根据地址获得数据的字节流
     * 
     * @param strUrl
     *            网络连接地址
     * @return
     */
    public static byte[] getImageFromNetByUrl(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10 * 1000);
            InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据
            byte[] btImg = readInputStream(inStream);// 得到图片的二进制数据
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从输入流中获取数据
     * 
     * @param inStream
     *            输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 将图片写入到磁盘
     * 
     * @param img
     *            图片数据流
     * @param filePath
     *            文件保存时的名称
     */
    public static void writeImageToDisk(byte[] img, String filePath) {
        try {
            File file = new File(filePath);
            FileOutputStream fops = new FileOutputStream(file);
            fops.write(img);
            fops.flush();
            fops.close();
            System.out.println("图片已经写入到临时目录盘");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取流
     * @return
     */
    public static byte[] getStream(String dealStream) {
        byte[] b = null;
        if (dealStream != null && !"".equals(dealStream)) {
            String[] array = dealStream.split(",");
            b = new byte[array.length];
            for (int i = 0; i < array.length; i++) {
                b[i] = Byte.parseByte(array[i]);
            }
        }
        return b;
    }

    /**
     * 转换反斜杠为斜杠
     * @autor cll
     * @desc 方法描述
     * @param path
     * @return
     */
    public static String convertSlash(String path) {
        if (StringUtils.isNotBlank(path)) {
            return path.replaceAll("\\\\", "/").replaceAll("//", "/");
        } else {
            return path;
        }
    }
}
