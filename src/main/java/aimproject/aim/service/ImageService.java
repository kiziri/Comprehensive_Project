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
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.*;
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
        imageInfo.setContentType(file.getContentType());
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
        File file = new File(image.getImagePath());
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer;
        String responseData;
        String returnData;

        try {
            URL url = new URL(analysisServerURL);
            ;//url 경로 설정
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 연결 생성

            //http 해더 설정
            connection.setRequestProperty("Content-Type", "multipart/form-data;charset=" + charset + ";boundary=" + boundary);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            //body 데이터 세팅
            addFilePart("img", file, connection);

            //http 요청 후 응답 받은 데이터를 버퍼에 쌓는다
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            stringBuffer = new StringBuffer();
            while ((responseData = bufferedReader.readLine()) != null) {
                stringBuffer.append(responseData); //StringBuffer에 응답받은 데이터 순차적으로 저장 실시
            }
            //응답 받은 데이터 출력
            returnData = stringBuffer.toString();

            //오브젝트화
            JSONParser parser = new JSONParser();
            JSONObject resultObject = (JSONObject) parser.parse(returnData);

            return resultObject;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * body에 폼 데이터 형식으로 이미지 추가
     */
    public void addFilePart(String name, File file, HttpURLConnection connection) {
        PrintWriter writer = null;
        OutputStream outputStream = null;
        FileInputStream inputStream = null;

        try {
            //body 작성
            outputStream = connection.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);

            // 이미지 데이터를 바디의 폼 데이터 형식으로 변환
            writer.append("--" + boundary).append(LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + file.getName() + "\"").append(LINE_FEED);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName())).append(LINE_FEED);
            writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
            writer.append(LINE_FEED);
            writer.flush();

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
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
