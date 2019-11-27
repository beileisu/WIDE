package com.wide.ui.views.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

public class SSHCommandsUtil {

    private static final String MESSAGE_LINE_END = "\n";

    public static void startHTTPServer(String hostname, String username, String password, String homePath) {
        StringBuffer commands = startHTTPServerCommand(homePath);
        executeCommand(hostname, username, password, commands.toString());
    }

    public static StringBuffer startHTTPServerCommand(String homePath) {
        StringBuffer shell = cdHomePath(homePath);
        appendCommand(shell, "start httpd.exe");
        exit(shell);
        return shell;
    }

    public static void reStartHTTPServer(String hostname, String username, String password, String homePath) {
        StringBuffer commands = restartHTTPServerCommand(homePath);
        executeCommand(hostname, username, password, commands.toString());
    }

    public static StringBuffer restartHTTPServerCommand(String homePath) {
        StringBuffer shell = cdHomePath(homePath);
        appendCommand(shell, "taskkill /f /im httpd.exe && start httpd.exe");
        exit(shell);
        return shell;
    }
    
    public static void stopHTTPServer(String hostname, String username, String password, String homePath) {
        StringBuffer commands = startHTTPServerCommand(homePath);
        executeCommand(hostname, username, password, commands.toString());
    }

    public static StringBuffer stopHTTPServerCommand(String homePath) {
        StringBuffer shell = cdHomePath(homePath);
        appendCommand(shell, "taskkill /f /im httpd.exe");
        exit(shell);
        return shell;
    }
    
    public static StringBuffer cdWindchillDSHomePath(String homePath) {
        String disk = StringUtils.substringBefore(homePath, ":") + ":";
        StringBuffer command = buildCommand();

        appendCommand(command, disk);
        cd(command, homePath + "/server/bat");
        return command;
    }
    
    public static void startWindchillDS(String hostname, String username, String password, String homePath) {
        StringBuffer commands = startWindchillDSCommand(homePath);
        String message = executeCommand(hostname, username, password, commands.toString());
        System.out.println(message);
    }

    public static StringBuffer startWindchillDSCommand(String homePath) {
        StringBuffer shell = cdWindchillDSHomePath(homePath);
        appendCommand(shell, "start-ds.bat");
        exit(shell);
        return shell;
    }

    public static void reStartWindchillDS(String hostname, String username, String password, String homePath) {
        StringBuffer commands = reStartWindchillDSCommand(homePath);
        String message = executeCommand(hostname, username, password, commands.toString());
        System.out.println(message);
    }

    public static StringBuffer reStartWindchillDSCommand(String homePath) {
        StringBuffer shell = cdWindchillDSHomePath(homePath);
        appendCommand(shell, "stop-ds.bat && start-ds.bat");
        exit(shell);
        return shell;
    }
    
    public static void stopWindchillDS(String hostname, String username, String password, String homePath) {
        StringBuffer commands = stopWindchillDSCommand(homePath);
        String message = executeCommand(hostname, username, password, commands.toString());
        System.out.println(message);
    }

    public static StringBuffer stopWindchillDSCommand(String homePath) {
        StringBuffer shell = cdWindchillDSHomePath(homePath);
        appendCommand(shell, "stop-ds.bat");
        exit(shell);
        return shell;
    }
    
    public static StringBuffer cdHomePath(String homePath) {
        String disk = StringUtils.substringBefore(homePath, ":") + ":";
        StringBuffer command = buildCommand();

        appendCommand(command, disk);
        cd(command, homePath + "/bin");
        return command;
    }
    
    public static List<String> checkHTTPServerStatus(String hostname, String username, String password) {
        List<String> pidList = Lists.newArrayList();

        StringBuffer commands = SSHCommandsUtil.searchHTTPServerPIDCommand();

        String message = executeCommand(hostname, username, password, commands.toString());
        List<String> messageList = Lists.newArrayList(StringUtils.split(message, MESSAGE_LINE_END));

        for (String string : messageList) {
            if (string.contains("httpd.exe")) {
                List<String> list = Lists.newArrayList(StringUtils.split(string, " "));
                if (list.size() == 2) {
                    pidList.add(list.get(1));
                }
            }
        }
        return pidList;
    }
    
