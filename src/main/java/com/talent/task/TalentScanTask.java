package com.talent.task;

import com.talent.domain.Talent;
import com.talent.service.TalentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

@EnableScheduling
@Component
public class TalentScanTask {

    @Value("${talent.scan.folder.path}")
    private String folderPath;

    @Value("${talent.repo.path}")
    private String repoPath;

    @Value("${talent.file.prestr}")
    private String prestr;

    @Autowired
    private TalentService talentService;

    @Scheduled(initialDelay = 5 * 1000, fixedDelay = 60 * 1000)
    private void scanAndGenerate() throws Exception {
        File folderDirectory = new File(folderPath);
        if (!folderDirectory.isDirectory()) {
            throw new Exception(folderPath + " input path is not a Directory , please input the right path of the Directory. ^_^...^_^");
        } else {
            File[] files = folderDirectory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().startsWith(prestr)) {
                        // 读取文件
                        FileInputStream fis = new FileInputStream(file);
                        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                        StringBuilder sb = new StringBuilder();
                        String s;
                        while ((s = br.readLine()) != null) {
                            sb.append(s);
                        }

                        //入库
                        Talent talent = new Talent();
                        talent.setDoc(sb.toString());
                        talentService.generateIndex(talent);

                        // 转移文件
                        File repoDirectory = new File(repoPath);
                        //判断文件夹是否创建，没有创建则创建新文件夹
                        if (!repoDirectory.exists()) {
                            repoDirectory.mkdirs();
                        }
                        if (file.renameTo(new File(repoPath + file.getName()))) {
                            System.out.println("File is moved successful!");
                        } else {
                            System.out.println("File is failed to move!");
                        }
                    }
                }
            }
        }
    }

}
