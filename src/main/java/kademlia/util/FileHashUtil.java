package kademlia.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * @author wesleywang
 * @Description:
 * @date 2021/1/21
 */
public class FileHashUtil {

    public static byte[] sha1Hash(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(bytes);
            return messageDigest.digest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String shaHashCode(String filePath) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(filePath);
        BigInteger bigInt = new BigInteger(1, shaHashCode(fis));//1代表绝对值
        return bigInt.toString(16);//转换为16进制
    }

    public static byte[] shaHashCode(InputStream fis) {
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            //分多次将一个文件读入，对于大型文件而言，占用内存比较少。
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            fis.close();
            //转换并返回包含16个元素字节数组,返回数值范围为-128到127
            bytes = md.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

}
