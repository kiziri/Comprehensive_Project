package aimproject.aim;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;


public class MultipartTest {
    @Test
    public void test() {
        sendMultiPart();
    }

    private final static String boundary = "aJ123Af2318";//바운더리 : 각 input의 경계
    private final static String LINE_FEED = "\r\n";// 값 구분
    private final static String charset = "utf-8"; //인코드 설정
    private OutputStream outputStream;
    private PrintWriter writer;

    public void addTextPart(String name, String value) throws IOException {// iput text 추가 함수 삭제 해도 됨
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"").append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    public void addFilePart(String name, File file) throws IOException {//input file 추가 함수 필수
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + file.getName() + "\"").append(LINE_FEED);
        writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName())).append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        FileInputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();
        writer.append(LINE_FEED);
        writer.flush();
    }


    public void sendMultiPart() {// 메인 함수 전송 반환 모든 부분을 담당
        try {
            File file = new File("src/main/resources/static/imageupload/face.jpg");// 보낼 파일 설정

            URL url = new URL("http://localhost:3000/up");//url 경로 설정
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 연결 생성

            //http 해더 설정
            connection.setRequestProperty("Content-Type", "multipart/form-data;charset=" + charset + ";boundary=" + boundary);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            //body 작성
            outputStream = connection.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);


            //body 데이터 세팅
            addFilePart("img", file);
            writer.append("--" + boundary + "--").append(LINE_FEED);
            writer.close();


            String responseData = "";
            String returnData = "";

            //http 요청 후 응답 받은 데이터를 버퍼에 쌓는다
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuffer sb = new StringBuffer();
            while ((responseData = br.readLine()) != null) {
                sb.append(responseData); //StringBuffer에 응답받은 데이터 순차적으로 저장 실시
            }

            //응답 받은 데이터 출력
            returnData = sb.toString();

            System.out.println(returnData);

            //오브젝트 화
            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(returnData);
            JSONArray arr= (JSONArray)object.get("data");

            //오브젝트 내부에서 볼러오기
            JSONObject temp = (JSONObject) arr.get(0);
            //int face = (int) temp.get("face");
            System.out.println(temp.toString());
            System.out.println(temp.get("sad").toString());








        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
