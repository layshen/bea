import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.epiboly.bea2.util.RSAUtil;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.UnsupportedEncodingException;

/**
 * @author vemao
 * @time 2023/1/16
 * @describe
 */
@RunWith(AndroidJUnit4.class)
public class AppTestTest extends TestCase {

    @Test
    public void testRSAUtil() throws UnsupportedEncodingException {
        android.util.Log.d("mao","私钥:" + RSAUtil.getPrivateKey());
        android.util.Log.d("mao","公钥:" + RSAUtil.getPublicKey());
        byte[] usernames = RSAUtil.encrypt("username");
        android.util.Log.d("mao","解密:" + RSAUtil.decrypt(usernames));
    }
}