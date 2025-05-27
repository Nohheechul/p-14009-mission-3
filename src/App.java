import domain.wiseSaying.controller.WiseSayingController;

import java.util.Scanner;

public class App {

    private final WiseSayingController wiseSayingController = new WiseSayingController();

    Scanner scanner = new Scanner(System.in);

    void run(){

        // 초기 설정: 데이터 폴더 생성, lastId 파일 읽기, JSON 파일 읽기
        wiseSayingController.setup();

        while (true) {
            System.out.print("명령) ");
            String commend = scanner.nextLine().trim();

            switch (commend.split("\\?")[0]) {
                case "등록":
                    wiseSayingController.addWiseSaying();
                    break;
                case "수정":
                    int updateId = Integer.parseInt(commend.split("=")[1]);
                    wiseSayingController.updateWiseSaying(updateId);
                    break;
                case "삭제":
                    int deleteId = Integer.parseInt(commend.split("=")[1]);
                    wiseSayingController.deleteWiseSaying(deleteId);
                    break;
                case "목록":
                    wiseSayingController.listWiseSayings();
                    break;
                case "빌드":
                    wiseSayingController.build();
                    break;
                case "종료":
                    System.out.println("== 종료 ==");
                    return;
                default:
            }
        }
    }
}
