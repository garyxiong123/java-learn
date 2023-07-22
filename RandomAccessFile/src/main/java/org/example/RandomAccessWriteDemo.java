package org.example;


import java.io.RandomAccessFile;

public class RandomAccessWriteDemo {
    public static void main(String[] args) {
        try {
            // 创建一个RandomAccessFile实例，指定文件路径和访问模式（"rw"为读写模式）
            RandomAccessFile file = new RandomAccessFile(RandomAccessReadDemo.path, "rw");

            // 定位到文件的末尾
            file.seek(file.length());

            // 写入数据到文件
            String data = "Hello, World!";
            file.write(data.getBytes());

            // 关闭RandomAccessFile
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
