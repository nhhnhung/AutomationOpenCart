package com.opencart.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class VideoUtil {

//    public static File convertAviToMp4(File folder) {
//        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".avi"));
//
//        if (files != null) {
//            for (File aviFile : files) {
//                String aviFilePath = aviFile.getAbsolutePath();
//                String mp4FilePath = aviFilePath.replace(".avi", ".mp4");
//
//                try {
//                    ProcessBuilder processBuilder = new ProcessBuilder(
//                            "ffmpeg", "-i", aviFilePath,
//                            "-vcodec", "libx264",
//                            "-pix_fmt", "yuv420p",
//                            mp4FilePath
//                    );
//                    processBuilder.inheritIO();
//                    Process process = processBuilder.start();
//                    process.waitFor();
//
//                    System.out.println("Đã chuyển đổi: " + mp4FilePath);
//                    return
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    public static File convertAviToMp4(File folder) {
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".avi"));

        if (files == null || files.length == 0) {
            System.out.println("Không tìm thấy file AVI để convert");
            return null;
        }
        File aviFile = files[0];

        String aviFilePath = aviFile.getAbsolutePath();
        String mp4FilePath = aviFilePath.replace(".avi", ".mp4");
        File mp4File = new File(mp4FilePath);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "ffmpeg",
                    "-y",                      // ghi đè nếu tồn tại
                    "-i", aviFilePath,
                    "-vcodec", "libx264",
                    "-pix_fmt", "yuv420p",
                    mp4FilePath
            );

            processBuilder.inheritIO();
            Process process = processBuilder.start();

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.out.println("Convert video thất bại, exitCode=" + exitCode);
                return null;
            }

            System.out.println("Đã chuyển đổi: " + mp4File.getAbsolutePath());

            // Xoá file AVI sau khi convert
            aviFile.delete();

            return mp4File;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
