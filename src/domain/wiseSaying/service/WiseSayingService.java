package domain.wiseSaying.service;

import domain.wiseSaying.entity.WiseSaying;
import domain.wiseSaying.repository.WiseSayingRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WiseSayingService {
    private final WiseSayingRepository wiseSayingRepository = new WiseSayingRepository();

    private final Map<Integer, WiseSaying> wiseSayingMap = new HashMap<>();
    private int lastId;

    // 특수문자 포함 여부 확인
    public boolean isContainSpecialChar(String inputString) {
        String SPECIAL_CHAR_PATTERN = ".*[^a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ ].*";
        return inputString.matches(SPECIAL_CHAR_PATTERN);
    }

    // 새로 만든 명언 mapdp 추가
    public void addWiseSaying(String contents, String writer) {
        lastId++;
        wiseSayingMap.put(lastId, new WiseSaying(lastId, contents, writer));
    }

    // map에 명언이 존재하는지 확인
    public boolean existsById(int id){
        return wiseSayingMap.containsKey(id);
    }

    // map에서 명언을 id로 찾아서 WiseSaying 객체 반환
    public WiseSaying getWiseSayingById(int id){
        return wiseSayingMap.get(id);
    }

    // 수정한 명언 map에 저장
    public void updateWiseSaying(WiseSaying wiseSaying) {
        wiseSayingMap.put(wiseSaying.getId(), wiseSaying);
    }

    // map에서 명언 삭제
    public void deleteWiseSaying(int deleteId) {
        wiseSayingMap.remove(deleteId);
    }

    // map에 저장된 모든 명언을 List로 반환
    public List<WiseSaying> getWiseSayingList() {
        return new ArrayList<>(wiseSayingMap.values());
    }

    public void build() {
        // map에 저장된 명언을 json 파일로 저장
        wiseSayingRepository.writeJsonFile(makeJsonString());
        // lastId 값 저장
        wiseSayingRepository.writeLastIdFile(lastId);
    }

    public void setup(){
        // 디렉토리 초기화
        wiseSayingRepository.createDirectoryIfNotExists();

        // ID 관리 초기화
        lastId = wiseSayingRepository.readLastIdFile();

        // 데이터 로딩
        String jsonData = wiseSayingRepository.readJsonFile();
        loadWiseSayings(jsonData);
    }

    // JSON 문자열로 변환하는 메소드
    private String makeJsonString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[\n"); // [ 삽입하고 줄바꿈
        List<WiseSaying> list = new ArrayList<>(wiseSayingMap.values());
        for (int i = 0; i < list.size(); i++) {
            WiseSaying wiseSaying = list.get(i);
            stringBuilder.append("  {\n");
            stringBuilder.append("    \"id\": ").append(wiseSaying.getId()).append(",\n");
            stringBuilder.append("    \"contents\": \"").append(wiseSaying.getContents()).append("\",\n");
            stringBuilder.append("    \"writer\": \"").append(wiseSaying.getWriter()).append("\"\n");
            if (i == list.size() - 1) {
                stringBuilder.append("  }\n"); // 마지막 객체 뒤에는 쉼표 없음
            } else {
                stringBuilder.append("  },\n"); // 그 외에는 쉼표 붙임
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    // JSON 문자열을 파싱하여 WiseSaying 객체로 변환하고 map에 저장하는 메소드
    private void loadWiseSayings(String json) {
        // 대괄호 제거
        if (json.startsWith("[")) json = json.substring(1);
        if (json.endsWith("]")) json = json.substring(0, json.length() - 1);

        // },{로 분리
        String[] objects = json.split("},\\s*\\{"); // \\s*는 공백 문자(스페이스, 탭, 줄바꿈) 0개 이상을 의미


        for (String objStr : objects) {
            // 중괄호 보정
            if (!objStr.startsWith("{")) objStr = "{" + objStr;
            if (!objStr.endsWith("}")) objStr = objStr + "}";

            // 각 필드 추출
            String idStr = getJsonValue(objStr, "id");
            String contents = getJsonValue(objStr, "contents");
            String writer = getJsonValue(objStr, "writer");

            // null 체크
            if (writer == null || contents == null || idStr == null || idStr.trim().isEmpty()) continue;

            int id = Integer.parseInt(idStr);

            wiseSayingMap.put(id, new WiseSaying(id, contents, writer));
        }
    }

    // JSON 문자열에서 특정 key의 값을 추출
    private String getJsonValue(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search);
        if (start == -1) return null;
        start += search.length();

        // 콜론 뒤 공백 무시
        while (start < json.length() && (json.charAt(start) == ' ' || json.charAt(start) == '\t')) {
            start++;
        }

        // 문자열 값
        if (json.charAt(start) == '"') {
            int end = json.indexOf("\"", start + 1);
            if (end == -1) return null;
            return json.substring(start + 1, end); // 큰따옴표 제외
        } else {
            // 숫자 값
            int end = start;
            while (end < json.length() && json.charAt(end) != ',' && json.charAt(end) != '}') {
                end++;
            }
            return json.substring(start, end).trim();
        }
    }
}
