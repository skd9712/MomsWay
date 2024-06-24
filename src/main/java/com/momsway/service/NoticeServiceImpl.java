package com.momsway.service;

import com.momsway.domain.Notice;
import com.momsway.domain.NoticeCategory;
import com.momsway.domain.NoticeImg;
import com.momsway.dto.NoticeDTO;
import com.momsway.repository.notice.NoticeRepository;
import com.momsway.repository.noticeimg.NoticeImgRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeServiceImpl implements NoticeService{
    private final NoticeRepository noticeRepository;
    private final NoticeImgRepository noticeImgRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<NoticeDTO> findTopList() {
        List<Object[]> toplist = noticeRepository.findTopList();
        List<NoticeDTO> result = toplist.stream().map(item -> NoticeDTO.builder()
                        .nid((Long) item[0]).title((String) item[1])
                        .createAt((LocalDateTime) item[2])
                        .category((String) item[3])
                        .build())
                .collect(Collectors.toList());
        return result;
    }

    @Override
    @Transactional
    public Page<NoticeDTO> findList(Pageable pageable) {
        long count = listCount();
        List<NoticeDTO> list = noticeRepository.findList(pageable);
        return new PageImpl<>(list,pageable,count);
    }

    @Override
    public long listCount() {
        return noticeRepository.count();
    }

    @Override
    public NoticeDTO findByNid(Long nid) {
        Notice detail = noticeRepository.findByNid(nid);
        List<String> imgPaths = detail.getNoticeImgs().stream()
                .map(img->img.getImgPath()).collect(Collectors.toList());
        return NoticeDTO.builder()
                .nid(detail.getNid())
                .notify(detail.getNotify())
                .title(detail.getTitle())
                .content(detail.getContent())
                .readNo(detail.getReadNo())
                .category(detail.getCategory())
                .createAt(detail.getCreateAt())
                .imgPaths(imgPaths)
                .build();
    }

    @Override
    public int delNotice(Long nid) {
        int result = 0;
        try{
            noticeRepository.deleteById(nid);
            result = 1;
        }catch (Exception e){

        }
        return result;
    }

    @Override
    public Long insertNotice(NoticeDTO dto, String saveFolder) {
        // notice 보드 엔티티의 내용을 먼저 만들고 DB에 insert
        Notice board = Notice.builder()
                .category(dto.getCategory())
                .notify(dto.getNotify())
                .title(dto.getTitle())
                .content(dto.getContent())
                .readNo(0L)
                .build();
        Notice newNotice = noticeRepository.save(board);
        List<String> fnames;
        Iterable<NoticeImg> noticeImgEntities;
        if(dto.getFiles()!=null && dto.getFiles().size()!=0){
            // multipartfile 을 notice_img 테이블에 insert
            fnames = noticeImgFileUpload(saveFolder,dto.getFiles());
            for(String s:fnames){
                log.info("fnames...{}",s);
            }
            noticeImgEntities = insertNoticeImgs(fnames, newNotice);
            noticeImgRepository.saveAll(noticeImgEntities);
        }
        return newNotice.getNid();
    }
    private Iterable<NoticeImg> insertNoticeImgs(List<String> fnames, Notice newNotice) {
        List<NoticeImg> noticeImgEntities = new ArrayList<>();
        for(int i=0; i< fnames.size(); i++){
            NoticeImg noticeImg = NoticeImg.builder()
                    .imgPath(fnames.get(i))
                    .notice(newNotice)
                    .build();
            noticeImgEntities.add(noticeImg);
        }
        return noticeImgEntities;
    }

    private List<String> noticeImgFileUpload(String saveFolder, List<MultipartFile> files) {
        List<File> saveFile = new ArrayList<>();
        List<String> saveFileNames = new ArrayList<>();
        for(int i=0; i<files.size(); i++){
            // file 이름이 중복되는 것을 방지하기 위해 uuid를 붙이고 공백을 encoding 한 이름으로 변경
            UUID uuid = UUID.randomUUID();
            String fname = files.get(i).getOriginalFilename();
            URLEncoder.encode(fname, StandardCharsets.UTF_8)
                    .replace("+","%20");
            String filename = uuid+"_"+fname;
            // file 객체를 새로 만들고 변경된 이름으로 file 이름을 지정한다.
            saveFile.add(new File(saveFolder,filename));
            // 변경된 filename 을 리스트에 추가한다.
            saveFileNames.add(filename);
        }
        // multipartfiles 의 file 들을 새롭게 만든 file 객체로 변경
        try {
            for (int i = 0; i < files.size(); i++) {
                files.get(i).transferTo(saveFile.get(i));
            }
        }catch (IOException e){
            log.info("noticeImgFileUpload exception...{}",e);
            // transactional 처리가 되어야 하므로 file 을 모두 삭제한다.
            for(int i=0; i<files.size(); i++){
                saveFile.get(i).delete();
            }
        }
        return saveFileNames;
    }
}
