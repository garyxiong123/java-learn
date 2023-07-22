package org.example;

//java RandomAccessFile 使用demo
//https://blog.csdn.net/qq_27093465/article/details/52178263


//使用Java的RandomAccessFile类，您可以对文件进行随机访问，允许您在文件中定位和操作任意位置的数据。以下是一个使用RandomAccessFile的简单示例：

import java.io.RandomAccessFile;

public class RandomAccessReadDemo {
    public static String path = "/Users/gary/Documents/code/learn/java/daily-test/RandomAccessFile/src/main/java/org/example/file.txt";

    public static void main(String[] args) {
        try {
            // 创建一个RandomAccessFile实例，指定文件路径和访问模式（"r"为只读模式）
            RandomAccessFile file = new RandomAccessFile(path, "r");

            // 读取文件中的字节数据
            byte[] buffer = new byte[1024];
            int bytesRead = file.read(buffer);

            while (bytesRead != -1) {
                // 处理读取的数据（示例：将字节转换为字符串并打印）
                String data = new String(buffer, 0, bytesRead);
                System.out.print(data);

                // 继续读取下一批字节数据
                bytesRead = file.read(buffer);
            }

            // 关闭RandomAccessFile
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
