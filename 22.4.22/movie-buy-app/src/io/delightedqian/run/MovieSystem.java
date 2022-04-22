package io.delightedqian.run;

import io.delightedqian.bean.Business;
import io.delightedqian.bean.Customer;
import io.delightedqian.bean.Movie;
import io.delightedqian.bean.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Project Name: MovieSystem
 * Description:
 * USER: ChenXiaoqian
 * DATE:  2022/4/21
 * TIME:  8:34
 */
public class MovieSystem {
    /* *
    1定义系统的数据容器存储用户数据
    存储很多用户（客户对象 商家对象）
     */
    public static final List<User> ALL_USERS = new ArrayList<>();

    /* *
     2存储系统全部 商家 和 排片 信息
     */

    public static final Map<Business, List<Movie>> ALL_MOVIES = new HashMap<>();


    //准备一个扫描器，静态的，因为此程序中可能会大量使用
    public static final Scanner SYS_SC = new Scanner(System.in);

    //定义一个静态的User类型的变量记住当前登陆成功的用户对象
    public static User loginUser;

    //一个时间转换器
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    //日志记录信息
    public static final Logger LOGGER = LoggerFactory.getLogger("MovieSystem.class");

    /* *
     3、准备一些测试数据
     */
    static {
        Customer c = new Customer();
        c.setLoginName("zyf888");
        c.setPassWord("123456");
        c.setUserName("黑马刘德华");
        c.setSex('男');
        c.setMoney(10000);
        c.setPhone("110110");
        ALL_USERS.add(c);

        Customer c1 = new Customer();
        c1.setLoginName("gzl888");
        c1.setPassWord("123456");
        c1.setUserName("黑马关之琳");
        c1.setSex('女');
        c1.setMoney(2000);
        c1.setPhone("111111");
        ALL_USERS.add(c1);

        Business b = new Business();
        b.setLoginName("baozugong888");
        b.setPassWord("123456");
        b.setUserName("黑马包租公");
        b.setMoney(0);
        b.setSex('男');
        b.setPhone("110110");
        b.setAddress("火星6号2B二层");
        b.setShopName("甜甜圈国际影城");
        ALL_USERS.add(b);
        // 注意，商家一定需要加入到店铺排片信息中去
        List<Movie> movies = new ArrayList<>();
        ALL_MOVIES.put(b, movies); // b = []

        Business b2 = new Business();
        b2.setLoginName("baozupo888");
        b2.setPassWord("123456");
        b2.setUserName("黑马包租婆");
        b2.setMoney(0);
        b2.setSex('女');
        b2.setPhone("110110");
        b2.setAddress("火星8号8B八层");
        b2.setShopName("巧克力国际影城");
        ALL_USERS.add(b2);
        // 注意，商家一定需要加入到店铺排片信息中去
        List<Movie> movies3 = new ArrayList<>();
        ALL_MOVIES.put(b2, movies3); // b2 = []
    }

    public static void main(String[] args) {
        showMain();
    }

    /* *
     首页展示
     */
    private static void showMain() {
        while (true) {
            System.out.println("=============黑马电影首页===============");
            System.out.println("1. 登录");
            System.out.println("2. 用户注册");
            System.out.println("3. 商家注册");
            System.out.println("请输入操作命令：");
            String command = SYS_SC.nextLine();
            switch (command) {
                case "1":
                    //登录。。
                    login();
                    break;
                case "2":
                    //用户注册。。
                    break;
                case "3":
                    //商家注册。。
                    break;
                default:
                    System.out.println("输入的值有误");

            }
        }
    }

    /* *
     用户登录功能
     */
    private static void login() {
        while (true) {
            System.out.println("请输入用户名称");
            String loginName = SYS_SC.nextLine();
            System.out.println("请输入登录密码");
            String passWord = SYS_SC.nextLine();

            //1 根据登录名称查询用户对象
            User u = getUserByLoginName(loginName);
            //判断密码
            if (u != null) {
                if (u.getPassWord().equals(passWord)) {
                    //定义一个静态的User类型的变量记住当前登陆成功的用户对象
                    loginUser = u;
                    LOGGER.info(u.getLoginName() + "登录成功");
                    System.out.println("登录成功");
                    if (u instanceof Business) {
                        //商家登录
                        showBusinessMain();
                    } else {
                        //客户登录
                        showCustomerMain();
                    }
                    //返回
                    return;

                } else {
                    System.out.println("登陆失败,密码有误");
                }
            } else {
                System.out.println("登陆失败,登录名有误");
            }
        }
    }


