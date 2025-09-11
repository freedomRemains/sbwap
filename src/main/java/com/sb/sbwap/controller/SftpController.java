package com.sb.sbwap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sb.sblib.util.SftpUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SftpController {

    /** SFTPアクセスユーティリティ */
    private final SftpUtil sftpUtil;

    @GetMapping("/sftp")
    public String getS3() {

        // SFTPテストページに遷移する
        return "sftp";
    }

    @GetMapping("/sftp/upload")
    public String getS3Upload(Model model) throws Exception {

        // AWS S3にファイルをアップロードする
        sftpUtil.upload("sftp/local/sftptest.xlsx", "/upload/sftptest.xlsx");

        // // アップロードしたファイルの一覧を取得し、モデルに追加する
        // addFileNamesToModel(model);

        // SFTPテストページに遷移する
        return "sftp";
    }

    // @GetMapping("/sftp/download")
    // public String getSftpDownload(Model model) throws Exception {

    //     // AWS S3からファイルをダウンロードする
    //     ClassPathResource resource = new ClassPathResource("sftp/local");
    //     sftpUtil.download("test/sftp/upload", "sftptest.xlsx", resource.getFile().getAbsolutePath(), "sftptest_download.xlsx");
    //     sftpUtil.download("test/sftp/upload_2", "sftptest_2.xlsx", resource.getFile().getAbsolutePath(), "sftptest_download_2.xlsx");

    //     // モデルにファイル名を追加する
    //     model.addAttribute("fileNames", "【ダウンロード先ディレクトリ】" + resource.getFile().getAbsolutePath());

    //     // SFTPテストページに遷移する
    //     return "sftp";
    // }

    // @GetMapping("/sftp/list")
    // public String getSftpList(Model model) throws IOException {

    //     // アップロードしたファイルの一覧を取得し、モデルに追加する
    //     addFileNamesToModel(model);

    //     // SFTPテストページに遷移する
    //     return "sftp";
    // }

    // @GetMapping("/sftp/delete")
    // public String getSftpDelete(Model model) throws IOException {

    //     // S3からファイルを削除する
    //     sftpUtil.delete("sftptest.xlsx");
    //     sftpUtil.delete("test/sftp/upload", "sftptest.xlsx");
    //     sftpUtil.delete("test/sftp/upload_2", "sftptest_2.xlsx");

    //     // アップロードしたファイルの一覧を取得し、モデルに追加する
    //     addFileNamesToModel(model);

    //     // SFTPテストページに遷移する
    //     return "sftp";
    // }

    // private void addFileNamesToModel(Model model) {

    //     // S3からファイルの一覧を取得する
    //     StringBuilder fileNames = new StringBuilder();
    //     sftpUtil.listFiles("")
    //             .forEach(fileName -> {
    //                 if (fileNames.length() > 0) {
    //                     fileNames.append(", ");
    //                 }
    //                 fileNames.append(fileName);
    //             });

    //     // モデルにファイル名を追加する
    //     model.addAttribute("fileNames", fileNames.toString());
    // }
}
