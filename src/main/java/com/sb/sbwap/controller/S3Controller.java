package com.sb.sbwap.controller;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sb.sblib.util.AwsS3Util;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class S3Controller {

    /** AWS S3アクセスユーティリティ */
    private final AwsS3Util awsS3Util;

    @GetMapping("/s3")
    public String getS3() {

        // S3テストページに遷移する
        return "s3";
    }

    @GetMapping("/s3/upload")
    public String getS3Upload(Model model) throws IOException {

        // S3にファイルをアップロードする
        ClassPathResource classPathResource = new ClassPathResource("s3/local/s3test.xlsx");
        awsS3Util.uploadFile("appstrage", "s3test.xlsx", classPathResource.getFile());
        awsS3Util.uploadFile("appstrage", "test/s3/upload/s3test.xlsx", classPathResource.getFile());
        awsS3Util.uploadFile("appstrage", "test/s3/upload_2/s3test_2.xlsx", classPathResource.getFile());

        // アップロードしたファイルの一覧を取得し、モデルに追加する
        addFileNamesToModel(model);

        // S3テストページに遷移する
        return "s3";
    }

    @GetMapping("/s3/download")
    public String getS3Download(Model model) throws IOException {

        // S3からファイルをダウンロードする
        ClassPathResource classPathResource = new ClassPathResource("s3/local/s3test_download.xlsx");
        awsS3Util.downloadFile("appstrage", "test/s3/upload/s3test.xlsx", classPathResource.getFile());
        classPathResource = new ClassPathResource("s3/local/s3test_download_2.xlsx");
        awsS3Util.downloadFile("appstrage", "test/s3/upload_2/s3test_2.xlsx", classPathResource.getFile());

        // アップロードしたファイルの一覧を取得し、モデルに追加する
        addFileNamesToModel(model);

        // S3テストページに遷移する
        return "s3";
    }

    @GetMapping("/s3/list")
    public String getS3List(Model model) throws IOException {

        // アップロードしたファイルの一覧を取得し、モデルに追加する
        addFileNamesToModel(model);

        // S3テストページに遷移する
        return "s3";
    }

    @GetMapping("/s3/delete")
    public String getS3Delete(Model model) throws IOException {

        // S3からファイルを削除する
        awsS3Util.deleteFile("appstrage", "test/s3/upload/s3test.xlsx");
        awsS3Util.deleteFile("appstrage", "test/s3/upload_2/s3test_2.xlsx");

        // アップロードしたファイルの一覧を取得し、モデルに追加する
        addFileNamesToModel(model);

        // S3テストページに遷移する
        return "s3";
    }

    private void addFileNamesToModel(Model model) {

        // S3からファイルの一覧を取得する
        StringBuilder fileNames = new StringBuilder();
        awsS3Util.listFiles("appstrage", "test/s3/upload/")
                .forEach(fileName -> {
                    if (fileNames.length() > 0) {
                        fileNames.append(", ");
                    }
                    fileNames.append(fileName);
                });

        // モデルにファイル名を追加する
        model.addAttribute("fileNames", fileNames.toString());
    }
}
