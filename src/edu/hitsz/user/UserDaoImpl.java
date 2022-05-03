package edu.hitsz.user;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import static java.nio.file.Files.exists;

public class UserDaoImpl implements UserDao {
    public ArrayList<User> users = new ArrayList<User>();
    private boolean isExist = false;
    private final String FilePath;
    private boolean isModiied = false;

    public UserDaoImpl(String path)  {
        FilePath = path;
        Path dir = Paths.get(FilePath);
        if (!exists(dir)) {
            try {
                Files.createFile(dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            isExist = true;
        }
        init();
    }

    //  初始化list
    private void init()
    {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(FilePath));
        } catch(EOFException e){
            users = new ArrayList<User>();
            return ;
        } catch (IOException e) {
            e.printStackTrace();
        }
        Object obj = null;
        try {
            obj = ois.readObject();
            if(obj instanceof ArrayList) {
                users = (ArrayList<User>) obj;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if(users==null){
            users = new ArrayList<User>();
        }
    }

    /*
      增加对象到users
      不真正写入文件
     */
    @Override
    public void doAdd(User user) {
        isModiied = true;
        users.add(user);
    }

    //  排序后 返回users中的所有对象
    @Override
    public ArrayList<User> getAllUsers() {
        Collections.sort(users);
        return users;
    }

    //  覆盖文件
    public void rewrite() {
        if(isModiied)
        {
            //  将User所有对象重新写入文件
            //  为了实现删除元素/增加元素
            ObjectOutputStream oos = null;
            FileOutputStream fo = null;
            try {
                fo = new FileOutputStream(FilePath);
                oos = new ObjectOutputStream(fo);
                oos.writeObject(users);  //  进行序列化
                oos.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (oos != null) {
                    try {
                        oos.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //  从users中删除
    @Override
    public void doDelete(User u)
    {
        users.remove(u);
    }
}