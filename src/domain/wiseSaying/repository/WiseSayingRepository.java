package domain.wiseSaying.repository;

import java.io.*;

public class WiseSayingRepository {

    String dirPath = System.getProperty("user.dir") + File.separator + "data";
    String lastIdFilePath= dirPath + File.separator + "lastId.txt";
    String jsonFilePath = dirPath + File.separator + "wiseSaying.json";

    public void createDirectoryIfNotExists(){
        File dir = new File(dirPath);
        if (!dir.exists()) {
           dir.mkdirs(); // 폴더가 없으면 생성
        }
    }

    public void writeJsonFile(String data){
        // 실제 파일에 저장
        try (FileWriter writer = new FileWriter(jsonFilePath)) {
            writer.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeLastIdFile(int lastId){
        // lastId 값을 파일에 저장
        try (FileWriter writer = new FileWriter(lastIdFilePath)) {
            writer.write(String.valueOf(lastId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public int readLastIdFile(){
        if (!new File(lastIdFilePath).exists()) { // 해당 경로에 파일이 존재하는지 확인
            // 파일이 없으면 lastId 0으로 초기화
            return 0;
        }

        // 파일이 존재하면 파일 읽어와서 lastId 초기화
        try(BufferedReader br = new BufferedReader(new FileReader(lastIdFilePath))) {
            return Integer.parseInt(br.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readJsonFile(){
        if (new File(jsonFilePath).exists()) {
            StringBuilder sb = new StringBuilder();

            try (BufferedReader br = new BufferedReader(new FileReader(jsonFilePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return sb.toString().trim();
        }else {
            // 파일이 없으면 새로 생성 (빈 배열 등 기본값 저장)
            try (FileWriter writer = new FileWriter(jsonFilePath)) {
                writer.write("[]"); // 빈 배열로 초기화
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return "[]"; // 빈 배열 반환
        }

    }


}