    public static StringBuffer searchHTTPServerPIDCommand() {
        StringBuffer command = buildCommand();
        appendCommand(command, "wmic process where name=\"httpd.exe\" get processid,executablepath");
        exit(command);
        return command;
    }
    
    public static void startWindchill(String hostname, String username, String password, String windchillHome) {
        StringBuffer commands = startWindchillCommand(windchillHome);
        executeCommand(hostname, username, password, commands.toString());
    }

    public static StringBuffer startWindchillCommand(String windchillHome) {
        StringBuffer shell = windchillShell(windchillHome);
        appendCommand(shell, "windchill start");
        exit(shell);
        return shell;
    }

    public static void stopWindchill(String windchillHome) {
        StringBuffer shell = windchillShell(windchillHome);
        appendCommand(shell, "windchill stop");
        exit(shell);
    }

    public static void restartWindchill(String windchillHome) {
        StringBuffer shell = windchillShell(windchillHome);
        appendCommand(shell, "windchill stop && windchill start");
        exit(shell);
    }

    public static StringBuffer windchillShell(String windchillHome) {
        String disk = StringUtils.substringBefore(windchillHome, ":") + ":";
        StringBuffer command = buildCommand();

        appendCommand(command, disk);
        cd(command, windchillHome + "/bin");
        appendCommand(command, "windchill shell");
        return command;
    }

    public static void appendCommand(StringBuffer command, String cmd) {
        command.append(cmd).append("\n\r");
    }

    public static void cd(StringBuffer command, String path) {
        appendCommand(command, "cd " + path);
    }

    public static void exit(StringBuffer command) {
        appendCommand(command, "exit");
    }

    public static List<String> getWindchillAppPID(String hostname, String username, String password) {
        List<String> pidList = Lists.newArrayList();

        StringBuffer commands = SSHCommandsUtil.searchWindchillAppPIDCommand();

        String message = executeCommand(hostname, username, password, commands.toString());
        List<String> messageList = Lists.newArrayList(StringUtils.split(message, MESSAGE_LINE_END));

        for (String string : messageList) {
            if (string.contains("java.exe")) {
                List<String> list = Lists.newArrayList(StringUtils.split(string, " "));
                if (list.size() == 2) {
                    pidList.add(list.get(1));
                }
            }
        }
        return pidList;
    }

    public static StringBuffer searchWindchillAppPIDCommand() {
        StringBuffer command = buildCommand();
        appendCommand(command, "wmic process where name=\"java.exe\" get processid,executablepath");
        exit(command);
        return command;
    }

    public static StringBuffer buildCommand() {
        StringBuffer command = new StringBuffer();
        return command;
    }

    public static String executeCommand(String hostname, String username, String password, String commands) {

        StringBuffer message = new StringBuffer();
        try {
            Connection conn = new Connection(hostname);
            conn.connect();

            boolean isAuthenticated = conn.authenticateWithPassword(username, password);
            if (isAuthenticated == false) {
                throw new IOException("Authorication failed");
            }
            Session sess = conn.openSession();

            sess.requestDumbPTY();
            sess.startShell();

            OutputStream stdin = sess.getStdin();
            InputStream stdout = sess.getStdout();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(stdin));
            bw.write(commands);
            bw.flush();
            InputStreamReader isr = new InputStreamReader(stdout, "GBK");
            BufferedReader br = new BufferedReader(isr);

            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                line = StringUtils.trimToEmpty(line);
                if (StringUtils.isNotEmpty(line)) {
                    message.append(line).append(MESSAGE_LINE_END);
                }
            }

            sess.close();
            conn.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return message.toString();
    }

    public static void main(String[] args) {
        String hostname = "192.168.197.132";
        String username = "wcadmin";
        String password = "wcadmin";
        String homePath = "E:\\ptc\\Windchill_11.0\\WindchillDS";
        
        //startHTTPServer(hostname, username, password, homePath);
        
        startWindchillDS(hostname, username, password, homePath);
        
        
    }

}