    /* *
     商家操作页面
     */
    private static void showBusinessMain() {
        while (true) {
            System.out.println("============黑马电影商家界面===================");
            System.out.println(loginUser.getLoginName() + (loginUser.getSex() == '男' ? "先生" : "女士")
                    + "欢迎");
            System.out.println("1、展示详情:");
            System.out.println("2、上架电影:");
            System.out.println("3、下架电影:");
            System.out.println("4、修改电影:");
            System.out.println("5、退出:");

            System.out.println("请输入您要操作的命令：");
            String command = SYS_SC.nextLine();
            switch (command) {
                case "1":
                    // 展示全部排片信息
                    showBusinessInfos();
                    break;
                case "2":
                    // 上架电影信息
                    addMovie();
                    break;
                case "3":
                    // 下架电影信息
                    deleteMovie();
                    break;
                case "4":
                    // 修改电影信息
                    updateMovie();
                    break;
                case "5":
                    System.out.println(loginUser.getLoginName() + "退出成功");
                    return; // 干掉方法
                default:
                    System.out.println("不存在该命令！！");
                    break;
            }
        }
    }

    /* *
    修改影片
     */
    private static void updateMovie() {
        while (true) {
            System.out.println("=============修改影片===============");
            Business business = (Business) loginUser;
            List<Movie> movies = ALL_MOVIES.get(business);
            if (movies == null) {
                System.out.println("暂无影片可以修改");
                return;
            }
            System.out.println("请输入要修改的影片");
            String movieName = SYS_SC.nextLine();

            //查询有无此电影
            Movie movie = getMovieByName(movieName);
            if (movie != null) {
                //修改

                System.out.println("请您输入修改后的片名：");
                String name = SYS_SC.nextLine();
                System.out.println("请您输入修改后的主演：");
                String actor = SYS_SC.nextLine();
                System.out.println("请您输入修改后的时长：");
                String time = SYS_SC.nextLine();
                System.out.println("请您输入修改后的票价：");
                String price = SYS_SC.nextLine();
                System.out.println("请您输入修改后的票数：");
                String totalNumber = SYS_SC.nextLine(); //

                // public Movie(String name, String actor, double time, double price, int number, Date startTime)        // 封装成电影对象 ，加入集合movices中去
                while (true) {
                    try {
                        System.out.println("请您输入影片放映时间：");
                        String stime = SYS_SC.nextLine();
                        movie.setName(name);
                        movie.setActor(actor);
                        movie.setPrice(Double.valueOf(price));
                        movie.setTime(Double.valueOf(time));
                        movie.setNumber(Integer.valueOf(totalNumber));
                        movie.setStartTime(sdf.parse(stime));
                        System.out.println("成功修改");
                        showBusinessInfos();
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        LOGGER.error("时间解析出了问题");
                    }
                }
            } else {
                System.out.println("暂无此影片可以修改");
                System.out.println("要继续修改其他影片吗？ y/n");
                String command = SYS_SC.nextLine();
                switch (command) {
                    case "y":
                        break;
                    default:
                        System.out.println("好的");
                        return;
                }
            }
        }

    }

    /* *
    删除影片
     */
    private static void deleteMovie() {
        while (true) {
            System.out.println("=============删除影片===============");
            Business business = (Business) loginUser;
            List<Movie> movies = ALL_MOVIES.get(business);
            if (movies == null) {
                System.out.println("暂无影片可以删除");
                return;
            }
            System.out.println("请输入要下架的影片");
            String movieName = SYS_SC.nextLine();

            //查询有无此电影
            Movie movie = getMovieByName(movieName);
            if (movie != null) {
                movies.remove(movie);
                System.out.println("影片下架成功" + movie.getName());
                showBusinessInfos();
                return;
            } else {
                System.out.println("暂无此影片可以下架");
                System.out.println("要继续下架其他影片吗？ y/n");
                String command = SYS_SC.nextLine();
                switch (command) {
                    case "y":
                        break;
                    default:
                        System.out.println("好的");
                        return;
                }
            }
        }
    }

