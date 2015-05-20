package com.fangchehome.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Shell {
	
	private Process pro = null;
	private Runtime runTime = null;
	
	public Shell() {
		runTime = Runtime.getRuntime();
		if (runTime == null) {
			System.err.println("创建运行环境失败!");
			System.exit(1);
		}
	}

	public String execueteCommand(String command) throws InterruptedException {
		StringBuffer sbTip = new StringBuffer();
		sbTip.append("************************");
		sbTip.append("执行命令："+command);
		sbTip.append("执行结果：");
		
		try {
			pro = runTime.exec(command);
			/**
			 * 这个输入流是获取shell输出的
			 */
			BufferedReader input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			/**
			 * 这个输出流主要是对Process进行输入控制用的
			 */
			PrintWriter output = new PrintWriter(new OutputStreamWriter(pro.getOutputStream()));
			
			String line = null;
			
			while ((line = input.readLine()) != null) {
				//System.out.println(line);
			}
			sbTip.append("************************\n");
			input.close();
			output.close();
			pro.destroy();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return sbTip.toString();
	}
}