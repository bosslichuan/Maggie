import java.sql.*;

/**
 * Created by Bossli on 2017/5/11.
 */
public class DBtest {

    public static void main(String[] args) {
        try{
            //加载MySql的驱动类
            Class.forName("com.mysql.jdbc.Driver") ;
        }catch(ClassNotFoundException e){
            System.out.println("找不到驱动程序类 ，加载驱动失败！");
            e.printStackTrace() ;
        }
        //连接MySql数据库，用户名和密码都是root
        String url = "jdbc:mysql://101.200.193.169:3306/zcurd_base?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull" ;
        String username = "root" ;
        String password = "aikan" ;
        try{
            Connection con =
                    DriverManager.getConnection(url , username , password ) ;
            Statement statement = con.createStatement();
//            boolean execute = statement.execute("desc v_test");
            ResultSet set = statement.executeQuery("desc v_test");
            while(set.next()){
                System.out.println(set.getString("field"));
            }
//            System.out.println(execute);
        }catch(SQLException se){
            System.out.println("数据库连接失败！");
            se.printStackTrace() ;
        }
    }
}
