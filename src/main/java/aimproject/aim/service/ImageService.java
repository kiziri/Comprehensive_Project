package aimproject.aim.service;

import aimproject.aim.model.Image;
import aimproject.aim.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    private final ImageRepository imageRepository;

    /**
     * 이미지 정보 저장
     */
    @Transactional
    public void save(Image image) {
        imageRepository.save(image);
    }

    /**
     * 이미지 전체 조회
     */


    /**
     * 이미지 건별 조회
     */


    /**
     * 이미지 아이디와 단건 조회
     */


    /**
     * 이미지 이름과 단건 조회
     */
    
    
    /**
     * 이미지 날짜와 단건 조회
     */
}
