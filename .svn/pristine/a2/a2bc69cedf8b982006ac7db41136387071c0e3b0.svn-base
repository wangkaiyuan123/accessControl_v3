/**
 * 
 */
package com.dhht.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 文件管理工具类
 * 
 * @author zhaoxl
 * 
 */
public class UploadFileUtils {
    public static final String SYNC_ERROR_FILE_PATH_KEY = "syncErrors";//
    
    private static Properties directoryProperties;
    private static String rootDirectory;
    static {// 初始化配置路径信息
        if(directoryProperties == null) {
            try {
                BufferedInputStream inputStream = new BufferedInputStream(ResourceUtils.getResourceAsStream("config.properties"));
                directoryProperties = new Properties();
                directoryProperties.load(inputStream);
                rootDirectory = directoryProperties.getProperty("rootDirectory");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取指定key的文件夹绝对路径
     * 
     * @param key 文件夹key
     * @return 对应的文件夹绝对路径信息或null（指定的key对应的文件夹不存在时）
     */
    public static String getDirectory(String key) {
        String directory = directoryProperties.getProperty(key);  // ${rootDirectory}/userRegister
        if(directory.indexOf("${rootDirectory}") >= 0) {
            directory = StringUtils.replace(directory, "${rootDirectory}", rootDirectory);  //将后面的替换掉前面的字符串
        }
        return directory;
    }

    /**
     * 获取文件全名
     * 
     * @param directroyKey 文件夹key
     * @param fileName 文件名
     * @return 文件绝对路径名：directroyKey对应的文件夹的绝对路径 + "/" + fileName
     * @throws NullPointerException 找不到对应key的文件夹时
     */
    public static String getFullNameByDirKey(String directroyKey, String fileName) throws NullPointerException {
        String directory = getDirectory(directroyKey);
        if(directory == null) {
            throw new NullPointerException("找不到对应文件夹");
        }
        return directory + "/" + fileName;
    }

    /**
     * 通过目标目录键的方式保存文件 <br />
     * 将源文件复制到targetDirKey对应的目录下名字为targetFileName的文件中
     * 
     * @param targetDirKey 目标文件夹key
     * @param targetFileName 目标文件名
     * @param srcFile 源文件
     * @throws NullPointerException {@link #getFullNameByDirKey(String, String)}
     * @throws FileNotFoundException {@link #copyFile(File, String)}
     * @throws FileExistsException {@link #copyFile(File, String)}
     * @throws IOException {@link #copyFile(File, String)}
     */
    public static void saveUploadFileByDirKey(String targetDirKey, String targetFileName, File srcFile) throws NullPointerException, FileExistsException, IOException {
        String targetPathName = getFullNameByDirKey(targetDirKey, targetFileName);
        copyFile(srcFile, targetPathName);
    }

    /**
     * 保存上传的文件 <br />
     * 将源文件复制到targetPathName对应的文件中
     * 
     * @param srcFile 源文件
     * @param targetPathName 目标文件绝对路径
     * @throws IOException {@link FileUtils#copyFile(File, File)}
     * @throws FileExistsException 目标文件已存在时
     * @throws FileNotFoundException 源文件不存在时
     */
    public static void copyFile(File srcFile, String targetPathName) throws FileExistsException, FileNotFoundException, IOException {
        if(!srcFile.exists()) {
            throw new FileNotFoundException("源文件不存在");
        }
        File targetFile = new File(targetPathName);
        if(targetFile.exists()) {
            throw new FileExistsException("文件已存在");
        }
        FileUtils.copyFile(srcFile, targetFile);
    }
    
    /**
     * 根据byte数组，生成文件 
     * @author: yeshengde
     * @param: bfile
     * @param: filePath
     * @param: fileName   
     * @return: void   
     * @date: 2017-5-7 下午7:09:19
     * @throws
     */
    public static void saveByte(byte[] bfile, String filePath,String fileName) {  
        BufferedOutputStream bos = null;  
        FileOutputStream fos = null;  
        File file = null;  
        try {  
            File dir = new File(filePath);  
            if(!dir.exists()&&!dir.isDirectory()){//判断文件目录是否存在  
                dir.mkdirs();  
            }  
            file = new File(filePath+"/"+fileName);  
            fos = new FileOutputStream(file);  
            bos = new BufferedOutputStream(fos);  
            bos.write(bfile);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (bos != null) {  
                try {  
                    bos.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
            if (fos != null) {  
                try {  
                    fos.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
        }  
    } 
    
    /**
     * 复制文件<br />
     * 将sourcePathName对应的文件，复制到targetPathName对应的文件
     * 
     * @param sourceDirectroyKey 原文件夹key
     * @param targetDirectroy 目标文件夹
     * @param sourceFileName 源文件名
     * @param targetFileName 目标文件名
     * @throws FileExistsException {@link #copyFile(File, String)}
     * @throws FileNotFoundException {@link #copyFile(File, String)}
     * @throws IOException {@link #copyFile(File, String)}
     */
    public static void copyFile(String sourcePathName, String targetPathName) throws FileExistsException, FileNotFoundException, IOException {
        File srcFile = new File(sourcePathName);
        copyFile(srcFile, targetPathName);
    }

    /**
     * 获取已上传的文件或文件夹
     * 
     * @param directroyKey 文件夹key
     * @param fileName 文件名
     * @return directroyKey对应的文件夹下，名称为fileName的文件或文件夹或null（找不到对应文件时）
     * @throws NullPointerException {@link #getFullNameByDirKey(String, String)}
     */
    public static File getFileByDirKey(String directroyKey, String fileName) throws NullPointerException {
        File file = new File(getFullNameByDirKey(directroyKey, fileName));
        if(file.exists()) {
            return file;
        }
        return null;
    }

    /**
     * 删除已上传的文件 或文件夹<br />
     * 删除directroyKey对应的文件夹下，名称为fileName的文件或文件夹
     * 
     * @param directroyKey 文件夹key
     * @param fileName 文件名
     * @return true:成功删除, false:其他
     * @throws NullPointerException {@link #getFullNameByDirKey(String, String)}
     * @throws IOException {@link #deleteFile(String)}
     */
    public static boolean deleteFileByDirKey(String directroyKey, String fileName) throws NullPointerException, IOException {
        String filePathName = getFullNameByDirKey(directroyKey, fileName);
        return deleteFile(filePathName);
    }

    /**
     * 删除文件或文件夹 <br />
     * 将pathName指定的文件或文件夹（包括子文件夹）删除
     * 
     * @param pathName 文件或文件夹的绝对路径
     * @return true:成功删除, false:其他
     * @throws IOException {@link FileUtils#deleteDirectory(File)}
     */
    public static boolean deleteFile(String pathName) throws IOException {
        File file = new File(pathName);
        if(file.isDirectory()) {
            FileUtils.deleteDirectory(file);
            return true;
        } else {
            return file.delete();
        }
    }

    /**
     * 移动文件 <br />
     * 将sourceFilePathName对应的文件，移动到targetFilePathName对应的文件中
     * 
     * @param sourceFilePathName 源文件绝对路径
     * @param targetFilePathName 目标文件绝对路径
     * @throws IOException {@link FileUtils#moveFile(File, File)}
     */
    public static void moveFile(String sourceFilePathName, String targetFilePathName) throws IOException {
        FileUtils.moveFile(new File(sourceFilePathName), new File(targetFilePathName));
    }

    /**
     * 移动文件
     * @param sourceFile            源文件
     * @param targetFilePathName    目标文件绝对路径
     * @throws IOException
     */
    public static void moveFile(File sourceFile,String targetFilePathName) throws IOException{
        FileUtils.moveFile(sourceFile, new File(targetFilePathName));
    }
    
    /**
     * 移动文件 <br />
     * 将sourceDirectroyKey对应的文件夹下，名称为sourceFileName的文件移动到targetDirectroyKey对应的文件夹下，名称为sourceFileName的文件中
     * 
     * @param sourceDirectroyKey 源文件夹key
     * @param targetDirectroyKey 目标文件夹key
     * @param sourceFileName 源文件名
     * @throws NullPointerException {@link #moveFile(String, String, String, String)}
     * @throws IOException {@link #moveFile(String, String, String, String)}
     */
    public static void moveFile(String sourceDirectroyKey, String targetDirectroyKey, String sourceFileName) throws NullPointerException, IOException {
        moveFile(sourceDirectroyKey, targetDirectroyKey, sourceFileName, sourceFileName);
    }

    /**
     * 移动文件 <br />
     * 将sourceDirectroyKey对应的文件夹下，名称为sourceFileName的文件移动到targetDirectroyKey对应的文件夹下，名称为targetFileName的文件中
     * 
     * @param sourceDirectroyKey 源文件夹key
     * @param targetDirectroyKey 目标文件夹key
     * @param sourceFileName 源文件名
     * @param targetFileName 目标文件名
     * @throws NullPointerException {@link #getFullNameByDirKey(String, String)}
     * @throws IOException {@link #moveFile(String, String)}
     */
    public static void moveFile(String sourceDirectroyKey, String targetDirectroyKey, String sourceFileName, String targetFileName) throws NullPointerException, IOException {
        moveFile(getFullNameByDirKey(sourceDirectroyKey, sourceFileName), getFullNameByDirKey(targetDirectroyKey, targetFileName));
    }

    /**
     * 移动文件夹 <br />
     * 将sourceFolderPathName对应的文件夹移动到targetFolderPathName对应的文件夹下
     * 
     * @param sourceFolderPathName 源文件夹绝对路径
     * @param targetFolderPathName 目标件夹绝对路径
     * @throws IOException {@link FileUtils#moveDirectoryToDirectory}
     */
    public static void moveFolder(String sourceFolderPathName, String targetFolderPathName) throws IOException {
        FileUtils.moveDirectoryToDirectory(new File(sourceFolderPathName), new File(targetFolderPathName), true);
    }

    /**
     * 移动文件夹<br />
     * 将sourceDirectroyKey对应的文件夹下，名称为sourceFolderName的文件夹移动到targetDirectroyKey对应的文件夹下，名称为targetFolderName的文件夹中
     * 
     * @param sourceDirectroyKey 源文件夹根目录名字
     * @param targetDirectroyKey 目标文件夹根目录名字
     * @param sourceFolderName 源文件夹路名称
     * @param targetFolderName 目标文件夹名称
     * @throws NullPointerException {@link #getFullNameByDirKey(String, String)}
     * @throws IOException {@link #moveFolder(String, String)}
     */
    public static void moveFolder(String sourceDirectroyKey, String targetDirectroyKey, String sourceFolderName, String targetFolderName) throws NullPointerException, IOException {
        moveFolder(getFullNameByDirKey(sourceDirectroyKey, sourceFolderName), getFullNameByDirKey(targetDirectroyKey, targetFolderName));
    }

    /**
     * 更改文件名 <br />
     * 将directroy对应的文件夹下，名称为oldName的文件或文件夹重命名为newName
     * 
     * @param directroy 文件所在目录
     * @param oldName 原名称
     * @param newName 新名称
     * @throws FileExistsException directroy对应的文件夹下对应newName的文件或文件夹已存在时
     */
    public static void renameFile(String directroy, String oldName, String newName) throws FileExistsException {
        File oldFile = new File(directroy + "/" + oldName);
        File newFile = new File(directroy + "/" + newName);
        if(newFile.exists()) {
            throw new FileExistsException("文件已存在");
        }
        oldFile.renameTo(newFile);
    }

    /**
     * 创建文件夹<br />
     * 如果文件夹的名字已存在，则自动按"(*)"的方式递增名称。<br />
     * 例如：folderName = "新建文件夹"和"新建文件夹 (2)"的文件夹已存在，且"新建文件夹 (3)"不存在，则创建后的新文件夹名称为"新建文件夹(3)"
     * 
     * @param directroy 父文件夹
     * @param folderInfo 要创建的文件夹信息(可以是一个包含路径信息的名称，也可以是一个真实的名称)
     * @return 创建后的文件夹名称
     */
    public static String createFolder(String directroy, String folderInfo) {
        String viablePathName = getViableFileName(directroy, folderInfo, "");// 能创建的文件夹的实际名称

        // 创建文件夹
        File newFolder = new File(directroy + "/" + viablePathName);
        newFolder.mkdirs();
        return viablePathName;
    }

    /**
     * 获取可用文件名<br/>
     * 如果指定目录下不存在期望全称（expectPathName + fileExt）的文件夹或文件，则返回期望名称。<br />
     * 否则返回按照"(*)"的方式递增，直到唯一的名称。<br />
     * 例如：expectName = "新建文件夹"、fileExt=""，且指定path下"新建文件夹"和"新建文件夹 (2)"的文件夹已存在，而"新建文件夹 (3)"不存在，则返回"新建文件夹 (3)",<br />
     * expectName = "文件"、 fileExt=".file"， 且指定path下"文件.file"和"文件 (2).file"的文件夹已存在，而"文件 (3).file"不存在，则返回"文件 (3).file",<br />
     * 
     * @param directroy 要获取名字的文件的上级目录
     * @param expectName 期望的文件名(不包含扩展名)
     * @param fileExt 文件扩展名（文件夹时，给空串""）
     * @return
     */
    public static String getViableFileName(String directroy, String expectName, String fileExt) {
        class NameGenerater {
            public String getFileName(String baseName, String ext) {
                if(StringUtils.isBlank(ext)) {
                    return baseName;
                }
                return baseName + "." + ext;
            }
        }

        NameGenerater ng = new NameGenerater();

        String viableName = ng.getFileName(expectName, fileExt);
        if(!isExist(directroy + "/" + viableName)) {
            return viableName;
        }
        int index = 2;
        while (true) {
            viableName = ng.getFileName(expectName + " (" + index + ")", fileExt);// 注意，为了显示美观，左边括号前加了一个空格
            boolean isExist = isExist(directroy + "/" + viableName);
            if(!isExist) {
                return viableName;
            }
            index++;
        }

    }

    /**
     * 判断一个文件是否存在
     * 
     * @param fullName 完成文件名
     * @return 存在：true、不存在：false
     */
    public static boolean isExist(String fullName) {
        File f = new File(fullName);
        if(f.exists()) {
            return true;
        } else {
            return false;
        }
    }
}
