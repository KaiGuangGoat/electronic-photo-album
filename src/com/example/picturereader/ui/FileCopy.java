package com.example.picturereader.ui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 把文件从一个目录拷贝到另一个目录
 * @author Karger Wang
 * @date 2014/12/15
 *
 */
public class FileCopy {
	private String targetRoot;//指定的备份目录
	
	public FileCopy(String targetPath) {
		setTargetRoot(targetPath);
	}
	
	public void setTargetRoot(String targetPath){
		if(targetPath.endsWith(File.separator)){
			targetPath = targetPath.substring(0, targetPath.length()-1);
		}
		this.targetRoot = targetPath;
	}
	
	/**
	 * 拷贝文件到指定的目录
	 * @param targetFileName
	 * @param fromFile   原文件
	 * @return
	 */
	public boolean copyFrom(String targetFileName,File fromFile) {
		File targetFile = new File(targetRoot+File.separator+ targetFileName);
		
		return copyFrom(targetFile, fromFile);
	}
	/**
	 * 拷贝文件内容到目标文件
	 * @param targetFile 目标文件
	 * @param fromFile   原文件
	 * @return
	 */
	public boolean copyFrom(File targetFile,File fromFile){
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		
		try {
			isFileLegal(fromFile);
			
			if(!targetFile.exists()){
				targetFile.getParentFile().mkdirs();
				targetFile.createNewFile();
			}
			
			in = new BufferedInputStream(new FileInputStream(fromFile));
			out = new BufferedOutputStream(new FileOutputStream(targetFile));
			
			byte[] buffer = new byte[1024];
			int bytes = 0;
			while((bytes = in.read(buffer))!=-1){
				out.write(buffer,0,bytes);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
				try {
					in.close();
					out.flush();
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			return false;
		}
		try {
			in.close();
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}	
		
		return true;
	}
	/**
	 * 需要拷贝的文件必须存在且不能为目录
	 * @param file
	 */
	private void isFileLegal(File file){
		if(!file.exists()){
			throw new IllegalArgumentException("The File is not exits!");
		}
		
		if(file.isDirectory()){
			throw new IllegalArgumentException("The File is directory!");
		}
	}
	
	public static void main(String[] args) {
		FileCopy test = new FileCopy("D:\\t1");
		File targetFile = new File("D:\\t1\\test.txt");
		File fromFile = new File("D:\\test\\myTest.txt");
		test.copyFrom(targetFile, fromFile);
	} 
}
