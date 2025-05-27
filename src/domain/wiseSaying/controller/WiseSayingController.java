package domain.wiseSaying.controller;

import domain.wiseSaying.entity.WiseSaying;
import domain.wiseSaying.service.WiseSayingService;


import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class WiseSayingController {

    private final WiseSayingService wiseSayingService = new WiseSayingService();
    Scanner scanner = new Scanner(System.in);

    public void addWiseSaying() {
        String contents = getValidatedInput("명언 : ");
        String writer = getValidatedInput("작가 : ");

        wiseSayingService.addWiseSaying(contents, writer);
        System.out.println("명언이 등록되었습니다");
    }

    private String getValidatedInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            if (!wiseSayingService.isContainSpecialChar(input)) {
                return input;
            }

            System.out.println("특수문자는 입력할 수 없습니다. 다시 입력해주세요.");
        }
    }

    public void updateWiseSaying(int updateId){
        if(!wiseSayingService.existsById(updateId)){
            System.out.println("해당 ID의 명언이 존재하지 않습니다.");
        }

        WiseSaying wiseSaying = wiseSayingService.getWiseSayingById(updateId);

        System.out.println("기존 명언: " + wiseSaying.getContents());
        System.out.print("새로운 명언: ");
        wiseSaying.setContents(scanner.nextLine());
        System.out.println("기존 작가: " + wiseSaying.getWriter());
        System.out.print("새로운 작가: ");
        wiseSaying.setWriter(scanner.nextLine());

        wiseSayingService.updateWiseSaying(wiseSaying);
        System.out.println("명언이 수정되었습니다.");
    }

    public void deleteWiseSaying(int deleteId){
        if(!wiseSayingService.existsById(deleteId)){
            System.out.println("해당 ID의 명언이 존재하지 않습니다.");
        }

        wiseSayingService.deleteWiseSaying(deleteId);
        System.out.println("명언이 삭제되었습니다.");
    }

    public void listWiseSayings() {
        System.out.println("번호 / 명언 / 작가");
        System.out.println("---------------------");

        List<WiseSaying> list = wiseSayingService.getWiseSayingList();

        // 최신순으로 정렬
        Collections.reverse(list);

        list.stream().forEach(wiseSaying -> System.out.printf("%d / %s / %s\n", wiseSaying.getId(), wiseSaying.getContents(), wiseSaying.getWriter()));

    }

    public void build(){
        wiseSayingService.build();
        System.out.println("빌드 성공");
    }

    public void setup(){
        wiseSayingService.setup();
    }
}
