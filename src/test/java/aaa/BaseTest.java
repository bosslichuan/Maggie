package aaa;

import com.tools.EMailUtil;

import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by Bossli on 2017/6/9.
 */
public class BaseTest {
    public static void main(String[] args) {
        //List<Object> list = DBTool.findDbSource("zcurd_busi", "select `email` from `b_notify_email` ", new String[]{}, new String[]{}, new String[]{});
        /*StringBuilder sb = new StringBuilder("adsa");
        String b = null;
        sb.append(sb).append("121");
        System.out.println(sb);*/
        //mailTest();
        try {
            System.out.println(BaseTest.class.getClassLoader().getResource("").toURI().getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void mailTest() {
        ArrayList<Object> list = new ArrayList<>();
        list.add("lichuan@ishugui.com");
        EMailUtil.sendMail(list, "hhahah", "测试时", EMailUtil.FROM2);
    }
}
