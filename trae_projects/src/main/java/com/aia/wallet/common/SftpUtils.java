package com.aia.wallet.common;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Properties;
import java.util.Vector;

@Component
public class SftpUtils {

    private static final Logger log = LoggerFactory.getLogger(SftpUtils.class);

    @Value("${app.sftp.host}")
    private String host;

    @Value("${app.sftp.port}")
    private int port;

    @Value("${app.sftp.username}")
    private String username;

    @Value("${app.sftp.password}")
    private String password;

    public void downloadFile(String remoteDir, String localDir, String fileName) throws Exception {
        Session session = null;
        ChannelSftp channelSftp = null;
        try {
            session = connect();
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            channelSftp.cd(remoteDir);
            File localFile = new File(localDir + File.separator + fileName);
            if (!localFile.getParentFile().exists()) {
                localFile.getParentFile().mkdirs();
            }
            
            channelSftp.get(fileName, localFile.getAbsolutePath());
            log.info("Downloaded file: {} to {}", fileName, localFile.getAbsolutePath());

        } finally {
            disconnect(channelSftp, session);
        }
    }

    public void uploadFile(String remoteDir, String localFilePath, String remoteFileName) throws Exception {
        Session session = null;
        ChannelSftp channelSftp = null;
        try {
            session = connect();
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            try {
                channelSftp.cd(remoteDir);
            } catch (SftpException e) {
                channelSftp.mkdir(remoteDir);
                channelSftp.cd(remoteDir);
            }

            channelSftp.put(localFilePath, remoteFileName);
            log.info("Uploaded file: {} to {}/{}", localFilePath, remoteDir, remoteFileName);

        } finally {
            disconnect(channelSftp, session);
        }
    }

    // List files to find the daily file
    public Vector<ChannelSftp.LsEntry> listFiles(String remoteDir) throws Exception {
        Session session = null;
        ChannelSftp channelSftp = null;
        try {
            session = connect();
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            
            channelSftp.cd(remoteDir);
            return channelSftp.ls("*.csv");
        } finally {
            disconnect(channelSftp, session);
        }
    }

    public void moveFile(String srcPath, String destPath) throws Exception {
        Session session = null;
        ChannelSftp channelSftp = null;
        try {
            session = connect();
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            // Ensure destination directory exists
            int lastSlashIndex = destPath.lastIndexOf('/');
            if (lastSlashIndex > 0) {
                String directory = destPath.substring(0, lastSlashIndex);
                try {
                    channelSftp.cd(directory);
                } catch (SftpException e) {
                    // Directory does not exist, try to create it
                    try {
                        channelSftp.mkdir(directory);
                    } catch (SftpException mkEx) {
                        log.warn("Could not create directory {}: {}", directory, mkEx.getMessage());
                    }
                }
            }

            channelSftp.rename(srcPath, destPath);
            log.info("Moved file from {} to {}", srcPath, destPath);

        } finally {
            disconnect(channelSftp, session);
        }
    }

    private Session connect() throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, host, port);
        session.setPassword(password);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
        return session;
    }

    private void disconnect(ChannelSftp channelSftp, Session session) {
        if (channelSftp != null && channelSftp.isConnected()) {
            channelSftp.disconnect();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }
}
