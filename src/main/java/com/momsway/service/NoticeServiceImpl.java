package com.momsway.service;

import com.momsway.domain.Notice;
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
    @Transactional
    public void addNoticeReadNo(Long nid) {
        Notice notice = noticeRepository.findByNid(nid);
        notice.setReadNo(notice.getReadNo()+1L);
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
    @Transactional
    public int delNotice(Long nid, String saveFolder) {
        int result = 0;
        try{
            NoticeDTO find = findByNid(nid);
            noticeImgFileRemove(find.getImgPaths(),saveFolder);
            noticeRepository.deleteById(nid);
            result = 1;
        }catch (Exception e){
            log.error("delNotice...{}",e);
        }
        return result;
    }

    private void noticeImgFileRemove(List<String> imgPaths, String saveFolder) {
        File file = null;
        for(String fname:imgPaths){
            file = new File(saveFolder + "/" + fname);
            file.delete();
        }
    }

    @Override
    @Transactional
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
        log.info("insertNotice getFiles[0] name...{}",dto.getFiles().get(0).getOriginalFilename());
        if(dto.getFiles()!=null && !dto.getFiles().get(0).getOriginalFilename().equals("")){
            // multipartfile 을 notice_img 테이블에 insert
            insertNoticeImgs(dto.getFiles(),saveFolder,newNotice);
        }
        return newNotice.getNid();
    }

    private void insertNoticeImgs(List<MultipartFile> files, String saveFolder, Notice entity){
        List<String> fnames;
        Iterable<NoticeImg> noticeImgEntities;
            // multipartfile 을 notice_img 테이블에 insert
            fnames = noticeImgFileUpload(saveFolder,files);
            for(String s:fnames){
                log.info("fnames...{}",s);
            }
            noticeImgEntities = connetNoticeToImg(fnames, entity);
            noticeImgRepository.saveAll(noticeImgEntities);
    }

    @Override
    @Transactional
    public Long updateNotice(NoticeDTO dto, String saveFolder) {
        // dto.imgPaths delete
        if(dto.getImgPaths()!=null && dto.getImgPaths().size()>0){
            deleteNoticeImg(dto.getImgPaths(),saveFolder);
        }
        // find notice entity and set
        Notice parent = noticeRepository.findByNid(dto.getNid());
        parent.setNotify(dto.getNotify());
        parent.setCategory(dto.getCategory());
        parent.setTitle(dto.getTitle());
        parent.setContent(dto.getContent());
        // dto.getFiles 는 insertNoticeImgs()
        if(dto.getFiles()!=null && !dto.getFiles().get(0).getOriginalFilename().equals("")){
            insertNoticeImgs(dto.getFiles(),saveFolder,parent);
        }
        return parent.getNid();
    }

    private void deleteNoticeImg(List<String> imgPaths, String saveFolder) {
        noticeImgFileRemove(imgPaths,saveFolder);
        for(String path:imgPaths){
            noticeImgRepository.deleteAllByImgPath(path);
        }
    }

    private Iterable<NoticeImg> connetNoticeToImg(List<String> fnames, Notice newNotice) {
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
        } catch (IOException e){
            log.info("noticeImgFileUpload exception...{}",e);
            // transactional 처리가 되어야 하므로 file 을 모두 삭제한다.
            for(int i=0; i<files.size(); i++){
                saveFile.get(i).delete();
            }
        }
        return saveFileNames;
    }
}
