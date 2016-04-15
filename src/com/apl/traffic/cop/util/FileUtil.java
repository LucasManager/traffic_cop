package com.apl.traffic.cop.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

public class FileUtil {

	/**
	 * 获取文件夹内的所有文件
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static File[] tolistFiles(String filePath) {
		File file = new File(filePath);
		File[] files = file.listFiles();
		return files;
	}

	/**
	 * 使用文件通道的方式复制文件
	 * 
	 * @param s
	 *            源文件
	 * @param t
	 *            复制到的新文件
	 */
	public static boolean fileChannelCopy(String source, String target) {
		File sourceFile = new File(source);
		File targetFile = new File(target);
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		boolean isSuccess = true;
		try {
			fi = new FileInputStream(sourceFile);
			fo = new FileOutputStream(targetFile);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
			isSuccess = true;
		} catch (IOException e) {
			isSuccess = false;
			e.printStackTrace();
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return isSuccess;
	}

	/**
	 * 读取文件内容
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static String readFileByLine(String filePath) throws IOException {
		StringBuffer resultString = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String tempString;
			while ((tempString = reader.readLine())!= null) {
				resultString.append(tempString);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			reader.close();
		}
		return resultString.toString();
	}

	public static void writeFile(String content, String targetPath) {
		FileWriter fileWriter = null;
		try {
			// System.out.println(targetPath);
			fileWriter = new FileWriter(new File(targetPath));
			fileWriter.write(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePathAndName
	 *            String 文件路径及名�?如c:/fqf.txt
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public static void delFile(String filePath) {
		try {
			File file = new File(filePath);
			file.delete();
		} catch (Exception e) {
			System.out.println("删除文件操作出错");
			e.printStackTrace();
		}

	}

	/**
	 * 删除文件夹内所有文件
	 * 
	 * @param filePathAndName
	 *            String 文件路径及名�?如c:/fqf.txt
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public static void delallFile(File[] delfiles) {
		try {
			for (File file : delfiles) {
				file.delete();
			}
		} catch (Exception e) {
			System.out.println("删除文件操作出错");
			e.printStackTrace();
		}
	}
	
	/**
	 * 压缩文件
	 * 
	 * @param filePathAndName
	 *            String 文件路径及名�?如c:/fqf.txt
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public static void compress(String pathName, String srcPathName) {
		File zipFile = new File(pathName);
		File srcdir = new File(srcPathName);
		if (!srcdir.exists())
			throw new RuntimeException(srcPathName + "不存在！");

		Project prj = new Project();
		Zip zip = new Zip();
		zip.setProject(prj);
		zip.setDestFile(zipFile);
		FileSet fileSet = new FileSet();
		fileSet.setProject(prj);
		fileSet.setDir(srcdir);
		// fileSet.setIncludes("**/*.java"); 包括哪些文件或文件夹
		// eg:zip.setIncludes("*.java");
		// fileSet.setExcludes(...); 排除哪些文件或文件夹
		zip.addFileset(fileSet);

		zip.execute();
	}

	//
	// /**
	// * 创建目录
	// *
	// * @param destDirName
	// * 目标目录�?
	// * @return 目录创建成功返回true，否则返回false
	// */
	// public static boolean createDir(String destDirName) {
	// File dir = new File(destDirName);
	// if (dir.exists()) {
	// return false;
	// }
	// if (!destDirName.endsWith(File.separator)) {
	// destDirName = destDirName + File.separator;
	// }
	// // 创建单个目录
	// if (dir.mkdirs()) {
	// return true;
	// } else {
	// return false;
	// }
	// }
	//
	//
	// /**
	// * 读取到字节数�?
	// *
	// * @param filePath
	// * //路径
	// * @throws IOException
	// */
	// public static byte[] getContent(String filePath) throws IOException {
	// File file = new File(filePath);
	// long fileSize = file.length();
	// if (fileSize > Integer.MAX_VALUE) {
	// System.out.println("file too big...");
	// return null;
	// }
	// FileInputStream fi = new FileInputStream(file);
	// byte[] buffer = new byte[(int) fileSize];
	// int offset = 0;
	// int numRead = 0;
	// while (offset < buffer.length && (numRead = fi.read(buffer, offset,
	// buffer.length - offset)) >= 0) {
	// offset += numRead;
	// }
	// // 确保�?��数据均被读取
	// if (offset != buffer.length) {
	// throw new IOException("Could not completely read file " +
	// file.getName());
	// }
	// fi.close();
	// return buffer;
	// }
	//
	//
	// /**
	// * 读取到字节数�?
	// *
	// * @param filePath
	// * @return
	// * @throws IOException
	// */
	// public static byte[] toByteArray2(String filePath) throws IOException {
	//
	// File f = new File(filePath);
	// if (!f.exists()) {
	// throw new FileNotFoundException(filePath);
	// }
	//
	// FileChannel channel = null;
	// FileInputStream fs = null;
	// try {
	// fs = new FileInputStream(f);
	// channel = fs.getChannel();
	// ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
	// while ((channel.read(byteBuffer)) > 0) {
	// // do nothing
	// // System.out.println("reading");
	// }
	// return byteBuffer.array();
	// } catch (IOException e) {
	// e.printStackTrace();
	// throw e;
	// } finally {
	// try {
	// channel.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// try {
	// fs.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// /**
	// * Mapped File way MappedByteBuffer 可以在处理大文件时，提升性能
	// *
	// * @param filename
	// * @return
	// * @throws IOException
	// */
	// public static byte[] toByteArray3(String filePath) throws IOException {
	//
	// FileChannel fc = null;
	// RandomAccessFile rf = null;
	// try {
	// rf = new RandomAccessFile(filePath, "r");
	// fc = rf.getChannel();
	// MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0,
	// fc.size()).load();
	// // System.out.println(byteBuffer.isLoaded());
	// byte[] result = new byte[(int) fc.size()];
	// if (byteBuffer.remaining() > 0) {
	// // System.out.println("remain");
	// byteBuffer.get(result, 0, byteBuffer.remaining());
	// }
	// return result;
	// } catch (IOException e) {
	// e.printStackTrace();
	// throw e;
	// } finally {
	// try {
	// rf.close();
	// fc.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }
}