    /* *
     查询有无此电影
     */
    public static Movie getMovieByName(String movieName) {
        Business business = (Business) loginUser;
        List<Movie> movies = ALL_MOVIES.get(business);
        for (Movie movie : movies) {
            if (movie.getName().contains(movieName)) {
                return movie;
            }
        }
        return null;
    }
    /* *
     添加影片功能
     */

    private static void addMovie() {
        System.out.println("================上架电影====================");
        // 根据商家对象(就是登录的用户loginUser)，作为Map集合的键 提取对应的值就是其排片信息 ：Map<Business , List<Movie>> ALL_MOVIES
        Business business = (Business) loginUser;
        List<Movie> movies = ALL_MOVIES.get(business);

        System.out.println("请您输入新片名：");
        String name = SYS_SC.nextLine();
        System.out.println("请您输入主演：");
        String actor = SYS_SC.nextLine();
        System.out.println("请您输入时长：");
        String time = SYS_SC.nextLine();
        System.out.println("请您输入票价：");
        String price = SYS_SC.nextLine();
        System.out.println("请您输入票数：");
        String totalNumber = SYS_SC.nextLine(); //

        // public Movie(String name, String actor, double time, double price, int number, Date startTime)        // 封装成电影对象 ，加入集合movices中去
        while (true) {
            try {

                System.out.println("请您输入影片放映时间：");
                String stime = SYS_SC.nextLine();
                Movie movie = new Movie(name, actor, Double.valueOf(time), Double.valueOf(price)
                        , Integer.valueOf(totalNumber), sdf.parse(stime));
                movies.add(movie);
                System.out.println("成功上架了" + "<<" + movie.getName() + ">>");
                return;

            } catch (ParseException e) {
                e.printStackTrace();
                LOGGER.error("时间解析出了问题");
            }
        }

    }


    /* *
    展示当前商家信息
     */
    private static void showBusinessInfos() {
        System.out.println("================商家详情界面================");
        LOGGER.info(loginUser.getUserName() + "正在查看详情界面");
        Business business = (Business) loginUser;
        System.out.println(business.getShopName() + "\t\t电话：" + business.getPhone()
                + "\t\t地址：" + business.getAddress()+"\t\t余额："+business.getMoney());
        //Map集合  通过键取值
        List<Movie> movies = ALL_MOVIES.get(business);
        if (movies.size() > 0) {
            System.out.println("片名\t\t\t主演\t\t时长\t\t评分\t\t票价\t\t余票数量\t\t放映时间");

            for (Movie movie : movies) {
                System.out.println(movie.getName() + "\t\t\t" + movie.getActor() + "\t\t" + movie.getTime() + "\t\t" +
                        movie.getScore() + "\t\t" + movie.getPrice() + "\t\t" + movie.getNumber() + "\t\t" +
                        sdf.format(movie.getStartTime()));
            }
        } else {
            System.out.println("你的店铺当前无片在放映");
        }
    }

    /* *
     客户操作界面
   */
    private static void showCustomerMain() {
        while (true) {
            System.out.println("============黑马电影客户界面===================");
            System.out.println(loginUser.getLoginName() + (loginUser.getSex() == '男' ? "先生" : "女士")
                    + "欢迎"+"\t\t余额"+loginUser.getMoney());
            System.out.println("请您选择要操作的功能：");
            System.out.println("1、展示全部影片信息功能:");
            System.out.println("2、根据电影名称查询电影信息:");
            System.out.println("3、评分功能:");
            System.out.println("4、购票功能:");
            System.out.println("5、退出系统:");
            System.out.println("请输入您要操作的命令：");
            String command = SYS_SC.nextLine();
            switch (command) {
                case "1":
                    // 展示全部排片信息
                    showAllMovies();
                    break;
                case "2":

                    break;
                case "3":
                    // 评分功能

                    break;
                case "4":
                    // 购票功能
                    buyMovie();
                    break;
                case "5":
                    return; // 干掉方法
                default:
                    System.out.println("不存在该命令！！");
                    break;
            }
        }
    }

