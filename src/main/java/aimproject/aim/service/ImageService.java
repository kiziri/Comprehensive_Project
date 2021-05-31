package aimproject.aim.service;

import aimproject.aim.model.AnalysisHistory;
import aimproject.aim.model.Image;
import aimproject.aim.model.Member;
import aimproject.aim.repository.AnalysisHistoryRepository;
import aimproject.aim.repository.ImageRepository;
import aimproject.aim.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
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

    private static final String boundary = "aJ123Af2318";//바운더리 : 각 input의 경계
    private static final String LINE_FEED = "\r\n";// 값 구분
    private static final String charset = "utf-8"; //인코드 설정
    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;
    private final AnalysisHistoryRepository analysisHistoryRepository;
    private OutputStream outputStream;
    private PrintWriter writer;

    @Value("${file.upload.directory}")
    private String imagePath;

    @Value("${analysis.server.url}")
    private String analysisServerURL;

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
        String imageResourcePath = imagePath + imageName;

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

    /**
     * 이미지 분석 요청
     */
    public JSONObject requestForAnalysis(Image image) {
        // 저장된 회원의 이미지 경로 가져오기
        File file  = new File(image.getImagePath());
        JSONObject analysisObject = null;
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;


        // 파이썬 모델 분석 서버 접속 경로 설정
        try {
            URL analysisURL = new URL(analysisServerURL);
            connection = (HttpURLConnection) analysisURL.openConnection();// 연결 생성
            
            // HTTP 헤더 설정
            connection.setRequestProperty("Content-Type", "multipart/form-data;charset=" + charset + ";boundary=" + boundary);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            
            // HTTP 바디 설정
            outputStream = connection.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);

            // 설정된 바디에 데이터 삽입
            setImageToBody("img", file);

            // HTTP 응답 데이터 수신
            String responseData;
            String returnData;
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuffer stringBuffer = new StringBuffer();
            while ((responseData = bufferedReader.readLine()) != null) {
                stringBuffer.append(responseData); //StringBuffer 에 응답받은 데이터 순차적으로 저장 실시
            }
            // StringBuffer 형식을 String 형식으로 변환
            returnData = stringBuffer.toString();

            // JSONObject 로 변환
            JSONParser parser = new JSONParser();
            analysisObject = (JSONObject) parser.parse(returnData);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return analysisObject;
    }

    /**
     * body에 폼 데이터 형식으로 이미지 추가
     */
    public void setImageToBody(String formName, File file) {
        FileInputStream inputStream = null;

        // 이미지 데이터를 바디의 폼 데이터 형식으로 변환
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + formName + "\"; filename=\"" + file.getName() + "\"").append(LINE_FEED);
        writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName())).append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        try {
            // 변환한 형식을 바이트화하여 전송
            inputStream = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            writer.append(LINE_FEED);
            writer.flush();
            writer.append("--" + boundary + "--").append(LINE_FEED);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
