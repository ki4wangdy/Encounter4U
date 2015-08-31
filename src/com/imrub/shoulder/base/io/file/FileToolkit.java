package com.imrub.shoulder.base.io.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class FileToolkit {
	
	public static boolean isExist(String path) {
		if(path == null || path.length() == 0){
			return false;
		}
		return new File(path).exists() ? true : false;
	}

	public static void writeBufferToFile(String file, byte[] buffer) {
		if(isExist(file)){
			File f = new File(file);
			OutputStream os = null;
			try {
				os = new FileOutputStream(f);
				os.write(buffer);
				os.flush();
			} catch (IOException e) {
			} finally{
				if(os != null){
					try{
						os.close();
					}catch(Exception e){
					}
				}
			}
		}
	}

	public static byte[] readBufferFromFile(String file) {
		if(isExist(file)){
			File f = new File(file);
			InputStream is = null;
			try {
				is = new FileInputStream(f);
				int length = is.available();
				byte[] buffer = new byte[length];
				is.read(buffer);
				return buffer;
			} catch (Exception e) {
			} finally{
				if(is != null){
					try{
						is.close();
					}catch(Exception e){
					}
				}
			}
		}
		return null;
	}

	public static boolean deleteFile(String file) {
		if(isExist(file)){
			return new File(file).delete();
		}
		return false;
	}

	public static void deleteDir(String dir) {
		if(isExist(dir)){
			File fileDir = new File(dir);
			if(fileDir.isDirectory()){
				for(File f : fileDir.listFiles()){
					if(f.isDirectory()){
						deleteDir(f.getAbsolutePath());
					} else {
						deleteFile(f.getAbsolutePath());
					}
				}
			}
			deleteFile(fileDir.getAbsolutePath());
		}
	}

	public static boolean isDirectoryAccessiable(String dir) {
		File dirF = new File(dir);
		return dirF.isDirectory() && dirF.canRead();
	}
	
	public static String longToMediaString(long fileSize) {
		if (fileSize == 0) {
			return "0MB";
		}
		float kbSize = fileSize / 1024f;
		if (kbSize < 1024) {
			return (int) kbSize + "KB";
		}
		float MBSize = kbSize / 1024f;
		if (MBSize < 1024) {
			return (int) MBSize + "MB";
		}
		return (int) (MBSize / 1024f) + "GB";
	}

	public static void copyUnSafe(InputStream is, String destPath) throws IOException {
		try {
			copy(is, destPath);
		} catch (Exception ex) {
		}
	}

	public static void copy(File srcFile, File descFile) {
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			inBuff = new BufferedInputStream(new FileInputStream(srcFile));
			outBuff = new BufferedOutputStream(new FileOutputStream(descFile));

			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			outBuff.flush();
		} catch (Exception e) {
		} finally {
			try {
				if (inBuff != null)
					inBuff.close();
				if (outBuff != null)
					outBuff.close();
			} catch (Exception e2) {
			}

		}
	}

	public static int copy(String srcPath, String descPath) {
		try {
			InputStream from = new FileInputStream(srcPath);
			return copy(from, descPath);
		} catch (FileNotFoundException e) {
			return -1;
		}
	}

	public static int copy(InputStream from, String descPath) {
		OutputStream to = null;
		try {
			deleteFile(descPath);
			to = new FileOutputStream(descPath);
			byte buf[] = new byte[1024];
			int c;
			while ((c = from.read(buf)) > 0) {
				to.write(buf, 0, c);
			}
			return 0;
		} catch (Exception ex) {
		} finally{
			if(to != null){
				try{
					to.close();
				}catch(Exception e){
				}
			}
			if(from != null){
				try{
					from.close();
				}catch(Exception e){
				}
			}
		}
		return -1;
	}

}