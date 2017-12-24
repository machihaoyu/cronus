package com.fjs.cronus.util;

import java.io.*;
import java.util.Base64;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

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
			ftp.enterLocalPassiveMode();
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
		return Base64.getEncoder().encodeToString(bytes);
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
			ftp.enterLocalPassiveMode();
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

	public static boolean uploadFileClient(String host, int port, String username, String password, String basePath,
									 String filePath, String filename, InputStream input) {
		boolean result = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(host);// 连接FTP服务器
			// 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
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

			ftp.setBufferSize(1024);
			ftp.setControlEncoding("utf-8");
			ftp.enterLocalPassiveMode();
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
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
	public  static  boolean  uploadClient(String base64,String host, int port, String username, String password, String basePath,
								 String filePath, String filename, InputStream input) {
		boolean result = false;
		try {
			InputStream inputStream = FileBase64ConvertUitl.decoderBase64File(base64);
			boolean flag = uploadFile(host, port, username, password, basePath,filePath, filename, inputStream);
			return  flag;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  result;
	}


}