    /* *
    用户购票功能
     */
    private static void buyMovie() {
        showAllMovies();
        System.out.println("==============用户购票功能================");
        while (true) {
            System.out.println("请输入你要买票的门店");
            String shopName = SYS_SC.nextLine();

            // 1 查询是否存在该商家
            Business business = getBusinessShopName(shopName);
            if (business == null) {
                System.out.println("对不起，没有该店铺，请确认");
            } else {
                //2 此商家全部影片
                List<Movie> movies = ALL_MOVIES.get(business);
                //3 判断是否存在上映的电影
                if (movies.size() > 0) {
                    //4 开始购票
                    while (true) {
                        System.out.println("请你输入需要购票的电影名称");
                        String movieName = SYS_SC.nextLine();

                        //取当前商家下，查询电影对象
                        Movie movie = getMovieByShopAndName(business, movieName);
                        if (movie != null) {
                            //开始购买
                            while (true) {
                                System.out.println("请输入要购买的票数");
                                String number = SYS_SC.nextLine();
                                int buyNumber = Integer.valueOf(number);
                                //判断电影是否够票
                                if (buyNumber <= movie.getNumber()) {
                                    //可以购买
                                    //当前需要花费的金额
                                    double money = BigDecimal.valueOf(movie.getPrice()).multiply(
                                            BigDecimal.valueOf(movie.getNumber())).doubleValue();
                                     if(loginUser.getMoney()>=money){
                                         //可以买
                                         System.out.println("成功购买了"+movie.getName()+buyNumber+"张，总金额"+
                                                 money);
                                         //更新自己/商家的金额
                                         loginUser.setMoney(loginUser.getMoney()-money);
                                         business.setMoney(loginUser.getMoney()+money);
                                         //更新票数
                                         movie.setNumber(movie.getNumber()-buyNumber);
                                         return;
                                     }else{
                                         //钱不够
                                         System.out.println("是否要继续购票？y/n");
                                         String command = SYS_SC.nextLine();
                                         switch (command) {
                                             case "y":
                                                 break;
                                             default:
                                                 System.out.println("好的");
                                                 return;
                                         }
                                     }
                                } else {
                                    //票数不够
                                    System.out.println("你当前最多可买" + movie.getNumber());
                                    System.out.println("是否要继续购票？y/n");
                                    String command = SYS_SC.nextLine();
                                    switch (command) {
                                        case "y":
                                            break;
                                        default:
                                            System.out.println("好的");
                                            return;
                                    }
                                }
                            }
                        } else {
                            System.out.println("电影名称查无");
                        }
                    }

                } else {
                    System.out.println("该电影院关门了");
                    System.out.println("是否要继续购票？y/n");
                    String command = SYS_SC.nextLine();
                    switch (command) {
                        case "y":
                            break;
                        default:
                            System.out.println("好的");
                            return;
                    }
                }
            }
        }
    }
    /* *
    取当前商家下，查询电影对象
     */
    private static Movie getMovieByShopAndName(Business business, String name) {
        List<Movie> movies = ALL_MOVIES.get(business);
        for (Movie movie : movies) {
            if (movie.getName().contains(name)) {
                return movie;
            }
        }
        return null;
    }


    /* *
     根据店铺名称查询商家对象
     */
    private static Business getBusinessShopName(String shopName) {
        Set<Business> businesses = ALL_MOVIES.keySet();
        for (Business business : businesses) {
            if (business.getShopName().equals(shopName)) {
                return business;
            }
        }
        return null;
    }

    /* *
     用户功能：展示全部商家排片信息
     */
    private static void showAllMovies() {
        ALL_MOVIES.forEach((business, movies) -> {
            System.out.println(business.getShopName() + "\t\t电话：" + business.getPhone() + "\t\t地址：" + business.getAddress());
            System.out.println("\t\t\t片名\t\t\t主演\t\t时长\t\t评分\t\t票价\t\t余票数量\t\t放映时间");
            for (Movie movie : movies) {
                System.out.println("\t\t\t" + movie.getName() + "\t\t\t" + movie.getActor() + "\t\t" + movie.getTime() + "\t\t" +
                        movie.getScore() + "\t\t" + movie.getPrice() + "\t\t" + movie.getNumber() + "\t\t" +
                        sdf.format(movie.getStartTime()));
            }
        });
    }

    /*
     判断用户是否存在
     **/
    private static User getUserByLoginName(String loginName) {
        for (User user : ALL_USERS) {
            if (user.getLoginName().equals(loginName)) {
                return user;
            }
        }
        return null;
    }
}
