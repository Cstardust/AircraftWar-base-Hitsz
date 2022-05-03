package edu.hitsz.application;
import edu.hitsz.gameTemplate.EasyGame;
import edu.hitsz.gameTemplate.Game;
import edu.hitsz.gameTemplate.HellGame;
import edu.hitsz.gameTemplate.MediumGame;
import edu.hitsz.user.User;
import edu.hitsz.user.UserDaoImpl;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.util.ArrayList;


import static edu.hitsz.application.Main.WINDOW_HEIGHT;
import static edu.hitsz.application.Main.WINDOW_WIDTH;

/*
Main（调用GameUI控制界面）一个线程
Game（控制游戏流程）一个线程
每个音乐一个线程

Main线程 通过控件 开启 Game线程
Main线程 等待Game线程结束 跳转到下一个控件
 */

/*
GameUI：负责游戏界面的绘画以及跳转
 */
public class GameUI {

    private static int bgNum;                               //  背景图片选择
    //  访问数据
    private UserDaoImpl userdao;
    //  游戏
    private static Game game;
    //  游戏
    private static String dif = "简单模式";
    public static String getDif() {
        return dif;
    }
    public static void setDif(String x) {
        dif = x;
    }

    //  初始界面
    public static class initBoard
    {
        private static JFrame jf01 = new JFrame("Aircraft war");
        // 创建内容面板，指定布局为 null，则使用绝对布局
        private static JPanel panel = new JPanel(null);
        private static JButton[] btn = new JButton[3];
        private static String[] modes = new String[]{"简单模式","中等模式","困难模式"};
        // 需要选择的条目
        private static String[] listData = new String[]{"开", "关"};
        // 创建一个下拉列表框
        private static final JComboBox<String> comboBox = new JComboBox<String>(listData);
        private static JLabel label01 = new JLabel();
        //  游戏

        public initBoard()
        {
            jf01.setSize(400, 600);
            jf01.setLocationRelativeTo(null);
            jf01.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            //  button模式
            for(int i=0;i<3;++i)
            {
                btn[i] = new JButton(modes[i]);
                btn[i].setBounds(140,10+140*i,100,70);
                int finalI = i;
                btn[i].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dif = modes[finalI];
                        switch (finalI)
                        {
                            case 0 :
                                game = new EasyGame();
                                break;
                            case 1:
                                game = new MediumGame();
                                break;
                            case 2:
                                game = new HellGame();
                        }
                    }
                });
                panel.add(btn[i]);
            }

            // 添加条目选中状态改变的监听器
            comboBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    // 只处理选中的状态
                    MusicThread.setIsMusic( ( true ? comboBox.getSelectedIndex()==0 : false) );
                }
            });

            // 设置默认选中的条目
            comboBox.setSelectedIndex(1);
            comboBox.setBounds(140,400,100,40);
            label01.setText("音效:");
            label01.setBounds(80,400,50,40);
            panel.add(label01);
            // 添加到内容面板
            panel.add(comboBox);

            JButton btn04 = new JButton("start");
            // 设置按钮的界限(坐标和宽高)，设置按钮的坐标为(140, 470)，宽高为 100, 50
            btn04.setBounds(140, 470, 100, 50);
            panel.add(btn04);
            boolean flag = true;
            btn04.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    jf01.dispose();
                    if(game==null){
                        game = new EasyGame();
                    }
                    processUI();
                    if(MusicThread.getIsMusic()) {
                        new MusicThread("src/videos/bgm.wav",true,false).start();
                    }
                }
            });
            // 显示窗口
            jf01.setContentPane(panel);
            jf01.setVisible(true);

        }
    }

    //  游戏进行界面
    public static class ProcessBoard
    {
        public static JFrame jf02;
        public ProcessBoard()
        {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            jf02 = new JFrame("Aircraft War");
            jf02.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            jf02.setResizable(false);
            //设置窗口的大小和位置,居中放置
            jf02.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                    WINDOW_WIDTH, WINDOW_HEIGHT);
            jf02.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jf02.add(game);
            jf02.setVisible(true);
            game.action();
        }
    }

    //  排行榜界面
    public static class RankingTable {
        private UserDaoImpl userdao;
        private JPanel MainPanel = new JPanel();
        private JPanel bottomPanel = new JPanel();
        private JPanel topPanel = new JPanel();
        private JScrollPane tableScrollPanel = new JScrollPane();
        private JLabel headerLabel = new JLabel();
        private JTable scoreTable = new JTable();
        private String user_name;
        String[] columnName = {"名次","玩家","得分","记录时间"};
        String[][]tableData;
        public RankingTable(String name) {
            userdao = new UserDaoImpl(game.getArchivalPath());
            user_name = name;
            //  增加记录
            userdao.doAdd(new User(user_name,game.getScore()));

            //   获取所有记录并排序输出
            ArrayList<User> al = userdao.getAllUsers();
            tableData = new String[al.size()][];
            int cnt = 0;
            for(User u:al)
            {
                tableData[cnt] = new String[]{String.valueOf(cnt+1), u.getName(),String.valueOf(u.getScore()),u.getStrDate()};
                ++cnt;
            }

            headerLabel.setText("难度: "+getDif());
            MainPanel.add(headerLabel);

            //表格模型
            DefaultTableModel model = new DefaultTableModel(tableData, columnName){
                @Override
                public boolean isCellEditable(int row, int col){
                    return false;
                }
            };

            //JTable 并不存储自己的数据，而是从表格模型那里获取它的数据
            scoreTable.setModel(model);
            tableScrollPanel.setViewportView(scoreTable);
            topPanel.add(tableScrollPanel);
            MainPanel.add(topPanel);


            JButton deleteButton = new JButton("删除");
            deleteButton.setSize(100,100);

            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = scoreTable.getSelectedRow();
                    if(row != -1){
                        int op = JOptionPane.showConfirmDialog(null,"删除该条记录","提示",JOptionPane.YES_NO_CANCEL_OPTION);
                        if(op==JOptionPane.YES_OPTION){
                            userdao.doDelete(new User(model.getValueAt(row, 1).toString(),
                                    Integer.valueOf(model.getValueAt(row, 2).toString()),
                                    model.getValueAt(row, 3).toString()));  //  删除
                            model.removeRow(row);
                        }
                    }
                }
            });

            bottomPanel.add(deleteButton);
            MainPanel.add(bottomPanel);
        }

        public void show() {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            JFrame frame = new JFrame("RankingTable");
            frame.setResizable(false);
            frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(MainPanel);
            //设置窗口的大小和位置,居中放置
            frame.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                    WINDOW_WIDTH, WINDOW_HEIGHT);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    userdao.rewrite();              //  重新写入！真正实现增加、删除
                    System.out.println("Bye~");
                }
            });
            frame.setVisible(true);
        }
    }



    //  开始界面控制
    public static void show() {
        initUI();
    }
    //  初始界面
    public static void initUI() {
        new initBoard();
    }
    //  游戏进行的飞行界面
    private static void processUI() {
        // 获得屏幕的分辨率，初始化 Frame
        new ProcessBoard();
    }
    //  游戏结束后的弹窗以及排行榜
    public static void rankingUI()
    {
        String user_name = JOptionPane.showInputDialog(null, "请输入用户名：\n", "Aircraft war", JOptionPane.QUESTION_MESSAGE);
        if(user_name==null||user_name.equals("")){
            user_name = "someone";
        }
        ProcessBoard.jf02.dispose();
        RankingTable s = new RankingTable(user_name);
        s.show();
    }

}


