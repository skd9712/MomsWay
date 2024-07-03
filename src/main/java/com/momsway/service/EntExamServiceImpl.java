package com.momsway.service;

import com.momsway.domain.EntExam;
import com.momsway.domain.User;
import com.momsway.dto.EntExamDTO;
import com.momsway.exception.CustomException;
import com.momsway.repository.entexam.EntExamRepository;
import com.momsway.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntExamServiceImpl implements EntExamService {

    @Value("${spring.servlet.multipart.location}")
    private String saveFolder;

    @Value("${spring.servlet.multipart.location}")
    private String filePath;
    private final EntExamRepository entExamRepository;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(EntExamService.class);

    @Override
    public Page<EntExamDTO> entlist(Pageable pageable, String search_txt) {
        Page<EntExamDTO> orderlist = entExamRepository.orderlist(pageable, search_txt);
        return orderlist;
    }

    @Override
    public long upload(String saveFolder, EntExamDTO dto, String username) {
        String imgPaths = null;
        if (!dto.getFiles().get(0).getOriginalFilename().equals(""))
            imgPaths = fileUpload(saveFolder, dto.getFiles());
        User user = userRepository.findByEmail(username);
        EntExam entity = EntExam.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .entExamUser(user)
                .imgPath(imgPaths)
                .readNo(0L)
                .build();
        log.info("service..uid...{}", dto.getUid());

        EntExam saveEntity = entExamRepository.save(entity);

        return saveEntity.getEid();
    }

    @Override
    public EntExamDTO findByEid(Long eid) {
        EntExamDTO detail = entExamRepository.findByEid(eid);
        if(detail==null)
            throw new CustomException("from entexamservice findByEid");
//조회수 증가
        entExamRepository.incrementReadNo(eid);
        return EntExamDTO.builder()
                .eid(detail.getEid())
                .title(detail.getTitle())
                .content(detail.getContent())
                .readNo(detail.getReadNo() + 1)
                .createAt(detail.getCreateAt())
                .imgPath(detail.getImgPath())
                .uid(detail.getUid())
                .email(detail.getEmail())
                .nickname(detail.getNickname())
                .build();
    }

    @Override
    public int delEnt(Long eid) {
        int result = 0;
        try {
            EntExamDTO entExamDTO = entExamRepository.findByEid(eid);
            if (entExamDTO != null) {
                String imgPath = entExamDTO.getImgPath();
                log.info("해당파일을 삭제합니다: {}", imgPath);
                if (imgPath != null && !imgPath.isEmpty()) {
                    File file = new File(saveFolder, imgPath);
                    log.info("절대 경로: {}", file.getAbsolutePath());
                    if (file.exists()) {
                        if (file.delete()) {
                            log.info("파일이 성공적으로 삭제되었습니다: {}", file.getAbsolutePath());
                        } else {
                            log.warn("파일삭제 실패: {}", file.getAbsolutePath());
                        }
                    } else {
                        log.info("파일이 존재하지 않습니다: {}", file.getAbsolutePath());
                    }

                }
                entExamRepository.deleteById(eid);
                result = 1;
            } else {
                log.info("엔티티가 존재하지 않습니다: {}", eid);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    @Override
    @Transactional
    public long entUpdate(Long eid, String saveFolder, EntExamDTO dto) {
        log.info("Starting update for eid: {}", eid);
        EntExam entExam = entExamRepository.findById(eid)
                .orElseThrow(() -> new IllegalArgumentException("Invalid eid: " + eid));
        log.info("Fetched EntExam: {}", entExam);
        log.info("...기존title...{}", dto.getTitle());
        log.info("...기존content...{}", dto.getContent());
        log.info("...기존Paths...{}", entExam.getImgPath());

        // 새로운 이미지 업로드
        String newImgPath = null;
        if (dto.getFiles() != null && !dto.getFiles().get(0).getOriginalFilename().equals("")) {
            newImgPath = fileUpload(saveFolder, dto.getFiles());
            log.info("newImgPath...{}", newImgPath);
        }

        if (entExam.getImgPath() != null && newImgPath != null) {
            File existingFile = new File(saveFolder + "/" + entExam.getImgPath());
            if (existingFile.exists()) {
                existingFile.delete();
            }
        }
        log.info("..dto.imgPaths..{}", dto.getImgPaths());

        entExam.setTitle(dto.getTitle());
        entExam.setContent(dto.getContent());


        if (newImgPath != null) {
            entExam.setImgPath(newImgPath);
        } else {
            if (dto.getImgPaths() != null && dto.getImgPaths().size() > 0) {
                entExam.setImgPath(null);
            } else {
                entExam.setImgPath(entExam.getImgPath());
            }
        }


        entExamRepository.save(entExam); // 엔티티 저장

        return entExam.getEid();

    }


    private String fileUpload(String saveFolder, List<MultipartFile> files) {
        List<File> saveFile = new ArrayList<>();
        StringBuilder imgPaths = new StringBuilder();
        for (int i = 0; i < files.size(); i++) {
            UUID uuid = UUID.randomUUID();
            String fname = files.get(i).getOriginalFilename();
            URLEncoder.encode(fname, StandardCharsets.UTF_8)
                    .replace("+", "%20");
            String filename = uuid + "_" + fname;
            saveFile.add(new File(saveFolder, filename));
            imgPaths.append(filename);
        }
        try {
            for (int i = 0; i < files.size(); i++) {
                files.get(i).transferTo(saveFile.get(i));
            }
        } catch (IOException e) {
            System.out.println(e);
            for (int i = 0; i < files.size(); i++)
                saveFile.get(i).delete();
        }
        return imgPaths.toString();
    }

}
