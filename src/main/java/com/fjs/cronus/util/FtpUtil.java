package com.fjs.cronus.util;

import java.io.*;
import java.util.Random;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import org.joda.time.DateTime;
import sun.misc.BASE64Encoder;

public class FtpUtil {

	/**
	 * Description: 向FTP服务器上传文件
	 * @param host FTP服务器hostname
	 * @param port FTP服务器端口
	 * @param username FTP登录账号
	 * @param password FTP登录密码
	 * @param basePath FTP服务器基础目录
	 * @param filePath FTP服务器文件存放路径。例如分日期存放：/2015/01/01。文件的路径为basePath+filePath
	 * @param filename 上传到FTP服务器上的文件名
	 * @param input 输入流
	 * @return 成功返回true，否则返回false
	 */
	public static boolean uploadFile(String host, int port, String username, String password, String basePath,
									 String filePath, String filename, InputStream input) {
		boolean result = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(host);// 连接FTP服务器
			// 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			ftp.setRemoteVerificationEnabled(false);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return result;
			}
			//切换到上传目录
			if (!ftp.changeWorkingDirectory(basePath+"/"+filePath)) {
				//如果目录不存在创建目录
				String[] dirs = filePath.split("/");
				String tempPath = basePath;
				for (String dir : dirs) {
					if (null == dir || "".equals(dir))
						continue;
					tempPath += "/" + dir;
					if (!ftp.changeWorkingDirectory(tempPath)) {
						if (!ftp.makeDirectory(tempPath)) {
							return result;
						} else {
							ftp.changeWorkingDirectory(tempPath);
						}
					}
				}
			}
			//设置上传文件的类型为二进制类型
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			ftp.setBufferSize(1024);
			ftp.setControlEncoding("utf-8");
			//ftp.enterRemotePassiveMode();
			//上传文件
			if (!ftp.storeFile(filename, input)) {
				return result;
			}
			input.close();
			ftp.logout();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}

	/**
	 * Description: 从FTP服务器下载文件
	 * @param host FTP服务器hostname
	 * @param port FTP服务器端口
	 * @param username FTP登录账号
	 * @param password FTP登录密码
	 * @param remotePath FTP服务器上的相对路径
	 * @param fileName 要下载的文件名
	 * @param localPath 下载后保存到本地的路径
	 * @return
	 */
	public static boolean downloadFile(String host, int port, String username, String password, String remotePath,
									   String fileName, String localPath) {
		boolean result = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(host);
			// 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return result;
			}
			ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				System.out.println(ff.getName());
				if (ff.getName().equals(fileName)) {
					File localFile = new File(localPath + "/" + ff.getName());
					ftp.setFileType(FTP.BINARY_FILE_TYPE);
					OutputStream is = new FileOutputStream(localFile);
					ftp.retrieveFile(ff.getName(), is);
					is.close();
				}
			}

			ftp.logout();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}

	public static String getInputStream(String host, int port, String username, String password, String remotePath,
										String fileName) {
		FTPClient ftp = new FTPClient();
		//InputStream inputStream = null;
	/*	ftp.setDefaultTimeout(3*1000);
        ftp.setDataTimeout(3*1000);*/
		byte[] bytes = null;
		try {
			int reply;
			ftp.connect(host);
			// 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			ftp.setBufferSize(1024);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return null;
			}
			ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
			//FTPFile[] fs = ftp.listFiles();
			/*for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					 ftp.setFileType(FTP.BINARY_FILE_TYPE);
					 ftp.enterLocalPassiveMode();
					 inputStream = ftp.retrieveFileStream(new String(ff.getName().getBytes("UTF-8"), "ISO-8859-1"));
                    //转byte数组
					System.out.println(inputStream.available());
					System.out.println("*********************");
					bytes = input2byte(inputStream);
					//System.out.println(bytes.length);
					inputStream.close();
				}
			}*/
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();
			ftp.setRemoteVerificationEnabled(false);
			String file = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");

			//inputStream = ftp.retrieveFileStream(file);
			//转byte数组
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ftp.retrieveFile(file, outputStream);
			//InputStream inputstream = new ByteArrayInputStream(outputStream.toByteArray());
			//System.out.println(inputStream.available());
			/*byte[] buf = new byte[inputStream.available()];
			int bufsize = 0;
			while ((bufsize = inputStream.read(buf, 0, buf.length)) != -1) {
				byteOut.write(buf, 0, bufsize);
			}*/
			bytes = outputStream.toByteArray();
			System.out.println(bytes.length);
			outputStream.close();
			//inputStream.close();
			ftp.logout();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				}catch (IOException e){

				}
			}
		}
		return new BASE64Encoder().encode(bytes) ;
	}

	public static boolean delete(String host, int port, String username, String password, String remotePath,
								 String fileName){
		boolean flag = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(host);
			// 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			ftp.setBufferSize(1024);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return flag;
			}
			ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
			//开始删除文件名
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					ftp.setFileType(FTP.BINARY_FILE_TYPE);
					flag = ftp.deleteFile(new String(ff.getName().getBytes("UTF-8"), "ISO-8859-1"));
					return flag;
				}
			}

			ftp.logout();
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return  flag;
	}

	/**
	 *  文件转byte
	 * @param inStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] input2byte(InputStream inStream) throws IOException {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[inStream.available()];
		int rc = 0;
		while ((rc = inStream.read(buff, 0, buff.length)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		byte[] in2b = swapStream.toByteArray();
		swapStream.close();
		return in2b;
	}
	public static void main(String[] args) {
		try {
	/*		long millis = System.currentTimeMillis();
			//long millis = System.nanoTime();
			//加上三位随机数
			Random random = new Random();
			int end3 = random.nextInt(999);
			//如果不足三位前面补0 图片新名称
			String name = millis + String.format("%03d", end3) +".jpg";
	        FileInputStream in=new FileInputStream(new File("D:\\1.jpg"));
			String imagePath = new DateTime().toString("yyyy/MM/dd");
	        boolean flag = uploadFile("192.168.1.124", 21, "zhanglei", "B4juNEg5", "/Uploads/",imagePath, name, in);
	        System.out.println(flag);*/
			//for (int i = 0;i<10; i++) {
			//String bytes = getInputStream("192.168.1.124", 21, "zhanglei", "B4juNEg5", "/Uploads/2017/10/09/", "4.jpg");
			// System.out.println(bytes.length());
			//}
	     	/*InputStream fis = FileBase64ConvertUitl.decoderBase64File(bytes);
			long millis = System.currentTimeMillis();
			//long millis = System.nanoTime();
			//加上三位随机数
			Random random = new Random();
			int end3 = random.nextInt(999);
			//如果不足三位前面补0 图片新名称
			String name = millis + String.format("%03d", end3) +".jpg";
			//FileInputStream in=new FileInputStream(new File("D:\\1.jpg"));
			String imagePath = new DateTime().toString("yyyy/MM/dd");
			boolean flag = uploadFile("192.168.1.124", 21, "zhanglei", "B4juNEg5", "/Uploads",imagePath, name, fis);
	        //缩略图
			*//*FileInputStream in=new FileInputStream(new File("D:\\1.jpg"));
			String base64 = ImageUtil.compressImage(in,300,300);
			InputStream is = FileBase64ConvertUitl.decoderBase64File(base64);
			boolean flag = uploadFile("192.168.1.124", 21, "zhanglei", "B4juNEg5", "/crmJavaFile/ftpuser/core/","2017/09/26", "1_S.jpg", is);*//**//*
			//boolean flag1 =	downloadFile("192.168.1.124", 21, "zhanglei", "B4juNEg5", "/Uploads/2017/10/09/","1507516521889432.jpg", "E:\\");
			System.out.println(flag);*//*
			System.out.println(flag);*/
			//boolean flag = delete("192.168.1.124", 21, "zhanglei", "B4juNEg5", "/Uploads/2017/10/09/", "4.jpg");
			/*FileInputStream in=new FileInputStream(new File("D:\\1.jpg"));
			String imagePath = new DateTime().toString("yyyy/MM/dd");
			boolean flag = uploadFile("192.168.1.124", 21, "zhanglei", "B4juNEg5", "/Uploads",imagePath, "dsf.jpg", in);
            System.out.println(flag);*/

			/*String bytes = getInputStream("192.168.1.124", 21, "zhanglei", "B4juNEg5", "/Uploads/2017/10/21/", "1508572674522267.jpg");
			System.out.println(bytes.length());*/
			/*long millis = System.currentTimeMillis();
			Random random = new Random();
			int end3 = random.nextInt(999);
			//如果不足三位前面补0 图片新名称
			String name = millis + String.format("%03d", end3) +".jpg";
			FileInputStream in=new FileInputStream(new File("D:\\1.jpg"));
			String imagePath = new DateTime().toString("yyyy/MM/dd");
			boolean flag = uploadFile("192.168.1.124", 21, "zhanglei", "B4juNEg5", "/Uploads/",imagePath, name, in);
			System.out.println(flag);*/
		/*	FileInputStream in=new FileInputStream(new File("D:\\1.jpg"));
			String image64 = FileBase64ConvertUitl.encodeBase64File(in);
			System.out.println(image64);*/
			/*String bytes = getInputStream("192.168.1.124", 21, "zhanglei", "B4juNEg5", "/Uploads/2017/11/14/", "1510653839270371.png");
			System.out.println(bytes.length());*/
		/*	String base64="R0lGODlhoAAyAPIAAAAAADeF+Ge1il/moIpTx////wAAAAAAACwAAAAAoAAyAEII/wABCBxIsKDB\n" +
					"gwgTKlzIsKHDhxAjIixAsaLFiwEyYtRYcYDHjwMEXiwgoOTIkQRSWgSQksDJigBeypxJs2aBmDZn\n" +
					"irSJE2fOmz1//tx5kqjQiz6PKq25MynMpVChDoxKtepSozKdWo2qdavXr1m7jhR7letNpGB1qh2q\n" +
					"dCpVrGm/ko17dO5KuxYzBti4t6NHv39HlhxM2GLLi4fppsVLES5MxhT19i0g2eJHwCcHX9RMsaXL\n" +
					"zofFQl6ruO7ot0jdlvZ6milBoURFrzZ9sKhqtLPB3lbcOuzT3HZb9z7reCzQ3Km7Dt8avG7O4sij\n" +
					"S89KE/rdn3r5Tr1cgLtgkxULg//+jDPxy+V00T/GzrFidsADugd+zJkkYQEVzY8nPT3s7uenSSbg\n" +
					"RR8F5V1499U3nmcq/dYUc/35xlZZ6rkmYVkRjlXhcxnq1uF1jRX1oYgjdhiTdb+VeNZiGyKHYoom\n" +
					"rthiVb2p9yJrFWI1Y3UMwXbVjTvSGGKEzbUFIE+pffgahx6ydqSKUJZG1o2Njfaee+3JFx98L92H\n" +
					"UoP5gRnleRbqZGWWlGXp3YEIJgjefoaJaRyUpzlGZZqTRdYeAAWy2eZ3cX52ln5zwnbnhG+dmSee\n" +
					"MIEEElA71ZdgmILCSSadh9rG3qJXaonZZm9Oaul4U1q1Y44BCtiplj356aZ4CzKo+JRRLQaZaF0D\n" +
					"jnTZiZ5mBuuXLUGXKXWYImrainEJm2OxwEFqa7M0/jfUsySOaWSJDxZq7bbSZautiqJJy61UPiln\n" +
					"LWPDjnsXXtRK9aOm6mqIJJ28xbvevEoSBy6R7cKIWlD7OkeuixCuRDB/+LKVrllmJdUvbpcaCyC6\n" +
					"TVIYI7FMTksxvcj2V6SPPG18nMdLPlxdxTxyZTLKBZs6sUQwxyzzzDTXvFBAADs=";
			InputStream is = FileBase64ConvertUitl.decoderBase64File(base64);
			//InputStream is = FileBase64ConvertUitl.decoderBase64File(base64);
			boolean flag = uploadFile("192.168.1.124", 21, "zhanglei", "B4juNEg5", "/Uploads","2017/11/22", "zhanglei.jpg", is);
			System.out.println(flag);*/
			FileInputStream in=new FileInputStream(new File("D:\\441876515053529363.png"));
			String base64File= FileBase64ConvertUitl.encodeBase64File(in);
			System.out.println(base64File);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
