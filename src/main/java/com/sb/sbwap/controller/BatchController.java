package com.sb.sbwap.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job jobA;
    private final Job jobB;

    @GetMapping("/batch")
    public String getBatch() {

        // バッチテストページに遷移する
        return "batch";
    }

    @GetMapping("/batch/taskletA")
    public String getTaskletA(Model model) throws Exception {

        // タスクレットAを実行する
        jobLauncher.run(jobA, createJobParameters().toJobParameters());

        // バッチテストページに遷移する
        return "batch";
    }

    @GetMapping("/batch/taskletB")
    public String getTaskletB(Model model) throws Exception {

        // タスクレットBを実行する
        jobLauncher.run(jobB, createJobParameters().toJobParameters());

        // バッチテストページに遷移する
        return "batch";
    }

    private JobParametersBuilder createJobParameters() {
        return new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis());
    }
}
