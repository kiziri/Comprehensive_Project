package aimproject.aim.service;

import aimproject.aim.model.AnalysisHistory;
import aimproject.aim.model.Image;
import aimproject.aim.model.Member;
import aimproject.aim.repository.AnalysisHistoryRepository;
import aimproject.aim.repository.ImageRepository;
import aimproject.aim.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ImageService {

    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;
    private final AnalysisHistoryRepository analysisHistoryRepository;

    @Value("${file.upload.directory}")
    private String imagePath;

    /**
     * 이미지 경로 객체 반환
     */
    private Path getPath() {
        return Paths.get(imagePath);
    }

    /**
     * 이미지 정보 저장
     */
    @Transactional
    public Long save(String memberId, MultipartFile file) throws IOException {

        // 회원 정보 조회
        Member member = memberRepository.findOne(memberId);

        Image imageInfo = new Image();

        // 경로 폴더 생성 및 이미지 이름 중복 방지 처리
        init();
        String imageName = StringUtils.cleanPath(setImageNameByUUID(file.getOriginalFilename()));

        String imageResourcePath = getPath().resolve(imageName).toString();
        log.info(imageResourcePath);
        log.info(getPath().resolve(imageName).toString());

        imageInfo.setMember(member);
        imageInfo.setImageName(imageName);
        imageInfo.setImageOriginName(file.getOriginalFilename());
        imageInfo.setImagePath(imageResourcePath);
        imageInfo.setImageDate(LocalDateTime.now());

        // 이미지 저장 및 기록 저장을 위한 각 엔티티 모델 객체 생성
        Image image = Image.createImage(imageInfo, member);
        AnalysisHistory analysisHistory = AnalysisHistory.createHistory(member, image);

        // 파일 저장은 서비스단에서 구현
        File target = new File(imagePath, imageName);  // 파일명과 경로 지정
        FileCopyUtils.copy(file.getBytes(), target);    // target에 해당하는 파일 생성

        analysisHistoryRepository.save(analysisHistory);
        imageRepository.save(image);
        return image.getImageId();
    }

    /**
     * 이미지 이름 세팅 부분
     */
    public String setImageNameByUUID(String originalName) {

        UUID uid = UUID.randomUUID();   // UUID 랜덤 자동 생성 부문
        String imageName = uid + "_" + originalName;

        return imageName;
    }

    /**
     * 이미지 전체 조회
     */
    public List<Image> findAll() {
        return imageRepository.findAll();
    }

    /**
     * 이미지 건별 조회
     */
    public Image findOne(Long imageId) {
        return imageRepository.findOne(imageId);
    }

    /**
     * 이미지 아이디와 단건 조회
     */
    public Image findByImageId(String memberId, Long imageId) {
        return imageRepository.findByImageId(memberId, imageId);
    }

    /**
     * 이미지 이름과 단건 조회
     */
    public Image findByImageName(String memberId, String imageName) {
        return imageRepository.findByImageName(memberId, imageName);
    }

    /**
     * 이미지 업로드 폴더 존재 여부 및 생성
     */
    public void init() {
        try {
            Files.createDirectories(getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
